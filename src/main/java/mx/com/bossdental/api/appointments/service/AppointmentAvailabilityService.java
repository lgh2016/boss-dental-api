package mx.com.bossdental.api.appointments.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.constants.AppointmentStatusCode;
import mx.com.bossdental.api.appointments.dto.LockAppointmentRequest;
import mx.com.bossdental.api.appointments.dto.LockAppointmentResponse;
import mx.com.bossdental.api.appointments.dto.StartSlotsResponse;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import mx.com.bossdental.api.appointments.repository.AppointmentStatusRepository;
import mx.com.bossdental.api.branches.entity.Branch;
import mx.com.bossdental.api.branches.repository.BranchRepository;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.patients.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentAvailabilityService {

    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime WEEKDAY_CLOSE_TIME = LocalTime.of(18, 0);
    private static final LocalTime SATURDAY_CLOSE_TIME = LocalTime.of(17, 0);
    private static final int SLOT_MINUTES = 30;
    private static final int DEFAULT_APPOINTMENT_MINUTES = 60;
    private static final int LOCK_MINUTES = 5;

    private final AppointmentStatusRepository appointmentStatusRepository;

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final PatientRepository patientRepository;

    public StartSlotsResponse getStartSlots(Long doctorId, Long branchId, LocalDate date) {

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden consultar horarios de fechas pasadas.");
        }

        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado."));

        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new IllegalArgumentException("El doctor no está activo.");
        }

        if (doctor.getRole() == null || !"DENTIST".equals(doctor.getRole().getName())) {
            throw new IllegalArgumentException("El usuario seleccionado no es doctor.");
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada."));

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException("La sucursal no está activa.");
        }

        List<String> blockingStatuses = List.of(
                AppointmentStatusCode.LOCKED,
                AppointmentStatusCode.CONFIRMED,
                AppointmentStatusCode.COMPLETED
        );

        List<Appointment> appointments = appointmentRepository.findAppointmentsBlockingAvailability(
                doctorId,
                branchId,
                date,
                blockingStatuses
        );

        List<LocalTime> availableSlots = buildSlots(date).stream()
                .filter(startTime -> !isPastSlot(date, startTime))
                .filter(startTime -> {
                    LocalTime endTime = startTime.plusMinutes(DEFAULT_APPOINTMENT_MINUTES);
                    return !hasConflict(startTime, endTime, appointments);
                })
                .toList();

        return new StartSlotsResponse(doctorId, branchId, date, availableSlots);
    }

    @Transactional
    public LockAppointmentResponse lockAppointment(LockAppointmentRequest request) {

        Patient patient = patientRepository.findById(request.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado."));

        if (!Boolean.TRUE.equals(patient.getActive())) {
            throw new IllegalArgumentException("El paciente no está activo.");
        }

        if (request.date().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se pueden bloquear horarios de fechas pasadas.");
        }

        User doctor = userRepository.findById(request.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor no encontrado."));

        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new IllegalArgumentException("El doctor no está activo.");
        }

        if (doctor.getRole() == null || !"DENTIST".equals(doctor.getRole().getName())) {
            throw new IllegalArgumentException("El usuario seleccionado no es doctor.");
        }

        Branch branch = branchRepository.findById(request.branchId())
                .orElseThrow(() -> new IllegalArgumentException("Sucursal no encontrada."));

        if (!Boolean.TRUE.equals(branch.getActive())) {
            throw new IllegalArgumentException("La sucursal no está activa.");
        }

        LocalTime defaultEndTime = request.startTime().plusMinutes(DEFAULT_APPOINTMENT_MINUTES);

        if (defaultEndTime.isAfter(getCloseTime(request.date()))) {
            throw new IllegalArgumentException("El horario seleccionado rebasa el horario del consultorio.");
        }

        List<String> blockingStatuses = List.of(
                AppointmentStatusCode.LOCKED,
                AppointmentStatusCode.CONFIRMED,
                AppointmentStatusCode.COMPLETED
        );

        List<Appointment> blockingAppointments =
                appointmentRepository.findAppointmentsBlockingAvailability(
                        request.doctorId(),
                        request.branchId(),
                        request.date(),
                        blockingStatuses
                );

        if (hasConflict(request.startTime(), defaultEndTime, blockingAppointments)) {
            throw new IllegalArgumentException("El horario seleccionado ya no está disponible.");
        }

        AppointmentStatus lockedStatus = appointmentStatusRepository
                .findByCodeAndActiveTrue(AppointmentStatusCode.LOCKED)
                .orElseThrow(() ->
                        new IllegalArgumentException("No existe el estatus LOCKED."));

        LocalDateTime lockedUntil = LocalDateTime.now().plusMinutes(LOCK_MINUTES);

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDentist(doctor);
        appointment.setBranch(branch);
        appointment.setAppointmentDate(request.date());
        appointment.setStartTime(request.startTime());
        appointment.setEndTime(defaultEndTime);
        appointment.setStatus(lockedStatus);
        appointment.setLockedUntil(lockedUntil);
        appointment.setActive(true);

        Appointment saved = appointmentRepository.save(appointment);

        List<LocalTime> endSlots = buildEndSlots(
                request.date(),
                request.startTime(),
                blockingAppointments
        );

        return new LockAppointmentResponse(
                saved.getId(),
                request.doctorId(),
                request.branchId(),
                request.date(),
                request.startTime(),
                defaultEndTime,
                AppointmentStatusCode.LOCKED,
                lockedUntil,
                endSlots
        );
    }

    private boolean isPastSlot(LocalDate date, LocalTime startTime) {
        return date.isEqual(LocalDate.now()) && startTime.isBefore(LocalTime.now());
    }

    private LocalTime getCloseTime(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case SATURDAY -> SATURDAY_CLOSE_TIME;
            case SUNDAY -> throw new IllegalArgumentException("El consultorio no abre los domingos.");
            default -> WEEKDAY_CLOSE_TIME;
        };
    }

    private List<LocalTime> buildSlots(LocalDate date) {
        List<LocalTime> slots = new java.util.ArrayList<>();
        LocalTime current = OPEN_TIME;
        LocalTime closeTime = getCloseTime(date);

        while (!current.plusMinutes(DEFAULT_APPOINTMENT_MINUTES).isAfter(closeTime)) {
            slots.add(current);
            current = current.plusMinutes(SLOT_MINUTES);
        }

        return slots;
    }

    private boolean hasConflict(LocalTime newStart, LocalTime newEnd, List<Appointment> appointments) {
        return appointments.stream()
                .filter(appointment -> appointment.getStartTime() != null && appointment.getEndTime() != null)
                .anyMatch(appointment ->
                        appointment.getStartTime().isBefore(newEnd)
                                && appointment.getEndTime().isAfter(newStart)
                );
    }

    private List<LocalTime> buildEndSlots(
            LocalDate date,
            LocalTime startTime,
            List<Appointment> blockingAppointments
    ) {
        List<LocalTime> endSlots = new java.util.ArrayList<>();

        LocalTime closeTime = getCloseTime(date);
        LocalTime currentEndTime = startTime.plusMinutes(SLOT_MINUTES);

        while (!currentEndTime.isAfter(closeTime)) {

            boolean hasConflict = hasConflict(
                    startTime,
                    currentEndTime,
                    blockingAppointments
            );

            if (hasConflict) {
                break;
            }

            endSlots.add(currentEndTime);
            currentEndTime = currentEndTime.plusMinutes(SLOT_MINUTES);
        }

        return endSlots;
    }
}
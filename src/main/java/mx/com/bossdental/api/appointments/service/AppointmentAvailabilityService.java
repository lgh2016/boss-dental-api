package mx.com.bossdental.api.appointments.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.constants.AppointmentStatusCode;
import mx.com.bossdental.api.appointments.dto.request.*;
import mx.com.bossdental.api.appointments.dto.response.*;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
import mx.com.bossdental.api.appointments.mapper.AppointmentMapper;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import mx.com.bossdental.api.appointments.repository.AppointmentStatusRepository;
import mx.com.bossdental.api.branches.entity.Branch;
import mx.com.bossdental.api.branches.repository.BranchRepository;
import mx.com.bossdental.api.exceptions.AppointmentLockExpiredException;
import mx.com.bossdental.api.exceptions.BusinessException;
import mx.com.bossdental.api.users.entity.User;
import mx.com.bossdental.api.users.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.patients.repository.PatientRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    private final AppointmentMapper appointmentMapper;

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

    /**
     * Confirma una cita previamente bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request       información de confirmación.
     * @return cita confirmada.
     */
    @Transactional
    public AppointmentResponse confirmAppointment(
            Long appointmentId,
            ConfirmAppointmentRequest request
    ) {

        /*
         * Buscar cita.
         */
        Appointment appointment = appointmentRepository.findById(
                        appointmentId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Appointment not found."
                        )
                );

        /*
         * Validar que la cita se encuentre LOCKED.
         */
        if (!"LOCKED".equals(
                appointment.getStatus().getCode()
        )) {

            throw new BusinessException(
                    "Only LOCKED appointments can be confirmed."
            );
        }

        /*
         * Validar expiración del lock.
         */
        if (appointment.getLockedUntil() == null
                || appointment.getLockedUntil()
                .isBefore(LocalDateTime.now())) {
            appointmentRepository.delete(appointment);

            throw new BusinessException(
                    "Appointment lock has expired."
            );
        }

        /*
         * Buscar paciente.
         */
        Patient patient = patientRepository.findById(
                        request.getPatientId()
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Patient not found."
                        ));

        /*
         * Buscar status CONFIRMED.
         */
        AppointmentStatus confirmedStatus =
                appointmentStatusRepository.findByCodeAndActiveTrue(
                                AppointmentStatusCode.CONFIRMED
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "CONFIRMED status not found."
                                ));

        /*
         * Completar información de la cita.
         */
        appointment.setPatient(patient);
        appointment.setReason(request.getReason());
        appointment.setNotes(request.getNotes());

        /*
         * Actualizar status.
         */
        appointment.setStatus(
                confirmedStatus
        );

        /*
         * Registrar fecha de confirmación.
         */
        appointment.setConfirmedAt(
                LocalDateTime.now()
        );

        /*
         * Limpiar lock temporal.
         */
        appointment.setLockedUntil(null);
        appointment.setLockedByUser(null);

        /*
         * Guardar cambios.
         */
        appointmentRepository.save(
                appointment
        );

        /*
         * Retornar response.
         */
        return appointmentMapper.toResponse(
                appointment
        );
    }

    /**
     * Actualiza la hora final
     * de una cita bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request nueva hora final.
     * @return lock actualizado.
     */
    @Transactional(
            noRollbackFor = AppointmentLockExpiredException.class
    )
    public LockAppointmentResponse updateEndTime(
            Long appointmentId,
            UpdateAppointmentEndTimeRequest request
    ) {

        /*
         * Buscar cita.
         */
        Appointment appointment = appointmentRepository.findById(
                        appointmentId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Appointment not found."
                        )
                );

        /*
         * Validar status LOCKED.
         */
        if (!AppointmentStatusCode.LOCKED.equals(
                appointment.getStatus().getCode()
        )) {

            throw new BusinessException(
                    "Only LOCKED appointments can be updated."
            );
        }

        /*
         * Validar expiración del lock.
         */
        if (appointment.getLockedUntil() == null
                || appointment.getLockedUntil()
                .isBefore(LocalDateTime.now())) {

            appointmentRepository.delete(appointment);
            appointmentRepository.flush();

            throw new AppointmentLockExpiredException(
                    "Appointment lock has expired."
            );
        }

        /*
         * Validar hora final.
         */
        if (!request.getEndTime().isAfter(
                appointment.getStartTime()
        )) {

            throw new BusinessException(
                    "End time must be after start time."
            );
        }

        /*
         * Validar horario del consultorio.
         */
        if (request.getEndTime().isAfter(
                getCloseTime(
                        appointment.getAppointmentDate()
                )
        )) {

            throw new BusinessException(
                    "The selected schedule exceeds clinic hours."
            );
        }

        /*
         * Buscar citas conflictivas.
         */
        List<String> blockingStatuses = List.of(
                AppointmentStatusCode.LOCKED,
                AppointmentStatusCode.CONFIRMED,
                AppointmentStatusCode.COMPLETED
        );

        List<Appointment> blockingAppointments =
                appointmentRepository.findAppointmentsBlockingAvailability(
                        appointment.getDentist().getId(),
                        appointment.getBranch().getId(),
                        appointment.getAppointmentDate(),
                        blockingStatuses
                );

        /*
         * Excluir cita actual.
         */
        blockingAppointments = blockingAppointments.stream()
                .filter(item ->
                        !item.getId().equals(
                                appointment.getId()
                        )
                )
                .toList();

        /*
         * Validar conflictos.
         */
        if (hasConflict(
                appointment.getStartTime(),
                request.getEndTime(),
                blockingAppointments
        )) {

            throw new BusinessException(
                    "The selected schedule is no longer available."
            );
        }

        /*
         * Actualizar hora final.
         */
        appointment.setEndTime(
                request.getEndTime()
        );

        /*
         * Renovar lock.
         */
        appointment.setLockedUntil(
                LocalDateTime.now().plusMinutes(
                        LOCK_MINUTES
                )
        );

        /*
         * Guardar cambios.
         */
        appointmentRepository.save(
                appointment
        );

        /*
         * Retornar response.
         */
        return new LockAppointmentResponse(
                appointment.getId(),
                appointment.getDentist().getId(),
                appointment.getBranch().getId(),
                appointment.getAppointmentDate(),
                appointment.getStartTime(),
                appointment.getEndTime(),
                AppointmentStatusCode.LOCKED,
                appointment.getLockedUntil(),
                List.of()
        );
    }

    /**
     * Actualiza la hora de inicio
     * de una cita bloqueada.
     *
     * Este flujo solo cambia la hora inicio.
     * La hora final se limpia porque debe recalcularse
     * desde el front usando el WS de horas fin disponibles.
     *
     * @param appointmentId ID de la cita.
     * @param request nueva hora inicio.
     * @return lock actualizado.
     */
    @Transactional(
            noRollbackFor = AppointmentLockExpiredException.class
    )
    public UpdateAppointmentStartTimeResponse updateStartTime(
            Long appointmentId,
            UpdateAppointmentStartTimeRequest request
    ) {

        /*
         * Buscar cita.
         */
        Appointment appointment = appointmentRepository.findById(
                        appointmentId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Appointment not found."
                        )
                );

        /*
         * Validar status LOCKED.
         */
            if (!AppointmentStatusCode.LOCKED.equals(
                appointment.getStatus().getCode()
        )) {

            throw new BusinessException(
                    "Only LOCKED appointments can be updated."
            );
        }

        /*
         * Validar expiración del lock.
         */
        if (appointment.getLockedUntil() == null
                || appointment.getLockedUntil()
                .isBefore(LocalDateTime.now())) {

            appointmentRepository.delete(appointment);
            appointmentRepository.flush();

            throw new AppointmentLockExpiredException(
                    "Appointment lock has expired."
            );
        }

        /*
         * Validar que la nueva hora inicio
         * no sea de una fecha pasada.
         */
        if (isPastSlot(
                appointment.getAppointmentDate(),
                request.getStartTime()
        )) {

            throw new BusinessException(
                    "Start time cannot be in the past."
            );
        }

        /*
         * Validar que la nueva hora inicio
         * esté dentro del horario del consultorio.
         */
        if (request.getStartTime().isBefore(OPEN_TIME)
                || !request.getStartTime().isBefore(
                getCloseTime(
                        appointment.getAppointmentDate()
                )
        )) {

            throw new BusinessException(
                    "Start time is outside clinic hours."
            );
        }

        /*
         * Actualizar nueva hora inicio.
         */
        appointment.setStartTime(
                request.getStartTime()
        );

        /*
         * Mantener bloqueo provisional.
         *
         * Aunque el front limpie visualmente la hora fin,
         * en base se deja una duración provisional para que
         * el nuevo horario siga bloqueado frente a otros usuarios.
         */
        appointment.setEndTime(
                request.getStartTime().plusMinutes(DEFAULT_APPOINTMENT_MINUTES)
        );


        /*
         * Renovar lock.
         */
        appointment.setLockedUntil(
                LocalDateTime.now().plusMinutes(
                        LOCK_MINUTES
                )
        );

        /*
         * Guardar cambios.
         */
        appointmentRepository.save(
                appointment
        );

        /*
         * Retornar lock actualizado.
         */
        return appointmentMapper.toUpdateStartTimeResponse(appointment);
    }

    /**
     * Actualiza el doctor de una cita bloqueada.
     *
     * Al cambiar doctor se conserva el mismo appointmentId,
     * pero se limpian las horas porque la disponibilidad cambia.
     *
     * @param appointmentId ID de la cita.
     * @param request nuevo doctor.
     * @return información mínima del lock actualizado.
     */
    @Transactional(
            noRollbackFor = AppointmentLockExpiredException.class
    )
    public UpdateAppointmentDentistResponse updateDentist(
            Long appointmentId,
            UpdateAppointmentDentistRequest request
    ) {

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() ->
                        new BusinessException("Appointment not found.")
                );

        if (!AppointmentStatusCode.LOCKED.equals(appointment.getStatus().getCode())) {
            throw new BusinessException("Only LOCKED appointments can be updated.");
        }

        if (appointment.getLockedUntil() == null
                || appointment.getLockedUntil().isBefore(LocalDateTime.now())) {

            appointmentRepository.delete(appointment);
            appointmentRepository.flush();

            throw new AppointmentLockExpiredException("Appointment lock has expired.");
        }

        User doctor = userRepository.findById(request.getDentistId())
                .orElseThrow(() ->
                        new BusinessException("Doctor not found.")
                );

        if (!Boolean.TRUE.equals(doctor.getActive())) {
            throw new BusinessException("Doctor is not active.");
        }

        if (doctor.getRole() == null || !"DENTIST".equals(doctor.getRole().getName())) {
            throw new BusinessException("Selected user is not a dentist.");
        }

        appointment.setDentist(doctor);

        /*
         * Al cambiar doctor, las horas anteriores ya no aplican.
         */
        appointment.setStartTime(null);
        appointment.setEndTime(null);

        appointment.setLockedUntil(
                LocalDateTime.now().plusMinutes(LOCK_MINUTES)
        );

        appointmentRepository.save(appointment);

        return appointmentMapper.toUpdateDentistResponse(appointment);
    }

    /**
     * Actualiza la fecha de una cita bloqueada.
     *
     * Al cambiar la fecha se conserva el mismo appointmentId,
     * pero se limpian las horas porque la disponibilidad cambia.
     *
     * @param appointmentId ID de la cita.
     * @param request nueva fecha.
     * @return información mínima del lock actualizado.
     */
    @Transactional(
            noRollbackFor = AppointmentLockExpiredException.class
    )
    public UpdateAppointmentDateResponse updateDate(
            Long appointmentId,
            UpdateAppointmentDateRequest request
    ) {

        /*
         * Buscar cita.
         */
        Appointment appointment = appointmentRepository.findById(
                        appointmentId
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Appointment not found."
                        )
                );

        /*
         * Validar status LOCKED.
         */
        if (!AppointmentStatusCode.LOCKED.equals(
                appointment.getStatus().getCode()
        )) {

            throw new BusinessException(
                    "Only LOCKED appointments can be updated."
            );
        }

        /*
         * Validar expiración del lock.
         */
        if (appointment.getLockedUntil() == null
                || appointment.getLockedUntil()
                .isBefore(LocalDateTime.now())) {

            appointmentRepository.delete(appointment);
            appointmentRepository.flush();

            throw new AppointmentLockExpiredException(
                    "Appointment lock has expired."
            );
        }

        /*
         * Validar que la nueva fecha no sea pasada.
         */
        if (request.getAppointmentDate().isBefore(
                LocalDate.now()
        )) {

            throw new BusinessException(
                    "Appointment date cannot be in the past."
            );
        }

        /*
         * Validar que el consultorio abra ese día.
         *
         * getCloseTime lanza error si es domingo.
         */
        getCloseTime(
                request.getAppointmentDate()
        );

        /*
         * Actualizar fecha.
         */
        appointment.setAppointmentDate(
                request.getAppointmentDate()
        );

        /*
         * Limpiar horarios porque la disponibilidad
         * depende de la nueva fecha.
         */
        appointment.setStartTime(null);
        appointment.setEndTime(null);

        /*
         * Renovar lock.
         */
        appointment.setLockedUntil(
                LocalDateTime.now().plusMinutes(
                        LOCK_MINUTES
                )
        );

        /*
         * Guardar cambios.
         */
        appointmentRepository.save(
                appointment
        );

        /*
         * Retornar response.
         */
        return appointmentMapper.toUpdateDateResponse(
                appointment
        );
    }


    // METODOS PRIVADOS.

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
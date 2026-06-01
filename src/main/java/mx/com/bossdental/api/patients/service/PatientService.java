package mx.com.bossdental.api.patients.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.activity.service.RegisterActivityService;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.repository.AppointmentRepository;
import mx.com.bossdental.api.clinicalrecords.entity.ClinicalRecord;
import mx.com.bossdental.api.clinicalrecords.repository.ClinicalRecordRepository;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.exceptions.BusinessException;
import mx.com.bossdental.api.patients.dto.request.PatientCreateRequest;
import mx.com.bossdental.api.patients.dto.response.AppointmentSummaryResponse;
import mx.com.bossdental.api.patients.dto.response.PatientDetailResponse;
import mx.com.bossdental.api.patients.dto.response.PatientListResponse;
import mx.com.bossdental.api.patients.dto.response.PatientResponse;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.patients.mapper.PatientMapper;
import mx.com.bossdental.api.patients.repository.PatientRepository;
import mx.com.bossdental.api.security.service.AuthenticatedUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final PatientMapper patientMapper;
    private final AppointmentRepository appointmentRepository;
    private final RegisterActivityService registerActivityService;
    private final AuthenticatedUserService authenticatedUserService;

    private static final String DEFAULT_BRANCH_NAME = "Ecatepec";

    public PageResponse<PatientListResponse> findAll(
            int page,
            int size,
            String query
    ) {

        Page<Patient> patients = patientRepository.findAllWithSearch(
                query,
                PageRequest.of(page, size)
        );

        return new PageResponse<>(
                patients.getContent()
                        .stream()
                        .map(patientMapper::toListResponse)
                        .toList(),
                patients.getNumber(),
                patients.getSize(),
                patients.getTotalElements(),
                patients.getTotalPages()
        );
    }

    @Transactional
    public PatientResponse createPatient(PatientCreateRequest request) {

        Long authenticatedUserId =
                authenticatedUserService.getAuthenticatedUserId();

        Patient patient = patientMapper.toEntity(request);
        patient.setActive(true);

        Patient savedPatient = patientRepository.save(patient);

        ClinicalRecord clinicalRecord = new ClinicalRecord();
        clinicalRecord.setPatient(savedPatient);
        clinicalRecord.setExpedientNumber(generateExpedientNumber());
        clinicalRecord.setActive(true);

        clinicalRecordRepository.save(clinicalRecord);

        registerActivityService.registerActivity(
                "USER",
                authenticatedUserId,
                savedPatient.getId(),
                "PATIENT_CREATED",
                "PATIENTS",
                "PATIENT",
                savedPatient.getId(),
                "Paciente creado",
                "Se creó el paciente " + buildPatientFullName(savedPatient)
        );

        return patientMapper.toResponse(savedPatient);
    }

    /**
     * Obtiene la información consolidada del paciente para
     * pintar el panel lateral de detalle.
     *
     * Actualmente los importes financieros se regresan en cero
     * porque los módulos de presupuesto, cotización y pagos
     * todavía no alimentan este resumen.
     *
     * @param patientId identificador único del paciente
     * @return detalle consolidado del paciente
     */
    @Transactional(readOnly = true)
    public PatientDetailResponse getPatientDetail(Long patientId) {
        Patient patient = patientRepository.findDetailById(patientId)
                .orElseThrow(() ->
                        new BusinessException(
                                String.format("No existe un paciente con id %s", patientId)
                        )
                );

        AppointmentSummaryResponse previousAppointment = appointmentRepository
                .findPreviousAppointmentsByPatientId(patientId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(this::mapAppointmentSummary)
                .orElse(null);

        AppointmentSummaryResponse nextAppointment = appointmentRepository
                .findNextAppointmentsByPatientId(patientId, PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .map(this::mapAppointmentSummary)
                .orElse(null);

        PatientDetailResponse response = patientMapper.toDetailResponse(patient);

        response.setDoctorName(nextAppointment != null ? nextAppointment.getDoctorName() : null);
        response.setBalance(BigDecimal.ZERO);
        response.setPaidAmount(BigDecimal.ZERO);
        response.setTotalBudgeted(BigDecimal.ZERO);
        response.setPreviousAppointment(previousAppointment);
        /**
         * Temporalmente todos los pacientes se muestran
         * asociados a la sucursal principal de Ecatepec.
         *
         * Cuando se implemente la asignación de sucursal
         * por paciente deberá reemplazarse esta lógica.
         */
        response.setLocation(DEFAULT_BRANCH_NAME);
        response.setNextAppointment(nextAppointment);

        return response;
    }

    /**
     * Construye el nombre completo del paciente.
     *
     * @param patient paciente.
     * @return nombre completo.
     */
    private String buildPatientFullName(Patient patient) {

        return patient.getName() + " " + patient.getLastName();
    }

    private String generateExpedientNumber() {
        int year = LocalDate.now().getYear();
        long nextNumber = clinicalRecordRepository.countByActiveTrue() + 1;
        return "EXP-" + year + "-" + String.format("%06d", nextNumber);
    }

    /**
     * Convierte una entidad de cita en su representación
     * resumida para el panel del paciente.
     *
     * @param appointment entidad de cita
     * @return resumen de la cita
     */
    private AppointmentSummaryResponse mapAppointmentSummary(Appointment appointment) {
        return AppointmentSummaryResponse.builder()
                .id(appointment.getId())
                .date(appointment.getAppointmentDate())
                .time(appointment.getStartTime())
                .reason(appointment.getReason())
                .doctorName(appointment.getDentist() != null
                        ? Stream.of(
                                appointment.getDentist().getName(),
                                appointment.getDentist().getLastName()
                        )
                        .filter(Objects::nonNull)
                        .filter(value -> !value.isBlank())
                        .collect(Collectors.joining(" "))
                        : null)
                .status(appointment.getStatus() != null
                        ? appointment.getStatus().getCode()
                        : null)
                .build();
    }
}
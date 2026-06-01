package mx.com.bossdental.api.appointments.repository;

import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query(value = """
        SELECT a.*
        FROM appointments a
        INNER JOIN appointment_statuses aps
            ON aps.id = a.status_id
        WHERE a.active = true
          AND a.dentist_id = :doctorId
          AND a.branch_id = :branchId
          AND a.appointment_date = :appointmentDate
          AND aps.code IN (:blockingStatuses)
          AND (
                aps.code <> 'LOCKED'
                OR a.locked_until > now()
              )
        """, nativeQuery = true)
    List<Appointment> findAppointmentsBlockingAvailability(
            @Param("doctorId") Long doctorId,
            @Param("branchId") Long branchId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("blockingStatuses") List<String> blockingStatuses
    );

    /*
     * Eliminar únicamente appointments
     * con status LOCKED cuyo tiempo
     * de bloqueo ya expiró.
     *
     * No eliminar locks vigentes.
     */
    @Modifying
    @Query("""
    DELETE FROM Appointment a
    WHERE a.status.code = :status
      AND a.lockedUntil IS NOT NULL
      AND a.lockedUntil < :now
""")
    int deleteExpiredLocks(
            @Param("status") String status,
            @Param("now") LocalDateTime now
    );

    /**
     * Consulta citas dentro de un rango mensual.
     *
     * Permite filtrar por doctor
     * y sucursal.
     *
     * @param startDate fecha inicial.
     * @param endDate fecha final.
     * @param doctorId doctor opcional.
     * @param branchId sucursal opcional.
     * @param statuses status visibles.
     * @return citas encontradas.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.active = true
          AND a.appointmentDate BETWEEN :startDate AND :endDate
          AND (:doctorId IS NULL OR a.dentist.id = :doctorId)
          AND (:branchId IS NULL OR a.branch.id = :branchId)
          AND a.status.code IN :statuses
        ORDER BY a.appointmentDate ASC,
                 a.startTime ASC
        """)
    List<Appointment> findMonthSchedule(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("doctorId") Long doctorId,
            @Param("branchId") Long branchId,
            @Param("statuses") List<String> statuses
    );

    /**
     * Consulta citas de un día específico.
     *
     * Permite filtrar por doctor
     * y sucursal.
     *
     * @param date fecha consultada.
     * @param doctorId doctor opcional.
     * @param branchId sucursal opcional.
     * @param statuses status visibles.
     * @return citas encontradas.
     */
    @Query("""
        SELECT a
        FROM Appointment a
        WHERE a.active = true
          AND a.appointmentDate = :date
          AND (:doctorId IS NULL OR a.dentist.id = :doctorId)
          AND (:branchId IS NULL OR a.branch.id = :branchId)
          AND a.status.code IN :statuses
        ORDER BY a.startTime ASC
        """)
    List<Appointment> findDaySchedule(
            @Param("date") LocalDate date,
            @Param("doctorId") Long doctorId,
            @Param("branchId") Long branchId,
            @Param("statuses") List<String> statuses
    );

    /**
     * Cuenta las citas activas del día, excluyendo citas canceladas o liberadas.
     *
     * @param appointmentDate fecha de la cita
     * @return total de citas del día
     */
    @Query("""
    SELECT COUNT(a)
    FROM Appointment a
    WHERE a.active = true
      AND a.appointmentDate = :appointmentDate
      AND a.status.code NOT IN ('CANCELLED', 'RELEASED')
""")
    Long countTodayAppointments(@Param("appointmentDate") LocalDate appointmentDate);

    /**
     * Consulta las citas activas del día, excluyendo citas canceladas o liberadas.
     *
     * @param appointmentDate fecha de la cita
     * @param pageable configuración de paginación
     * @return página de citas del día
     */
    @Query("""
    SELECT a
    FROM Appointment a
    WHERE a.active = true
      AND a.appointmentDate = :appointmentDate
      AND a.status.code NOT IN ('CANCELLED', 'RELEASED')
    ORDER BY a.startTime ASC
""")
    Page<Appointment> findTodayAppointments(
            @Param("appointmentDate") LocalDate appointmentDate,
            Pageable pageable
    );

    /**
     * Obtiene la cita más reciente del paciente ocurrida antes
     * de la fecha y hora actual.
     *
     * @param patientId identificador único del paciente
     * @param pageable configuración de paginación
     * @return listado de citas anteriores ordenadas de la más reciente a la más antigua
     */
    @Query("""
    SELECT a
    FROM Appointment a
    LEFT JOIN FETCH a.dentist d
    LEFT JOIN FETCH a.status s
    WHERE a.patient.id = :patientId
      AND (
            a.appointmentDate < CURRENT_DATE
            OR (
                a.appointmentDate = CURRENT_DATE
                AND a.startTime < CURRENT_TIME
            )
      )
    ORDER BY a.appointmentDate DESC, a.startTime DESC
""")
    List<Appointment> findPreviousAppointmentsByPatientId(
            @Param("patientId") Long patientId,
            Pageable pageable
    );

    /**
     * Obtiene la próxima cita del paciente a partir
     * de la fecha y hora actual.
     *
     * @param patientId identificador único del paciente
     * @param pageable configuración de paginación
     * @return listado de próximas citas ordenadas de la más cercana a la más lejana
     */
    @Query("""
    SELECT a
    FROM Appointment a
    LEFT JOIN FETCH a.dentist d
    LEFT JOIN FETCH a.status s
    WHERE a.patient.id = :patientId
      AND (
            a.appointmentDate > CURRENT_DATE
            OR (
                a.appointmentDate = CURRENT_DATE
                AND a.startTime >= CURRENT_TIME
            )
      )
    ORDER BY a.appointmentDate ASC, a.startTime ASC
""")
    List<Appointment> findNextAppointmentsByPatientId(
            @Param("patientId") Long patientId,
            Pageable pageable
    );

    /**
     * Obtiene las citas activas de un paciente
     * ordenadas de la más reciente a la más antigua.
     *
     * @param patientId ID del paciente.
     * @return lista de citas del paciente.
     */
    List<Appointment> findByPatientIdAndActiveTrueOrderByAppointmentDateDescStartTimeDesc(Long patientId);


}
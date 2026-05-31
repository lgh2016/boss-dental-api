package mx.com.bossdental.api.appointments.repository;

import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
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


}
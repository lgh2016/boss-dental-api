package mx.com.bossdental.api.appointments.repository;

import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDentistIdAndAppointmentDate(Long dentistId, LocalDate appointmentDate);

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


}
package mx.com.bossdental.api.appointments.repository;

import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.appointments.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentDate(LocalDate appointmentDate);

    List<Appointment> findByPatientId(Long patientId);

    List<Appointment> findByDentistIdAndAppointmentDate(Long dentistId, LocalDate appointmentDate);

    List<Appointment> findByStatus(AppointmentStatus status);
}
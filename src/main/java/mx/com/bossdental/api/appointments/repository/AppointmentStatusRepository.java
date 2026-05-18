package mx.com.bossdental.api.appointments.repository;

import mx.com.bossdental.api.appointments.entity.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppointmentStatusRepository extends JpaRepository<AppointmentStatus, Long> {

    Optional<AppointmentStatus> findByCodeAndActiveTrue(String code);
}
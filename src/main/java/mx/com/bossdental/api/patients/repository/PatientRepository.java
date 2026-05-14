package mx.com.bossdental.api.patients.repository;

import mx.com.bossdental.api.patients.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientNumber(String patientNumber);

    boolean existsByPatientNumber(String patientNumber);

    List<Patient> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrPhoneContaining(
            String name,
            String lastName,
            String phone
    );
}
package mx.com.bossdental.api.clinicalrecords.repository;

import mx.com.bossdental.api.clinicalrecords.entity.ClinicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicalRecordRepository
        extends JpaRepository<ClinicalRecord, Long> {

    Optional<ClinicalRecord> findByExpedientNumber(String expedientNumber);

    Optional<ClinicalRecord> findByPatientId(Long patientId);
}
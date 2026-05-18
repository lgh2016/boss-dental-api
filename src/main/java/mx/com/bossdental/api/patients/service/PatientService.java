package mx.com.bossdental.api.patients.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.clinicalrecords.entity.ClinicalRecord;
import mx.com.bossdental.api.clinicalrecords.repository.ClinicalRecordRepository;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.patients.dto.PatientCreateRequest;
import mx.com.bossdental.api.patients.dto.PatientListResponse;
import mx.com.bossdental.api.patients.dto.PatientResponse;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.patients.mapper.PatientMapper;
import mx.com.bossdental.api.patients.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final ClinicalRecordRepository clinicalRecordRepository;
    private final PatientMapper patientMapper;

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

        Patient patient = patientMapper.toEntity(request);
        patient.setActive(true);

        Patient savedPatient = patientRepository.save(patient);

        ClinicalRecord clinicalRecord = new ClinicalRecord();
        clinicalRecord.setPatient(savedPatient);
        clinicalRecord.setExpedientNumber(generateExpedientNumber());
        clinicalRecord.setActive(true);

        clinicalRecordRepository.save(clinicalRecord);

        return patientMapper.toResponse(savedPatient);
    }

    private String generateExpedientNumber() {
        int year = LocalDate.now().getYear();
        long nextNumber = clinicalRecordRepository.countByActiveTrue() + 1;
        return "EXP-" + year + "-" + String.format("%06d", nextNumber);
    }
}
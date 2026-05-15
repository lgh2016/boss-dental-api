package mx.com.bossdental.api.patients.service;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.patients.dto.PatientListResponse;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.patients.mapper.PatientMapper;
import mx.com.bossdental.api.patients.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

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
}
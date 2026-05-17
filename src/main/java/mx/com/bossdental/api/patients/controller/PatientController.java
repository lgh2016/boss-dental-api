package mx.com.bossdental.api.patients.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.common.dto.PageResponse;
import mx.com.bossdental.api.patients.dto.PatientListResponse;
import mx.com.bossdental.api.patients.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<PageResponse<PatientListResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String query
    ) {

        return ResponseEntity.ok(
                patientService.findAll(page, size, query)
        );
    }
}
package mx.com.bossdental.api.patients.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(
        Long id,
        String expedientNumber,
        String name,
        String lastName,
        String phone,
        String email,
        LocalDate birthDate,
        String address,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
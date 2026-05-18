package mx.com.bossdental.api.patients.dto;

import java.time.LocalDate;

public record CreatePatientRequest(
        String name,
        String lastName,
        String email,
        String phone,
        String gender,
        LocalDate birthDate,
        String address,
        String emergencyContactName,
        String emergencyContactPhone,
        String photoUrl
) {
}
package mx.com.bossdental.api.patients.dto.response;

import java.time.LocalDate;

public record PatientResponse(
        Long id,
        String patientNumber,
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
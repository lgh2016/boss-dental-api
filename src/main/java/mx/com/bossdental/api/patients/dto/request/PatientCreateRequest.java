package mx.com.bossdental.api.patients.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record PatientCreateRequest(

        @NotBlank(message = "El nombre del paciente es obligatorio.")
        @Size(max = 100, message = "El nombre no puede exceder 100 caracteres.")
        String name,

        @NotBlank(message = "El apellido del paciente es obligatorio.")
        @Size(max = 100, message = "El apellido no puede exceder 100 caracteres.")
        String lastName,

        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 150, message = "El correo no puede exceder 150 caracteres.")
        String email,
        
        @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres.")
        String phone,

        @Size(max = 50, message = "El género no puede exceder 50 caracteres.")
        String gender,

        LocalDate birthDate,

        @Size(max = 500, message = "La dirección no puede exceder 500 caracteres.")
        String address,

        @Size(max = 150, message = "El contacto de emergencia no puede exceder 150 caracteres.")
        String emergencyContactName,

        @Size(max = 20, message = "El teléfono de emergencia no puede exceder 20 caracteres.")
        String emergencyContactPhone,

        String photoUrl
) {
}
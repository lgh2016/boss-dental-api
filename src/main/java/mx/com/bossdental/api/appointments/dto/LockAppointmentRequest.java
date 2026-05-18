package mx.com.bossdental.api.appointments.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record LockAppointmentRequest(
        Long doctorId,
        Long branchId,
        Long patientId,
        LocalDate date,
        LocalTime startTime
) {
}
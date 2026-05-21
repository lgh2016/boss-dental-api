package mx.com.bossdental.api.appointments.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record LockAppointmentResponse(
        Long appointmentId,
        Long doctorId,
        Long branchId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        String status,
        LocalDateTime lockedUntil,
        List<LocalTime> endSlots
) {
}
package mx.com.bossdental.api.appointments.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record StartSlotsResponse(
        Long doctorId,
        Long branchId,
        LocalDate date,
        List<LocalTime> slots
) {
}
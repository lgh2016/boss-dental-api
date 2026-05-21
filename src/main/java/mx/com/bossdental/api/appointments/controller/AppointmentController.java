package mx.com.bossdental.api.appointments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.dto.request.ConfirmAppointmentRequest;
import mx.com.bossdental.api.appointments.dto.request.LockAppointmentRequest;
import mx.com.bossdental.api.appointments.dto.request.UpdateAppointmentEndTimeRequest;
import mx.com.bossdental.api.appointments.dto.response.AppointmentResponse;
import mx.com.bossdental.api.appointments.dto.response.LockAppointmentResponse;
import mx.com.bossdental.api.appointments.dto.response.StartSlotsResponse;
import mx.com.bossdental.api.appointments.service.AppointmentAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentAvailabilityService appointmentAvailabilityService;

    @GetMapping("/start-slots")
    public StartSlotsResponse getStartSlots(
            @RequestParam Long doctorId,
            @RequestParam Long branchId,
            @RequestParam LocalDate date
    ) {
        return appointmentAvailabilityService.getStartSlots(doctorId, branchId, date);
    }

    @PostMapping("/lock")
    public LockAppointmentResponse lockAppointment(
            @RequestBody LockAppointmentRequest request
    ) {
        return appointmentAvailabilityService.lockAppointment(request);
    }

    /**
     * Confirma una cita previamente bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request       Información final de confirmación.
     * @return cita confirmada.
     */
    @PutMapping("/{appointmentId}/confirm")
    public ResponseEntity<AppointmentResponse> confirmAppointment(
            @PathVariable Long appointmentId,
            @Valid @RequestBody ConfirmAppointmentRequest request
    ) {

        return ResponseEntity.ok(
                appointmentAvailabilityService.confirmAppointment(
                        appointmentId,
                        request
                )
        );
    }

    /**
     * Actualiza la hora final
     * de una cita bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request nueva hora final.
     * @return cita actualizada.
     */
    @PutMapping("/{appointmentId}/end-time")
    public ResponseEntity<LockAppointmentResponse> updateEndTime(
            @PathVariable Long appointmentId,
            @RequestBody UpdateAppointmentEndTimeRequest request
    ) {

        return ResponseEntity.ok(
                appointmentAvailabilityService.updateEndTime(
                        appointmentId,
                        request
                )
        );
    }
}
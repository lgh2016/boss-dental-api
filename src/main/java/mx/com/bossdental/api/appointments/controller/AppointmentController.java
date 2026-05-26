package mx.com.bossdental.api.appointments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.dto.request.*;
import mx.com.bossdental.api.appointments.dto.response.*;
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

    /**
     * Actualiza la hora de inicio de una cita bloqueada.
     *
     * Este flujo se usa cuando el usuario cambia la hora inicio
     * desde el formulario de agenda.
     *
     * Al cambiar la hora inicio:
     * - Se limpia la hora final actual.
     * - Se conserva el bloqueo de la cita.
     * - Se recalculan las horas fin disponibles.
     *
     * @param appointmentId ID de la cita bloqueada.
     * @param request nueva hora inicio.
     * @return cita bloqueada actualizada con horas fin disponibles.
     */
    @PutMapping("/{appointmentId}/start-time")
    public ResponseEntity<UpdateAppointmentStartTimeResponse> updateStartTime(
            @PathVariable Long appointmentId,
            @RequestBody UpdateAppointmentStartTimeRequest request
    ) {

        return ResponseEntity.ok(
                appointmentAvailabilityService.updateStartTime(
                        appointmentId,
                        request
                )
        );
    }

    /**
     * Actualiza el doctor
     * de una cita bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request nuevo doctor.
     * @return lock actualizado.
     */
    @PutMapping("/{appointmentId}/dentist")
    public ResponseEntity<UpdateAppointmentDentistResponse> updateDentist(
            @PathVariable Long appointmentId,
            @RequestBody UpdateAppointmentDentistRequest request
    ) {

        return ResponseEntity.ok(
                appointmentAvailabilityService.updateDentist(
                        appointmentId,
                        request
                )
        );
    }

    /**
     * Actualiza la fecha
     * de una cita bloqueada.
     *
     * @param appointmentId ID de la cita.
     * @param request nueva fecha.
     * @return lock actualizado.
     */
    @PutMapping("/{appointmentId}/date")
    public ResponseEntity<UpdateAppointmentDateResponse> updateDate(
            @PathVariable Long appointmentId,
            @Valid @RequestBody UpdateAppointmentDateRequest request
    ) {

        return ResponseEntity.ok(
                appointmentAvailabilityService.updateDate(
                        appointmentId,
                        request
                )
        );
    }
}
package mx.com.bossdental.api.appointments.controller;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.dto.LockAppointmentRequest;
import mx.com.bossdental.api.appointments.dto.LockAppointmentResponse;
import mx.com.bossdental.api.appointments.dto.StartSlotsResponse;
import mx.com.bossdental.api.appointments.service.AppointmentAvailabilityService;
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
}
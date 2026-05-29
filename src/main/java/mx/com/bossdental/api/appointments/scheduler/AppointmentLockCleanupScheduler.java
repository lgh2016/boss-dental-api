package mx.com.bossdental.api.appointments.scheduler;

import lombok.RequiredArgsConstructor;
import mx.com.bossdental.api.appointments.service.AppointmentAvailabilityService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler encargado de eliminar
 * locks expirados de appointments.
 */
@Component
@RequiredArgsConstructor
public class AppointmentLockCleanupScheduler {

    private final AppointmentAvailabilityService appointmentAvailabilityService;

    /**
     * Ejecuta la limpieza automática
     * de locks expirados diariamente.
     */
    @Scheduled(
            cron = "0 0 7 * * *",
            zone = "America/Mexico_City"
    )
    public void cleanupExpiredLocks() {

        appointmentAvailabilityService.cleanupExpiredLocks();
    }
}
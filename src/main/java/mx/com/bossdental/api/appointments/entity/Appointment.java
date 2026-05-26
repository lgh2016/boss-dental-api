package mx.com.bossdental.api.appointments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.branches.entity.Branch;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.users.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * appointments (citas)
 */
@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    /**
     * Paciente de la cita.
     * Puede ser null mientras la cita está LOCKED.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    /**
     * Doctor asignado a la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id", nullable = false)
    private User dentist;

    /**
     * Sucursal de la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    /**
     * Estado actual de la cita.
     * Catálogo: appointment_statuses.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private AppointmentStatus status;

    /**
     * Fecha de la cita.
     */
    @Column(nullable = false)
    private LocalDate appointmentDate;

    /**
     * Hora inicial de la cita.
     *
     * Puede ser null cuando el usuario cambia doctor
     * y aún no selecciona nueva hora inicio.
     */
    @Column
    private LocalTime startTime;

    /**
     * Hora final de la cita.
     *
     * Puede ser null cuando el usuario cambia doctor
     * o cambia hora inicio y aún no selecciona nueva hora fin.
     */
    @Column
    private LocalTime endTime;

    /**
     * Motivo principal de la cita.
     */
    @Column(length = 255)
    private String reason;

    /**
     * Notas internas.
     */
    @Column(length = 500)
    private String notes;

    /**
     * Fecha/hora límite del bloqueo temporal.
     */
    private LocalDateTime lockedUntil;

    /**
     * Usuario que bloqueó temporalmente la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locked_by_user_id")
    private User lockedByUser;

    /**
     * Usuario que creó la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdByUser;

    /**
     * Fecha/hora de confirmación.
     */
    private LocalDateTime confirmedAt;

    /**
     * Fecha/hora de cancelación.
     */
    private LocalDateTime cancelledAt;
}
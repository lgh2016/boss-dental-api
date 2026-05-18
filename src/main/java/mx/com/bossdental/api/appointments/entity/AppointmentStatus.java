package mx.com.bossdental.api.appointments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;

/**
 * Catálogo de estados para appointments (citas).
 */
@Getter
@Setter
@Entity
@Table(name = "appointment_statuses")
public class AppointmentStatus extends BaseEntity {

    /**
     * Código técnico del estado.
     * Ejemplo: LOCKED, CONFIRMED, RELEASED.
     */
    @Column(nullable = false, unique = true, length = 30)
    private String code;

    /**
     * Nombre visible del estado.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Descripción del estado.
     */
    @Column(length = 255)
    private String description;
}
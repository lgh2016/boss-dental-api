package mx.com.bossdental.api.activity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;

@Entity
@Table(name = "activity_logs")
@Getter
@Setter
public class ActivityLog extends BaseEntity {

    @Column(nullable = false)
    private String actorType;

    private Long actorUserId;

    @Column(nullable = false)
    private String actionCode;

    @Column(nullable = false)
    private String module;

    private String entityType;

    private Long entityId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    /**
     * ID del paciente relacionado con la actividad.
     */
    @Column(name = "patient_id")
    private Long patientId;
}

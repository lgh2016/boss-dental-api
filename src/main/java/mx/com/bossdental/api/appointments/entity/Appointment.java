package mx.com.bossdental.api.appointments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.appointments.enums.AppointmentStatus;
import mx.com.bossdental.api.branches.entity.Branch;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.users.entity.User;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "appointments")
public class Appointment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dentist_id")
    private User dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    @Column(nullable = false)
    private LocalDate appointmentDate;

    @Column(nullable = false)
    private LocalTime startTime;

    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AppointmentStatus status = AppointmentStatus.SCHEDULED;

    @Column(length = 255)
    private String reason;

    @Column(length = 500)
    private String notes;
}
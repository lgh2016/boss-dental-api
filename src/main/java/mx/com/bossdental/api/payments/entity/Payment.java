package mx.com.bossdental.api.payments.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.payments.enums.PaymentMethod;
import mx.com.bossdental.api.quotations.entity.Quotation;
import mx.com.bossdental.api.users.entity.User;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id")
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;
}
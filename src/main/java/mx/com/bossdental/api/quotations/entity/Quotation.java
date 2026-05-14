package mx.com.bossdental.api.quotations.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.appointments.entity.Appointment;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.patients.entity.Patient;
import mx.com.bossdental.api.quotations.enums.QuotationStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "quotations")
public class Quotation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private QuotationStatus status = QuotationStatus.DRAFT;

    private BigDecimal subtotal = BigDecimal.ZERO;

    private BigDecimal discount = BigDecimal.ZERO;

    private BigDecimal total = BigDecimal.ZERO;

    @Column(length = 500)
    private String notes;
}
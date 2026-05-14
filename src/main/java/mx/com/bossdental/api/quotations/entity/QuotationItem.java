package mx.com.bossdental.api.quotations.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.treatments.entity.Treatment;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "quotation_items")
public class QuotationItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotation_id", nullable = false)
    private Quotation quotation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id")
    private Treatment treatment;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(length = 30)
    private String tooth;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false)
    private BigDecimal unitPrice = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
}
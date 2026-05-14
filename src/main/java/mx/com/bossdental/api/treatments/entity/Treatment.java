package mx.com.bossdental.api.treatments.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "treatments")
public class Treatment extends BaseEntity {

    @Column(nullable = false, unique = true, length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    private BigDecimal defaultPrice;
}
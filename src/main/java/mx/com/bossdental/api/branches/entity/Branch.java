package mx.com.bossdental.api.branches.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;

@Getter
@Setter
@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 20)
    private String phone;
}
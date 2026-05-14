package mx.com.bossdental.api.roles.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;

@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}

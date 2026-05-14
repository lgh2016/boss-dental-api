package mx.com.bossdental.api.users.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.branches.entity.Branch;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.roles.entity.Role;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20)
    private String phone;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(length = 500)
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "professional_license", length = 100)
    private String professionalLicense;

    @Column(length = 100)
    private String specialty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
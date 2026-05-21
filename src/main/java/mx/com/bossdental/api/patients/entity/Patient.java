package mx.com.bossdental.api.patients.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.clinicalrecords.entity.ClinicalRecord;
import mx.com.bossdental.api.common.entity.BaseEntity;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient extends BaseEntity {
    
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 150)
    private String lastName;

    @Column(length = 20)
    private String phone;

    @Column(length = 150)
    private String email;

    private LocalDate birthDate;

    @Column(length = 20)
    private String gender;

    @Column(length = 255)
    private String address;

    @Column(length = 500)
    private String photoUrl;

    @Column(length = 150)
    private String emergencyContactName;

    @Column(length = 20)
    private String emergencyContactPhone;

    @OneToOne(mappedBy = "patient", fetch = FetchType.LAZY)
    private ClinicalRecord clinicalRecord;
}
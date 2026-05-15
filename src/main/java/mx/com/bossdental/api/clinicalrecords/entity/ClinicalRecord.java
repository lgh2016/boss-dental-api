package mx.com.bossdental.api.clinicalrecords.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import mx.com.bossdental.api.common.entity.BaseEntity;
import mx.com.bossdental.api.patients.entity.Patient;

@Getter
@Setter
@Entity
@Table(name = "clinical_records")
public class ClinicalRecord extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patient;

    @Column(nullable = false, unique = true, length = 30)
    private String expedientNumber;

    @Column(length = 1000)
    private String allergies;

    @Column(length = 1000)
    private String chronicDiseases;

    @Column(length = 1000)
    private String currentMedications;

    @Column(length = 2000)
    private String notes;
}
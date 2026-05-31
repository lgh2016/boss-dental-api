package mx.com.bossdental.api.patients.repository;

import mx.com.bossdental.api.patients.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    @EntityGraph(attributePaths = "clinicalRecord")
    @Query("""
    SELECT p
    FROM Patient p
    LEFT JOIN p.clinicalRecord cr
    WHERE (:query IS NULL OR :query = ''
        OR LOWER(FUNCTION('unaccent', p.name)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :query, '%')))
        OR LOWER(FUNCTION('unaccent', p.lastName)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :query, '%')))
        OR LOWER(FUNCTION('unaccent', p.phone)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :query, '%')))
        OR LOWER(FUNCTION('unaccent', cr.expedientNumber)) LIKE LOWER(FUNCTION('unaccent', CONCAT('%', :query, '%')))
    )
    ORDER BY p.id DESC
""")
    Page<Patient> findAllWithSearch(
            @Param("query") String query,
            Pageable pageable
    );

    boolean existsByEmailIgnoreCaseAndActiveTrue(String email);

    boolean existsByPhoneAndActiveTrue(String phone);

    /**
     * Obtiene el paciente con su expediente clínico para construir
     * el detalle del panel lateral.
     *
     * @param patientId identificador único del paciente
     * @return paciente encontrado con expediente clínico
     */
    @Query("""
    SELECT p
    FROM Patient p
    LEFT JOIN FETCH p.clinicalRecord cr
    WHERE p.id = :patientId
      AND p.active = true
""")
    Optional<Patient> findDetailById(@Param("patientId") Long patientId);

}
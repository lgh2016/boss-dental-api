package mx.com.bossdental.api.treatments.repository;

import mx.com.bossdental.api.treatments.entity.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    Optional<Treatment> findByNameIgnoreCase(String name);

    List<Treatment> findByNameContainingIgnoreCase(String name);
}
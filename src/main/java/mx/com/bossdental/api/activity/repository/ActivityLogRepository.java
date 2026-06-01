package mx.com.bossdental.api.activity.repository;

import mx.com.bossdental.api.activity.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository de actividad del sistema.
 */
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    /**
     * Obtiene actividades activas más recientes.
     *
     * @param pageable paginación.
     * @return actividades.
     */
    Page<ActivityLog> findByActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Obtiene actividades activas de un paciente,
     * ordenadas de más reciente a más antigua.
     *
     * @param patientId ID del paciente.
     * @param pageable paginación.
     * @return actividades del paciente.
     */
    Page<ActivityLog> findByPatientIdAndActiveTrueOrderByCreatedAtDesc(
            Long patientId,
            Pageable pageable
    );
}
package mx.com.bossdental.api.users.repository;

import mx.com.bossdental.api.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("""
    SELECT u
    FROM User u
    JOIN FETCH u.role
    WHERE u.email = :email
""")
    Optional<User> findByEmailWithRole(@Param("email") String email);

    @Query(value = """
        SELECT u.*
        FROM users u
        INNER JOIN roles r
            ON r.id = u.role_id
        WHERE u.active = true
          AND r.name = 'DENTIST'
          AND (:branchId IS NULL OR u.branch_id = :branchId)
        ORDER BY u.name, u.last_name
        """, nativeQuery = true)
    List<User> findActiveDoctorOptions(
            @Param("branchId") Long branchId
    );
}
package mx.com.bossdental.api.branches.repository;

import mx.com.bossdental.api.branches.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
}
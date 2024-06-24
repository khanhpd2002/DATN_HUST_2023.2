package vn.com.cardoctor.garage_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.com.cardoctor.garage_service.entities.ProjectConfig;

import java.util.Optional;

@Repository
public interface ProjectConfigRepository extends JpaRepository<ProjectConfig, Long>, JpaSpecificationExecutor<ProjectConfig> {
    Optional<ProjectConfig> findByCode(String code);
}

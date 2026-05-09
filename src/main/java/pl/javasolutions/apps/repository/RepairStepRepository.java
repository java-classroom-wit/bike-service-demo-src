package pl.javasolutions.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javasolutions.apps.repository.model.RepairStepEntity;

public interface RepairStepRepository extends JpaRepository<RepairStepEntity, Long> {
}


package pl.javasolutions.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javasolutions.apps.repository.model.BicycleEntity;

public interface BicycleRepository extends JpaRepository<BicycleEntity, Long> {
}


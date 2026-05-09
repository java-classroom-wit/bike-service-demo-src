package pl.javasolutions.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.javasolutions.apps.repository.model.PartEntity;

public interface PartRepository extends JpaRepository<PartEntity, Long> {
}


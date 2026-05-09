package pl.javasolutions.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.javasolutions.apps.repository.model.RepairOrderEntity;

import java.util.List;
import java.util.Optional;

public interface RepairOrderRepository extends JpaRepository<RepairOrderEntity, Long> {

    /**
     * Fetches all repair orders with bicycle and mechanic eagerly loaded via JOIN FETCH,
     * eliminating N+1 queries for ManyToOne associations.
     * Collections (repairSteps, parts) are handled via @BatchSize on the entity.
     */
    @Query("SELECT r FROM RepairOrderEntity r LEFT JOIN FETCH r.bicycle LEFT JOIN FETCH r.mechanic")
    List<RepairOrderEntity> findAllWithBicycleAndMechanic();

    /**
     * Fetches a single repair order by ID with all associations eagerly loaded.
     * Using two separate queries for collections to avoid Cartesian product.
     */
    @Query("SELECT r FROM RepairOrderEntity r LEFT JOIN FETCH r.bicycle LEFT JOIN FETCH r.mechanic WHERE r.id = :id")
    Optional<RepairOrderEntity> findByIdWithBicycleAndMechanic(@Param("id") Long id);
}


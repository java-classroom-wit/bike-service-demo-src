package pl.javasolutions.apps.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.javasolutions.apps.repository.model.MechanicEntity;

import java.util.List;
import java.util.Optional;

public interface MechanicRepository extends JpaRepository<MechanicEntity, Long> {

    /**
     * Fetches all mechanics with their repairOrders eagerly loaded via JOIN FETCH,
     * eliminating N+1 queries for the OneToMany association.
     * DISTINCT prevents duplicate mechanic entries when a mechanic has multiple repair orders.
     */
    @Query("SELECT DISTINCT m FROM MechanicEntity m LEFT JOIN FETCH m.repairOrders")
    List<MechanicEntity> findAllWithRepairOrders();

    /**
     * Fetches a single mechanic by ID with repairOrders eagerly loaded.
     */
    @Query("SELECT m FROM MechanicEntity m LEFT JOIN FETCH m.repairOrders WHERE m.id = :id")
    Optional<MechanicEntity> findByIdWithRepairOrders(@Param("id") Long id);
}


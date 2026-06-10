package pl.javasolutions.apps.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.javasolutions.apps.repository.model.PartEntity;

import java.math.BigDecimal;

public interface PartRepository extends JpaRepository<PartEntity, Long> {

    Page<PartEntity> findByManufacturer(String manufacturer, Pageable pageable);

    Page<PartEntity> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<PartEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("SELECT p FROM PartEntity p WHERE " +
           "(:manufacturer IS NULL OR p.manufacturer = :manufacturer) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<PartEntity> findWithFilters(
            @Param("manufacturer") String manufacturer,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("name") String name,
            Pageable pageable);
}


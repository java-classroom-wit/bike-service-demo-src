package pl.javasolutions.apps.service.bicycle.part;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Domain model representing a Part used in repairs.
 * Sub-domain record used within the RepairOrder context.
 */
public record Part(
        Long id,
        String name,
        String manufacturer,
        BigDecimal price
) {
    public Part {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(price, "price cannot be null");
    }
}

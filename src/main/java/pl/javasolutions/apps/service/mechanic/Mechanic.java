package pl.javasolutions.apps.service.mechanic;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a Mechanic in the service layer.
 * Immutable record that abstracts away JPA entity implementation details.
 */
public record Mechanic(
        Long id,
        String firstName,
        String lastName,
        String specialization,
        LocalDate hiredAt,
        String email,
        List<Long> repairOrderIds
) {
    public Mechanic {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(firstName, "firstName cannot be null");
        Objects.requireNonNull(lastName, "lastName cannot be null");
        Objects.requireNonNull(hiredAt, "hiredAt cannot be null");
        Objects.requireNonNull(repairOrderIds, "repairOrderIds cannot be null");
    }
}

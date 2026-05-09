package pl.javasolutions.apps.service.repair.order;

import pl.javasolutions.apps.service.bicycle.Bicycle;
import pl.javasolutions.apps.service.bicycle.part.Part;
import pl.javasolutions.apps.service.repair.step.RepairStep;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a RepairOrder - the aggregate root.
 * Immutable record that composes related domain models (Bicycle, Part, RepairStep).
 *
 * Follows the Composite pattern for aggregate root representation.
 */
public record RepairOrder(
        Long id,
        String description,
        String status,
        LocalDateTime receivedAt,
        LocalDateTime completedAt,
        BigDecimal estimatedCost,
        Bicycle bicycle,
        Long mechanicId,
        List<RepairStep> repairSteps,
        List<Part> parts
) {
    public RepairOrder {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(receivedAt, "receivedAt cannot be null");
        Objects.requireNonNull(bicycle, "bicycle cannot be null");
        Objects.requireNonNull(repairSteps, "repairSteps cannot be null");
        Objects.requireNonNull(parts, "parts cannot be null");
    }
}

package pl.javasolutions.apps.service.repair.step;

import java.util.Objects;

/**
 * Domain model representing a Repair Step.
 * Sub-domain record used within the RepairOrder context.
 */
public record RepairStep(
        Long id,
        String description,
        Integer stepOrder,
        Integer durationMinutes
) {
    public RepairStep {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(description, "description cannot be null");
        Objects.requireNonNull(stepOrder, "stepOrder cannot be null");
    }
}

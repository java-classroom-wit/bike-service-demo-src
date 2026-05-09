package pl.javasolutions.apps.service.bicycle;

import java.util.Objects;

/**
 * Domain model representing a Bicycle.
 * Sub-domain record used within the RepairOrder context.
 */
public record Bicycle(
        Long id,
        String brand,
        String model,
        String frameNumber,
        String ownerName,
        String ownerPhone
) {
    public Bicycle {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(brand, "brand cannot be null");
        Objects.requireNonNull(model, "model cannot be null");
        Objects.requireNonNull(frameNumber, "frameNumber cannot be null");
        Objects.requireNonNull(ownerName, "ownerName cannot be null");
    }
}

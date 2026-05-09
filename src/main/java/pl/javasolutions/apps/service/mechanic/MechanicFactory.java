package pl.javasolutions.apps.service.mechanic;

import pl.javasolutions.apps.repository.model.MechanicEntity;
import pl.javasolutions.apps.repository.model.RepairOrderEntity;

import java.util.List;

/**
 * Mapper for converting between MechanicEntity (persistence layer)
 * and Mechanic (domain model / service layer).
 * <p>
 * Follows the Mapper pattern to decouple domain models from JPA entities.
 */
public class MechanicFactory {

    /**
     * Converts a MechanicEntity to a Mechanic domain model.
     *
     * @param entity the MechanicEntity to convert
     * @return the Mechanic domain model
     */
    public static Mechanic create(MechanicEntity entity) {
        if (entity == null) {
            return null;
        }

        List<Long> repairOrderIds = entity.getRepairOrders() != null
                ? entity.getRepairOrders().stream()
                  .map(RepairOrderEntity::getId)
                  .toList()
                : List.of();

        return new Mechanic(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getSpecialization(),
                entity.getHiredAt(),
                entity.getEmail(),
                repairOrderIds
        );
    }
}


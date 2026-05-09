package pl.javasolutions.apps.service.repair.order;

import pl.javasolutions.apps.repository.model.RepairOrderEntity;
import pl.javasolutions.apps.service.bicycle.Bicycle;
import pl.javasolutions.apps.service.bicycle.BicycleFactory;
import pl.javasolutions.apps.service.bicycle.part.Part;
import pl.javasolutions.apps.service.bicycle.part.PartFactory;
import pl.javasolutions.apps.service.repair.step.RepairStep;
import pl.javasolutions.apps.service.repair.step.RepairStepFactory;

import java.util.List;

/**
 * Mapper for converting between RepairOrderEntity (persistence layer)
 * and RepairOrder (domain model / service layer).
 * <p>
 * Follows the Mapper pattern to decouple domain models from JPA entities.
 * Handles composition of related sub-domain models (Bicycle, Part, RepairStep).
 */
public class RepairOrderFactory {

    /**
     * Converts a RepairOrderEntity to a RepairOrder domain model (full mapping with relationships).
     *
     * @param entity the RepairOrderEntity to convert
     * @return the RepairOrder domain model
     */
    public static RepairOrder create(RepairOrderEntity entity) {
        if (entity == null) {
            return null;
        }

        Bicycle bicycle = BicycleFactory.create(entity.getBicycle());

        List<RepairStep> repairSteps = entity.getRepairSteps() != null
                ? entity.getRepairSteps().stream()
                  .map(RepairStepFactory::create)
                  .toList()
                : List.of();

        List<Part> parts = entity.getParts() != null
                ? entity.getParts().stream()
                  .map(PartFactory::create)
                  .toList()
                : List.of();

        Long mechanicId = entity.getMechanic() != null ? entity.getMechanic().getId() : null;

        return new RepairOrder(
                entity.getId(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getReceivedAt(),
                entity.getCompletedAt(),
                entity.getEstimatedCost(),
                bicycle,
                mechanicId,
                repairSteps,
                parts
        );
    }
}

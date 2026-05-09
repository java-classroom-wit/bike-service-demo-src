package pl.javasolutions.apps.service.repair.step;

import pl.javasolutions.apps.repository.model.RepairStepEntity;

public class RepairStepFactory {

    public static RepairStep create(RepairStepEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RepairStep(
                entity.getId(),
                entity.getDescription(),
                entity.getStepOrder(),
                entity.getDurationMinutes()
        );
    }
}

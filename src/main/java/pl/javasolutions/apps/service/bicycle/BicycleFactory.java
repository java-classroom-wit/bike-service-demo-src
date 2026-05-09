package pl.javasolutions.apps.service.bicycle;

import pl.javasolutions.apps.repository.model.BicycleEntity;

public class BicycleFactory {
    public static Bicycle create(BicycleEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Bicycle(
                entity.getId(),
                entity.getBrand(),
                entity.getModel(),
                entity.getFrameNumber(),
                entity.getOwnerName(),
                entity.getOwnerPhone()
        );
    }
}

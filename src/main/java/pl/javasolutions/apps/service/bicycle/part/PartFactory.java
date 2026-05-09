package pl.javasolutions.apps.service.bicycle.part;

import pl.javasolutions.apps.repository.model.PartEntity;

public class PartFactory {
    public static Part create(PartEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Part(
                entity.getId(),
                entity.getName(),
                entity.getManufacturer(),
                entity.getPrice()
        );
    }
}

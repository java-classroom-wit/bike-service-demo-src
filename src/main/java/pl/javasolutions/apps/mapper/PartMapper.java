package pl.javasolutions.apps.mapper;

import org.springframework.stereotype.Component;
import pl.javasolutions.apps.dto.PartDTO;
import pl.javasolutions.apps.dto.PartV1DTO;
import pl.javasolutions.apps.dto.PartV2DTO;
import pl.javasolutions.apps.repository.model.PartEntity;

@Component
public class PartMapper {

    public PartDTO toDTO(PartEntity entity) {
        return new PartDTO(
                entity.getName(),
                entity.getManufacturer(),
                entity.getPrice()
        );
    }

    public PartEntity toEntity(PartDTO dto) {
        PartEntity part = new PartEntity();
        part.setName(dto.getName());
        part.setManufacturer(dto.getManufacturer());
        part.setPrice(dto.getPrice());
        return part;
    }

    public void updateEntity(PartEntity part, PartDTO dto) {
        part.setName(dto.getName());
        part.setManufacturer(dto.getManufacturer());
        part.setPrice(dto.getPrice());
    }

    public void patchEntity(PartEntity part, PartDTO dto) {
        if (dto.getName() != null) part.setName(dto.getName());
        if (dto.getManufacturer() != null) part.setManufacturer(dto.getManufacturer());
        if (dto.getPrice() != null) part.setPrice(dto.getPrice());
    }

    public PartV1DTO toV1DTO(PartEntity entity) {
        return new PartV1DTO(entity.getId(), entity.getName(), entity.getPrice());
    }

    public PartV2DTO toV2DTO(PartEntity entity) {
        return new PartV2DTO(
                entity.getId(),
                entity.getName(),
                entity.getManufacturer(),
                entity.getPrice()
        );
    }
}


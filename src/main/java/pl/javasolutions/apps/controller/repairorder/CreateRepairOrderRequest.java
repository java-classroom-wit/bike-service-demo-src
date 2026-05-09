package pl.javasolutions.apps.controller.repairorder;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.service.repair.order.CreateRepairOrderCommand;

import java.math.BigDecimal;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

public record CreateRepairOrderRequest(
        String description,
        Long bicycleId,
        Long mechanicId,
        BigDecimal estimatedCost
) {

    public CreateRepairOrderCommand createRepairOrderCommand() {
        return new CreateRepairOrderCommand(
                description, bicycleId, mechanicId, estimatedCost);
    }

    public CreateRepairOrderRequest(String description, Long bicycleId, Long mechanicId, BigDecimal estimatedCost) {
        this.description = description;
        this.bicycleId = bicycleId;
        this.mechanicId = mechanicId;
        this.estimatedCost = estimatedCost;
        validate();
    }

    private void validate() {
        if (isBlank(description)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "description is required");
        }
        if (Objects.isNull(bicycleId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "bicycleId is required");
        }
    }
}


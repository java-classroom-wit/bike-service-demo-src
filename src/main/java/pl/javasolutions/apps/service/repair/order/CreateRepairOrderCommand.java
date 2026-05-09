package pl.javasolutions.apps.service.repair.order;

import java.math.BigDecimal;

public record CreateRepairOrderCommand(
        String description,
        Long bicycleId,
        Long mechanicId,
        BigDecimal estimatedCost
) {

    public RepairOrderStatus status() {
        return RepairOrderStatus.NEW;
    }
}


package pl.javasolutions.apps.controller.repairorder;

import pl.javasolutions.apps.service.repair.order.RepairOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RepairOrderResponse(
    Long id,
    String description,
    String status,
    LocalDateTime receivedAt,
    LocalDateTime completedAt,
    BigDecimal estimatedCost,
    Long bicycleId,
    Long mechanicId
) {
    public static RepairOrderResponse from(RepairOrder order) {
        Long bicycleId = order.bicycle() != null ? order.bicycle().id() : null;
        Long mechanicId = order.mechanicId();

        return new RepairOrderResponse(
            order.id(),
            order.description(),
            order.status(),
            order.receivedAt(),
            order.completedAt(),
            order.estimatedCost(),
            bicycleId,
            mechanicId
        );
    }
}


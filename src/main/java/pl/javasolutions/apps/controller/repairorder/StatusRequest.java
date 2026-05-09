package pl.javasolutions.apps.controller.repairorder;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.service.repair.order.RepairOrderStatus;

public record StatusRequest(RepairOrderStatus status) {

    public StatusRequest(String status) {
        this(getStatusAsEnum(status));
    }

    private static RepairOrderStatus getStatusAsEnum(String status) {
        // Strip surrounding quotes that may appear when body is read as raw string
        // by StringHttpMessageConverter for application/json content type
        String trimmed = status != null ? status.strip().replaceAll("^\"|\"$", "") : null;
        return RepairOrderStatus.from(
                trimmed, () -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
                });
    }
}

package pl.javasolutions.apps.service.repair.order;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Supplier;

import static org.apache.commons.lang3.EnumUtils.getEnumIgnoreCase;

public enum RepairOrderStatus {
    NEW, IN_PROGRESS, COMPLETED;

    public static RepairOrderStatus from(String status, Supplier<RepairOrderStatus> fallback) {
        return StringUtils.isEmpty(status)
                ? fallback.get()
                : getEnumIgnoreCase(RepairOrderStatus.class, status);
    }
}

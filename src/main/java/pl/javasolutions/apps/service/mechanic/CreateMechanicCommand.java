package pl.javasolutions.apps.service.mechanic;

import java.time.LocalDate;

/**
 * Command object for creating a new Mechanic.
 * Pure domain data carrier — no HTTP or infrastructure dependencies.
 * <p>
 * Follows the Command pattern as used in the RepairOrder domain.
 */
public record CreateMechanicCommand(
        String firstName,
        String lastName,
        String specialization,
        LocalDate hiredAt,
        String email
) {
}


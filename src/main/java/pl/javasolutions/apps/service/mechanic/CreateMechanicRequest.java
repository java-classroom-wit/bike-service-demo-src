package pl.javasolutions.apps.service.mechanic;

import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @deprecated Moved to controller layer as {@code pl.javasolutions.apps.controller.mechanic.CreateMechanicRequest}.
 * Use {@link CreateMechanicCommand} in the service layer instead.
 */
@Deprecated
public record CreateMechanicRequest(
        String firstName,
        String lastName,
        String specialization,
        LocalDate hiredAt,
        String email
) {

    public CreateMechanicCommand toCommand() {
        return new CreateMechanicCommand(firstName, lastName, specialization, hiredAt, email);
    }
}
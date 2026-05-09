package pl.javasolutions.apps.controller.mechanic;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.javasolutions.apps.service.mechanic.CreateMechanicCommand;

import java.time.LocalDate;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * HTTP request record for creating a new Mechanic.
 * Handles validation and maps to {@link CreateMechanicCommand} for the service layer.
 * <p>
 * Follows the same pattern as {@code CreateRepairOrderRequest} in the RepairOrder domain.
 */
public record CreateMechanicRequest(
        String firstName,
        String lastName,
        String specialization,
        LocalDate hiredAt,
        String email
) {

    public CreateMechanicRequest(String firstName, String lastName, String specialization, LocalDate hiredAt, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.hiredAt = hiredAt;
        this.email = email;
        validate();
    }

    private void validate() {
        if (isBlank(firstName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "firstName is required");
        }
        if (isBlank(lastName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "lastName is required");
        }
        if (Objects.isNull(hiredAt)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "hiredAt is required");
        }
    }

    public CreateMechanicCommand toCommand() {
        return new CreateMechanicCommand(firstName, lastName, specialization, hiredAt, email);
    }
}


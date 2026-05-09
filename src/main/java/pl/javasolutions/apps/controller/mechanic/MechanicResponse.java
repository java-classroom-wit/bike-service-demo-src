package pl.javasolutions.apps.controller.mechanic;

import pl.javasolutions.apps.service.mechanic.Mechanic;

import java.time.LocalDate;

public record MechanicResponse(
        Long id,
        String firstName,
        String lastName,
        String specialization,
        LocalDate hiredAt,
        String email
) {
    public static MechanicResponse from(Mechanic mechanic) {
        return new MechanicResponse(
                mechanic.id(),
                mechanic.firstName(),
                mechanic.lastName(),
                mechanic.specialization(),
                mechanic.hiredAt(),
                mechanic.email()
        );
    }
}
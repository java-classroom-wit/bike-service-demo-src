package pl.javasolutions.apps.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ManufacturerValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidManufacturer {
    String message() default "Nieznany producent. Dozwoleni: Shimano, SRAM, Continental, Schwalbe, Magura, Tektro";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}


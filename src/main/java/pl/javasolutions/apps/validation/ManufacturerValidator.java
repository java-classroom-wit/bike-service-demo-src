package pl.javasolutions.apps.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class ManufacturerValidator implements ConstraintValidator<ValidManufacturer, String> {

    private static final Set<String> ALLOWED_MANUFACTURERS = Set.of(
            "Shimano", "SRAM", "Continental", "Schwalbe", "Magura", "Tektro"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true; // null/blank handled by other annotations if needed
        }
        return ALLOWED_MANUFACTURERS.contains(value);
    }
}


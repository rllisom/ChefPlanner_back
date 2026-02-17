package com.salesianostriana.chefplanner.recipes.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class MinutesValidator implements ConstraintValidator<ValidMinutes, Duration> {
    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        return value != null && !value.isNegative() && !value.isZero();
    }
}

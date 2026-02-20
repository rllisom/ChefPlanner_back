package com.salesianostriana.chefplanner.menuitem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearValidator implements ConstraintValidator<ValidYear, LocalDate> {

    private int minYear;
    private int maxYear;

    @Override
    public void initialize(ValidYear annotation) {
        this.minYear = annotation.minYear();
        this.maxYear = annotation.maxYear();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int year = value.getYear();
        return year >= minYear && year <= maxYear;
    }
}
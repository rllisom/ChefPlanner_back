package com.salesianostriana.chefplanner.menuitem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class WeekRangeValidator implements ConstraintValidator<ValidWeekRange, LocalDate> {

    @Override
    public void initialize(ValidWeekRange annotation) {
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        LocalDate today = LocalDate.now();

        LocalDate startOfCurrentWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        LocalDate endOfTwoWeeksAhead = startOfCurrentWeek
                .plusWeeks(2)
                .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return !value.isBefore(startOfCurrentWeek) && !value.isAfter(endOfTwoWeeksAhead);
    }
}

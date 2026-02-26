package com.salesianostriana.chefplanner.menuitem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WeekRangeValidator.class)
@Documented
public @interface ValidWeekRange {
    String message() default "La fecha debe estar entre la semana actual y dentro de 2 semanas";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

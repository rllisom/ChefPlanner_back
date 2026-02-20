package com.salesianostriana.chefplanner.menuitem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = YearValidator.class)
@Documented
public @interface ValidYear {
    String message() default "El año debe ser válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minYear() default 2000;
    int maxYear() default 2100;
}
package com.salesianostriana.chefplanner.recipes.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinutesValidator.class)
@Documented
public @interface ValidMinutes {
    String message() default "El tiempo debe ser un valor positivo mayor a 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

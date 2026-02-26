package com.salesianostriana.chefplanner.recipes.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DifficultyValidator.class)
@Documented
public @interface ValidDifficulty {
    String message() default "La dificultad debe ser EASY, MEDIUM o HARD";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

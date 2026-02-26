package com.salesianostriana.chefplanner.ingredient.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIngredientValidator.class)
@Documented
public @interface UniqueIngredient {

    String message () default "El nombre del ingrediente ya existe";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}

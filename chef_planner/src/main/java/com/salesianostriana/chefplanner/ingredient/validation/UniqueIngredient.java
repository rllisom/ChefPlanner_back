package com.salesianostriana.chefplanner.ingredient.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueIngredientValidator.class)
@Documented
public @interface UniqueIngredient {

    String message () default "precio fuera de rango";

    Class<?>[] groups() default {};

    Class<? extends Payload> [] payload() default {};
}

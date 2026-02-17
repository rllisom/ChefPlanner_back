package com.salesianostriana.chefplanner.recipes.validation;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifficultyValidator implements ConstraintValidator<ValidDifficulty, Difficulty> {

    @Override
    public boolean isValid(Difficulty value, ConstraintValidatorContext context) {

        return value != null;
    }
}

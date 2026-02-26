package com.salesianostriana.chefplanner.ingredient.validation;

import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

public class UniqueIngredientValidator implements ConstraintValidator<UniqueIngredient,String> {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        return StringUtils.hasText(s) && !ingredientRepository.existsByNameIgnoreCase(s);
    }
}

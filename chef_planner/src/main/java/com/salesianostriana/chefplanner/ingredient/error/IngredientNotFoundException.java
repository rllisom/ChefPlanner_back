package com.salesianostriana.chefplanner.ingredient.error;

import jakarta.persistence.EntityNotFoundException;

public class IngredientNotFoundException extends EntityNotFoundException {
    public IngredientNotFoundException(String message) {
        super(message);
    }

    public IngredientNotFoundException(Long id) {
        super("No se ha encontrado el ingrediente con ID: " + id);
    }
}

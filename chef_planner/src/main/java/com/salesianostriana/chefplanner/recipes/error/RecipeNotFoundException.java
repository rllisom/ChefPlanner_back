package com.salesianostriana.chefplanner.recipes.error;

import jakarta.persistence.EntityNotFoundException;

public class RecipeNotFoundException extends EntityNotFoundException {
    public RecipeNotFoundException(String message) {
        super(message);
    }

    public RecipeNotFoundException(Long id) {
        super("Receta con ID " + id + " no encontrada.");
    }
}

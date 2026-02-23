package com.salesianostriana.chefplanner.recipes.Dto;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;

import java.time.Duration;

public record RecipeResponse(
        String title,
        Duration minutes,
        Difficulty difficulty,
        boolean featured,
        String authorName
) {

    public static RecipeResponse fromEntity(Recipe recipe,String username) {
        return new RecipeResponse(
                recipe.getTitle(),
                recipe.getMinutes(),
                recipe.getDifficulty(),
                recipe.isFeatured(),
                username
        );
    }
}

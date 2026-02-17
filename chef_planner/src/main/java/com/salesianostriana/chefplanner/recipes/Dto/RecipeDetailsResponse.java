package com.salesianostriana.chefplanner.recipes.Dto;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;

public record RecipeDetailsResponse(
        String title,
        String description,
        int minutes,
        Difficulty difficulty,
        boolean featured,
        String authorName,
        List<IngredientResponse> ingredients
){
    public static RecipeDetailsResponse fromEntity(Recipe recipe) {
        return new RecipeDetailsResponse(
                recipe.getTitle(),
                recipe.getDescription(),
                (int) recipe.getMinutes().toMinutes(),
                recipe.getDifficulty(),
                recipe.isFeatured(),
                recipe.getAuthor() != null ? recipe.getAuthor().getUsername() : "Anónimo",
                recipe.getIngredients().stream()
                        .map(IngredientResponse::fromEntity)
                        .toList()
        );
}

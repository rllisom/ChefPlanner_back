package com.salesianostriana.chefplanner.recipes.Dto;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.validation.ValidDifficulty;
import com.salesianostriana.chefplanner.recipes.validation.ValidMinutes;
import jakarta.validation.constraints.*;
import org.apache.catalina.User;

import java.time.Duration;

public record RecipeRequest (
        @NotBlank String title,
        String description,
        @ValidMinutes
        Duration minutes,
        @ValidDifficulty
        Difficulty difficulty,
        @NotNull
        boolean featured
) {
    public Recipe toEntity() {
        return Recipe.builder()
                .title(this.title)
                .description(this.description)
                .minutes(Duration.ofMinutes(this.minutes.toMinutes()))
                .difficulty(difficulty)
                .featured(false)
                .build();
    }

}

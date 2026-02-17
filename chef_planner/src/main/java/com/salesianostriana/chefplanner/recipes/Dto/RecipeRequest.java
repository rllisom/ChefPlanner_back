package com.salesianostriana.chefplanner.recipes.Dto;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.validation.ValidDifficulty;
import com.salesianostriana.chefplanner.recipes.validation.ValidMinutes;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.apache.catalina.User;

import java.time.Duration;

@Schema(description = "Objeto de transferencia para crear o actualizar una receta")
public record RecipeRequest (

        @Schema(description = "Título de la receta", example = "Pasta Carbonara", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "El título no puede estar vacío")
        String title,

        @Schema(description = "Descripción detallada del proceso de elaboración", example = "Cocer la pasta al dente...")
        String description,

        @Schema(description = "Tiempo de preparación (Formato ISO-8601, ej: PT15M para 15 minutos)", example = "PT15M", requiredMode = Schema.RequiredMode.REQUIRED)
        @ValidMinutes
        Duration minutes,

        @Schema(description = "Nivel de dificultad", example = "EASY", allowableValues = {"EASY", "MEDIUM", "HARD"})
        @ValidDifficulty
        Difficulty difficulty,

        @Schema(description = "Indica si la receta es destacada en la plataforma", example = "true")
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

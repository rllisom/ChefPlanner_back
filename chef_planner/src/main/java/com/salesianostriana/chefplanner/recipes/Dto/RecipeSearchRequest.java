package com.salesianostriana.chefplanner.recipes.Dto;

import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Criterios para el filtrado de recetas")
public record RecipeSearchRequest(
        @Parameter(description = "Nivel de dificultad", example = "EASY")
        Difficulty difficulty,

        @Parameter(description = "Tiempo máximo de preparación en minutos", example = "30")
        Integer maxMinutes
) {
}

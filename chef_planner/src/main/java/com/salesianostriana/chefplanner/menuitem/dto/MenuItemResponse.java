package com.salesianostriana.chefplanner.menuitem.dto;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record MenuItemResponse(
        @Schema(description = "ID del menuItem", example = "1")
        Long id,

        @Schema(description = "Fecha del elemento de menú", example = "2026-01-15")
        LocalDate date,

        @Schema(description = "Tipo de comida", example = "LUNCH")
        MealType mealType,

        @Schema(description = "ID de la receta asociada", example = "42")
        Long recipeId,

        @Schema(description = "Título de la receta", example = "Pasta Carbonara")
        String recipeTitle,

        @Schema(description = "ID del perfil que creó el menú", example = "3")
        Long profileId

) {
    public static MenuItemResponse of(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getDate(),
                menuItem.getMealType(),
                menuItem.getRecipe().getId(),
                menuItem.getRecipe().getTitle(),
                menuItem.getProfile().getId()
        );
    }
}
package com.salesianostriana.chefplanner.menuitem.dto;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.validation.ValidWeekRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "Datos necesarios para planificar un MenuItem")
public record MenuItemRequest(

        @Schema(description = "Fecha del elemento de menú", example = "2026-02-25")
        @NotNull(message = "La fecha es obligatoria")
        @ValidWeekRange
        LocalDate date,

        @Schema(description = "Tipo de comida", example = "LUNCH")
        @NotNull(message = "El tipo de comida es obligatorio")
        MealType mealType,

        @Schema(description = "ID de la receta asociada", example = "42")
        @NotNull(message = "La receta es obligatoria")
        Long recipeId,

        @Schema(description = "ID del perfil que crea el menú", example = "3")
        @NotNull(message = "El id del perfil es obligatorio")
        Long profile_Id

) {}

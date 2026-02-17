package com.salesianostriana.chefplanner.menuitem.dto;

import com.salesianostriana.chefplanner.menuitem.model.MealType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record MenuItemRequest(

        @NotNull(message = "La fecha es obligatoria")
        LocalDate date,

        @NotNull(message = "El tipo de comida es obligatorio")
        MealType mealType,

        @NotNull(message = "La receta es obligatoria")
        Long recipeId

) {}
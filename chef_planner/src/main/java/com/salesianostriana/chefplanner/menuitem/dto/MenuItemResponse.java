package com.salesianostriana.chefplanner.menuitem.dto;

import com.salesianostriana.chefplanner.menuitem.model.MealType;

import java.time.LocalDate;

public record MenuItemResponse(
        Long id,
        LocalDate date,
        MealType mealType,
        Long recipeId,
        String recipeTitle
) {}
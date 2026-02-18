package com.salesianostriana.chefplanner.menuitem.service;

import com.salesianostriana.chefplanner.menuitem.dto.MenuItemRequest;
import com.salesianostriana.chefplanner.menuitem.dto.MenuItemResponse;
import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.menuitem.repositorio.MenuItemRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepositorio menuItemRepositorio;
    private final RecipeRepository recipeRepository;

    public MenuItemResponse create(MenuItemRequest request) {

        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new RuntimeException("Receta no encontrada"));

        boolean existe = menuItemRepositorio
                .existsByDateAndMealType(request.date(), request.mealType());

        if (existe) {
            throw new RuntimeException("Ya hay una receta planificada para ese día y tipo de comida");
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setDate(request.date());
        menuItem.setMealType(request.mealType());
        menuItem.setRecipe(recipe);

        MenuItem guardado = menuItemRepositorio.save(menuItem);

        return toResponse(guardado);
    }

    public List<MenuItemResponse> getByRange(LocalDate startDate,
                                             LocalDate endDate) {

        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new RuntimeException("Rango de fechas no válido");
        }

        List<MenuItem> items = menuItemRepositorio
                .findByDateBetweenOrderByDateAscMealTypeAsc(startDate, endDate);

        List<MenuItemResponse> result = new ArrayList<>();
        for (MenuItem item : items) {
            result.add(toResponse(item));
        }
        return result;
    }

    public void delete(LocalDate date, MealType mealType) {

        if (date == null || mealType == null) {
            throw new RuntimeException("Datos de borrado no válidos");
        }

        MenuItem item = menuItemRepositorio
                .findByDateAndMealType(date, mealType)
                .orElseThrow(() -> new RuntimeException("No existe planificación para esa fecha y tipo"));

        menuItemRepositorio.delete(item);
    }

    private MenuItemResponse toResponse(MenuItem menuItem) {
        return new MenuItemResponse(
                menuItem.getId(),
                menuItem.getDate(),
                menuItem.getMealType(),
                menuItem.getRecipe().getId(),
                menuItem.getRecipe().getTitle()
        );
    }
}
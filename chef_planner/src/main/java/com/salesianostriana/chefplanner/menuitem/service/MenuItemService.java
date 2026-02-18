package com.salesianostriana.chefplanner.menuitem.service;

import com.salesianostriana.chefplanner.menuitem.dto.MenuItemRequest;
import com.salesianostriana.chefplanner.menuitem.dto.MenuItemResponse;
import com.salesianostriana.chefplanner.menuitem.error.MenuItemNotFoundException;
import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.menuitem.repository.MenuItemRepository;
import com.salesianostriana.chefplanner.recipes.error.RecipeNotFoundException;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.user.error.ProfileNotFoundException;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RecipeRepository recipeRepository;
    private final UserProfileRepository userProfileRepository;

    public MenuItem create(MenuItemRequest request) {

        Recipe recipe = recipeRepository.findById(request.recipeId())
                .orElseThrow(() -> new RecipeNotFoundException("Receta no encontrada"));

        UserProfile profile = userProfileRepository.findById(request.profile_Id()).orElseThrow(
                () -> new ProfileNotFoundException(request.profile_Id())
        );

        boolean existe = menuItemRepository
                .existsByDateAndMealType(request.date(), request.mealType());

        if (existe) {
            throw new IllegalArgumentException("Ya hay una receta planificada para ese día y tipo de comida");
        }

        MenuItem menuItem = MenuItem.builder()
                .date(request.date())
                .mealType(request.mealType())
                .recipe(recipe)
                .profile(profile)
                .build();

        return menuItemRepository.save(menuItem);
    }

    public List<MenuItem> getByRange(LocalDate startDate,
                                             LocalDate endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Rango de fechas no válido");
        }
        List<MenuItem> items = menuItemRepository
                .findByDateBetweenOrderByDateAscMealTypeAsc(startDate, endDate);
        return items;
    }

    public void delete(LocalDate date, MealType mealType) {

        if (date == null || mealType == null) {
            throw new RuntimeException("Datos de borrado no válidos");
        }

        MenuItem item = menuItemRepository
                .findByDateAndMealType(date, mealType)
                .orElseThrow(() -> new MenuItemNotFoundException("No existe planificación para esa fecha y tipo"));

        menuItemRepository.delete(item);
    }
}
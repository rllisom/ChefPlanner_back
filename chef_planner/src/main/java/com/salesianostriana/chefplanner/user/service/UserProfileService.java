package com.salesianostriana.chefplanner.user.service;

import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.menuitem.repository.MenuItemRepository;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.user.dto.UserListResponse;
import com.salesianostriana.chefplanner.user.error.ProfileNotFoundException;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<UserListResponse> findAll() {
        List<UserListResponse> response = userProfileRepository.findAll().stream()
                .map(profile -> {
                    User user = userRepository.findById(UUID.fromString(profile.getUserUuid()))
                            .orElseThrow(
                                    () -> new ProfileNotFoundException("No se encontró el perfil para el usuario con UUID: " + profile.getUserUuid())
                            );
                    return UserListResponse.of(profile, user);
                })
                .toList();

        if (response.isEmpty()) {
            throw new ProfileNotFoundException("No se encontraron perfiles de usuario.");
        }

        return response;
    }

    @Transactional
    public void deleteProfileById(Long id) {
        UserProfile userProfile = userProfileRepository.findById(id)
                .orElseThrow(() -> new ProfileNotFoundException("No se encontró el perfil de usuario con ID: " + id));

        User user = userRepository.findById(UUID.fromString(userProfile.getUserUuid()))
                .orElseThrow(() -> new ProfileNotFoundException("No se encontró el usuario asociado al perfil con ID: " + id));

        List<MenuItem> menus = userProfile.getMenuItems();
        if (!menus.isEmpty()) {
            menus.forEach(menu -> menu.setProfile(null));
        }
        List<Recipe> recipes = userProfile.getRecipes();
        if (!recipes.isEmpty()) {
            recipes.forEach(recipeRepository::delete);
        }
        System.out.println("Se han eliminado " + menus.size() + " menús y " + recipes.size() + " recetas asociadas al perfil con ID: " + id);


        userProfile.getMenuItems().clear();

        userProfile.getRecipes().clear();

        userProfile.getPantryIngredients().clear();

        userProfileRepository.delete(userProfile);
        userRepository.delete(user);
    }
}

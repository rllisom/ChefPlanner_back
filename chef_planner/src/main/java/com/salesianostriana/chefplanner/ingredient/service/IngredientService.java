package com.salesianostriana.chefplanner.ingredient.service;

import com.salesianostriana.chefplanner.ingredient.dto.IngredientRequest;
import com.salesianostriana.chefplanner.ingredient.error.IngredientNotFoundException;
import com.salesianostriana.chefplanner.ingredient.filter.IngredientSpec;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.service.CurrentUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;
    private final UserProfileRepository userProfileRepository;
    private final CurrentUserService currentUserService;

    public Page<Ingredient> mostrarFiltrados(String name,Pageable pageable){
        return ingredientRepository.findBy(
                PredicateSpecification.allOf(IngredientSpec.filtrarPorNombre(name)),
                q ->q.page(pageable)
        );
    }

    public Page<Ingredient> getPantryAdminIngredients(Pageable pageable) {
        Page<Ingredient> response = ingredientRepository.findAll(pageable);

        if (response.isEmpty()) {
            throw new IngredientNotFoundException("No se encontraron ingredientes en la despensa.");
        }
        return response;
    }

    public Ingredient addIngredient(IngredientRequest request){
        Ingredient ingredient = Ingredient.builder()
                .name(request.name())
                .build();
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id){
        ingredientRepository.deleteById(id);
    }


    @Transactional
    public Page<Ingredient> getPantryIngredientsOfCurrentUser(Pageable pageable) {
        String currentUserId = currentUserService.getCurrentUserIdAsString();

        UserProfile profile = userProfileRepository.findByUserUuid(currentUserId)
                .orElse(null);

        if (profile == null || profile.getPantryIngredients().isEmpty()) {
            return Page.empty(pageable);
        }

        List<Ingredient> ingredients = profile.getPantryIngredients();

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Ingredient> pagedList;

        if (ingredients.size() < startItem) {
            pagedList = List.of();
        } else {
            int toIndex = Math.min(startItem + pageSize, ingredients.size());
            pagedList = ingredients.subList(startItem, toIndex);
        }

        return new PageImpl<>(pagedList, pageable, ingredients.size());
    }


    public double cantidadIngredientes(){
        return ingredientRepository.findAll().stream().count();
    }


    @Transactional
    public void addIngredientToCurrentUserPantry(Long ingredientId) {
        String currentUserId = currentUserService.getCurrentUserIdAsString();

        UserProfile profile = userProfileRepository.findByUserUuid(currentUserId)
                .orElseThrow(() -> new IllegalStateException(
                        "UserProfile no encontrado para userUuid: " + currentUserId));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Ingrediente no encontrado con id: " + ingredientId));

        boolean alreadyInPantry = profile.getPantryIngredients().stream()
                .anyMatch(i -> i.getId().equals(ingredient.getId()));

        if (!alreadyInPantry) {
            profile.getPantryIngredients().add(ingredient);
        }
    }

    @Transactional
    public void removeIngredientFromCurrentUserPantry(Long ingredientId) {
        String currentUserId = currentUserService.getCurrentUserIdAsString();

        UserProfile profile = userProfileRepository.findByUserUuid(currentUserId)
                .orElseThrow(() -> new IllegalStateException(
                        "UserProfile no encontrado para userUuid: " + currentUserId));

        profile.getPantryIngredients()
                .removeIf(ingredient -> ingredient.getId().equals(ingredientId));
    }

    @Transactional
    public Page<Ingredient> getIngredientsNotInCurrentUserPantry(Pageable pageable) {
        String currentUserId = currentUserService.getCurrentUserIdAsString();

        UserProfile profile = userProfileRepository.findByUserUuid(currentUserId)
                .orElse(null);

        if (profile == null || profile.getPantryIngredients().isEmpty()) {
            return ingredientRepository.findAll(pageable);
        }

        return ingredientRepository.findIngredientsNotInPantry(currentUserId, pageable);
    }
}

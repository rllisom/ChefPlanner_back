package com.salesianostriana.chefplanner.ingredient.service;

import com.salesianostriana.chefplanner.ingredient.dto.IngredientRequest;
import com.salesianostriana.chefplanner.ingredient.filter.IngredientSpec;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.service.CurrentUserService;
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

    public Ingredient addIngredient(IngredientRequest request){
        Ingredient ingredient = Ingredient.builder()
                .name(request.name())
                .build();
        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id){
        ingredientRepository.deleteById(id);
    }


    public Page<Ingredient> getPantryIngredientsOfCurrentUser(Pageable pageable) {
        String currentUserId = currentUserService.getCurrentUserIdAsString();

        UserProfile profile = userProfileRepository.findByUserUuid(currentUserId)
                .orElseThrow(() -> new IllegalStateException(
                        "UserProfile no encontrado para userUuid: " + currentUserId));

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
}

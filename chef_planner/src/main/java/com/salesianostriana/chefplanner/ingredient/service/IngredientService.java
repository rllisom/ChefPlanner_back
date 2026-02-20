package com.salesianostriana.chefplanner.ingredient.service;

import com.salesianostriana.chefplanner.ingredient.dto.IngredientRequest;
import com.salesianostriana.chefplanner.ingredient.filter.IngredientSpec;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class IngredientService {

    private final IngredientRepository ingredientRepository;

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
}

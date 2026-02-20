package com.salesianostriana.chefplanner.ingredient.filter;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IngredientSpec{

    static PredicateSpecification<Ingredient> filtrarPorNombre(String name){
        return (from, criteriaBuilder) ->
                name == null? criteriaBuilder.and() :
                        criteriaBuilder.like(criteriaBuilder.lower(from.get("name")), "%"+name.toLowerCase()+"%");
    }

}

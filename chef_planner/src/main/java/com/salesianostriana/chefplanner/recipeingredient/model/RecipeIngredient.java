package com.salesianostriana.chefplanner.recipeingredient.model;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class RecipeIngredient {

    @EmbeddedId
    @Builder.Default
    private RecipeIngredientId id = new RecipeIngredientId();

    private int quantity;
    private String unit;

    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
}

package com.salesianostriana.chefplanner.recipeingredient.model;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RecipeIngredientId implements Serializable {

    private Long recipeId;
    private Long ingredientId;


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return recipeId != null && Objects.equals(recipeId, that.recipeId)
                && ingredientId != null && Objects.equals(ingredientId, that.ingredientId);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(recipeId, ingredientId);
    }
}

package com.salesianostriana.chefplanner.data;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            if (recipeRepository.count() == 0 && ingredientRepository.count() == 0) {

                System.out.println("Iniciando carga de datos de prueba...");

                Ingredient pasta = Ingredient.builder().name("Pasta Spaghetti").build();
                Ingredient tomate = Ingredient.builder().name("Salsa de Tomate").build();
                Ingredient queso = Ingredient.builder().name("Queso Parmesano").build();
                Ingredient carne = Ingredient.builder().name("Carne Picada").build();
                Ingredient huevo = Ingredient.builder().name("Huevo").build();

                ingredientRepository.saveAll(List.of(pasta, tomate, queso, carne, huevo));

                Recipe bolonesa = Recipe.builder()
                        .title("Espaguetis a la Boloñesa")
                        .description("Receta clásica italiana con salsa de carne y tomate.")
                        .minutes(Duration.ofMinutes(45))
                        .difficulty(Difficulty.MEDIUM)
                        .featured(true)
                        .build();

                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(pasta).quantity(250).unit("gramos").build());
                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(carne).quantity(300).unit("gramos").build());
                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(tomate).quantity(200).unit("ml").build());

                Recipe huevosQueso = Recipe.builder()
                        .title("Huevos Revueltos con Queso")
                        .description("Un desayuno rápido y proteico.")
                        .minutes(Duration.ofMinutes(10))
                        .difficulty(Difficulty.EASY)
                        .featured(false)
                        .build();

                huevosQueso.addIngredient(RecipeIngredient.builder()
                        .ingredient(huevo).quantity(3).unit("unidades").build());
                huevosQueso.addIngredient(RecipeIngredient.builder()
                        .ingredient(queso).quantity(50).unit("gramos").build());


                recipeRepository.save(bolonesa);
                recipeRepository.save(huevosQueso);

                System.out.println("¡Carga finalizada! Se han creado " +
                        recipeRepository.count() + " recetas y " +
                        ingredientRepository.count() + " ingredientes.");
            }
        };
    }
}
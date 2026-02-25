package com.salesianostriana.chefplanner.data;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.model.UserRole;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inyectamos el encoder para las contraseñas

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Solo cargamos si la base de datos de usuarios está vacía
            if (userRepository.count() == 0) {
                System.out.println("Cargando usuarios de prueba...");

                User admin = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@chefplanner.com")
                        .roles(Set.of(UserRole.ADMIN))
                        .build();

                User user = User.builder()
                        .username("user")
                        .password(passwordEncoder.encode("user123"))
                        .email("user@chefplanner.com")
                        .roles(Set.of(UserRole.USER))
                        .build();

                userRepository.saveAll(List.of(admin, user));
            }

            if (recipeRepository.count() == 0 && ingredientRepository.count() == 0) {
                System.out.println("Iniciando carga de recetas e ingredientes...");

                // 1. Ingredientes
                Ingredient pasta = Ingredient.builder().name("Pasta Spaghetti").build();
                Ingredient tomate = Ingredient.builder().name("Salsa de Tomate").build();
                Ingredient queso = Ingredient.builder().name("Queso Parmesano").build();
                Ingredient carne = Ingredient.builder().name("Carne Picada").build();
                Ingredient huevo = Ingredient.builder().name("Huevo").build();

                ingredientRepository.saveAll(List.of(pasta, tomate, queso, carne, huevo));

                // 2. Recetas
                Recipe bolonesa = Recipe.builder()
                        .title("Espaguetis a la Boloñesa")
                        .description("Receta clásica italiana con salsa de carne y tomate.")
                        .minutes(Duration.ofMinutes(45))
                        .difficulty(Difficulty.MEDIUM)
                        .featured(true)
                        .build();

                Recipe huevosQueso = Recipe.builder()
                        .title("Huevos Revueltos con Queso")
                        .description("Un desayuno rápido y proteico.")
                        .minutes(Duration.ofMinutes(10))
                        .difficulty(Difficulty.EASY)
                        .featured(false)
                        .build();

                // 3. Asociación
                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(pasta).quantity(250).unit("gramos").build());
                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(carne).quantity(300).unit("gramos").build());
                bolonesa.addIngredient(RecipeIngredient.builder()
                        .ingredient(tomate).quantity(200).unit("ml").build());

                huevosQueso.addIngredient(RecipeIngredient.builder()
                        .ingredient(huevo).quantity(3).unit("unidades").build());
                huevosQueso.addIngredient(RecipeIngredient.builder()
                        .ingredient(queso).quantity(50).unit("gramos").build());

                // 4. Guardar
                recipeRepository.save(bolonesa);
                recipeRepository.save(huevosQueso);

                System.out.println("¡Carga finalizada con éxito!");
            }
        };
    }
}
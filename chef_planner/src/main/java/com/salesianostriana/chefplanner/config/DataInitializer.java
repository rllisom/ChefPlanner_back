package com.salesianostriana.chefplanner.config;

import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.repository.IngredientRepository;
import com.salesianostriana.chefplanner.menuitem.model.MealType;
import com.salesianostriana.chefplanner.menuitem.model.MenuItem;
import com.salesianostriana.chefplanner.menuitem.repository.MenuItemRepository;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredientId;
import com.salesianostriana.chefplanner.recipeingredient.repository.RecipeIngredientRepository;
import com.salesianostriana.chefplanner.recipes.model.Difficulty;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.repository.RecipeRepository;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.model.UserRole;


import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final MenuItemRepository menuItemRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            log.info("Datos iniciales ya cargados");
            return;
        }

        log.info("🚀 Iniciando carga de datos iniciales...");

        // 1. USERS
        User admin = User.builder()
                .id(UUID.fromString("a1b2c3d4-0000-0000-0000-000000000001"))
                .email("admin@chefplanner.com")
                .username("admin")
                .password("{noop}prueba123")
                .roles(Set.of(UserRole.ADMIN))
                .build();
        userRepository.save(admin);

        User chefMaria = User.builder()
                .id(UUID.fromString("a1b2c3d4-0000-0000-0000-000000000002"))
                .email("chef1@chefplanner.com")
                .username("chef_maria")
                .password("{noop}contrasena123")
                .roles(Set.of(UserRole.USER))
                .build();
        userRepository.save(chefMaria);

        User chefPedro = User.builder()
                .id(UUID.fromString("a1b2c3d4-0000-0000-0000-000000000003"))
                .email("chef2@chefplanner.com")
                .username("chef_pedro")
                .password("{noop}contrasena123")
                .roles(Set.of(UserRole.USER))
                .build();
        userRepository.save(chefPedro);

        // 2. USER PROFILES
        UserProfile profileMaria = UserProfile.builder()
                .id(2L)
                .userUuid(chefMaria.getId().toString())
                .build();
        userProfileRepository.save(profileMaria);

        UserProfile profilePedro = UserProfile.builder()
                .id(3L)
                .userUuid(chefPedro.getId().toString())
                .build();
        userProfileRepository.save(profilePedro);

        // 3. INGREDIENTS (necesitas IngredientRepository y modelo)
        List<Ingredient> ingredients = List.of(
                Ingredient.builder().id(1L).name("Tomate").build(),
                Ingredient.builder().id(2L).name("Cebolla").build(),
                Ingredient.builder().id(3L).name("Ajo").build(),
                Ingredient.builder().id(4L).name("Aceite de oliva").build(),
                Ingredient.builder().id(5L).name("Sal").build(),
                Ingredient.builder().id(6L).name("Harina").build(),
                Ingredient.builder().id(7L).name("Huevo").build(),
                Ingredient.builder().id(8L).name("Leche").build(),
                Ingredient.builder().id(9L).name("Pollo").build(),
                Ingredient.builder().id(10L).name("Arroz").build()
        );
        ingredientRepository.saveAll(ingredients);

        // 4. RECIPES
        Recipe tortilla = Recipe.builder()
                .id(1L)
                .title("Tortilla Española")
                .description("Tortilla clásica de patata y cebolla")
                .minutes(Duration.ofMinutes(30))
                .difficulty(Difficulty.EASY)
                .author(profileMaria)
                .featured(true)
                .build();
        recipeRepository.save(tortilla);

        Recipe pollo = Recipe.builder()
                .id(2L)
                .title("Pollo al ajillo")
                .description("Pollo con ajo y vino blanco")
                .minutes(Duration.ofMinutes(45))
                .difficulty(Difficulty.MEDIUM)
                .author(profileMaria)
                .build();
        recipeRepository.save(pollo);

        Recipe arroz = Recipe.builder()
                .id(3L)
                .title("Arroz con tomate")
                .description("Arroz caldoso con sofrito de tomate")
                .minutes(Duration.ofMinutes(25))
                .difficulty(Difficulty.EASY)
                .author(profilePedro)
                .featured(true)
                .build();
        recipeRepository.save(arroz);

        Recipe croquetas = Recipe.builder()
                .id(4L)
                .title("Croquetas caseras")
                .description("Croquetas de pollo con bechamel")
                .minutes(Duration.ofMinutes(90))
                .difficulty(Difficulty.HARD)
                .author(profilePedro)
                .build();
        recipeRepository.save(croquetas);

        // 5. RECIPE INGREDIENTS (usando relaciones bidireccionales)
        Ingredient tomate = ingredientRepository.findById(1L).orElseThrow();
        Ingredient cebolla = ingredientRepository.findById(2L).orElseThrow();
        // ... otros ingredients

        RecipeIngredient tortillaHuevos = RecipeIngredient.builder()
                .id(new RecipeIngredientId(1L, 7L))
                .quantity(4)
                .unit("unidades")
                .recipe(tortilla)
                .ingredient(ingredientRepository.findById(7L).orElseThrow())
                .build();
        recipeIngredientRepository.save(tortillaHuevos);

        // Repite para todos los recipe_ingredient del SQL...

        // 6. PANTRY (ManyToMany en UserProfile)
        profileMaria.getPantryIngredients().addAll(List.of(
                ingredientRepository.findById(1L).orElseThrow(),
                ingredientRepository.findById(2L).orElseThrow(),
                ingredientRepository.findById(3L).orElseThrow(),
                ingredientRepository.findById(4L).orElseThrow(),
                ingredientRepository.findById(5L).orElseThrow()
        ));
        userProfileRepository.save(profileMaria);

        // 7. MENU ITEMS
        menuItemRepository.saveAll(List.of(
                MenuItem.builder()
                        .id(1L)
                        .profile(profileMaria)
                        .date(LocalDate.of(2026, 2, 24))
                        .mealType(MealType.BREAKFAST) // Ajusta enum
                        .recipe(tortilla)
                        .build()

        ));

        log.info("✅ Datos cargados: {} users, {} recipes, {} ingredients",
                userRepository.count(), recipeRepository.count(), ingredientRepository.count());
    }
}

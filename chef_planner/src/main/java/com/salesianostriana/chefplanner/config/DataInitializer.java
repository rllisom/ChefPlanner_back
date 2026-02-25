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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Log
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final IngredientRepository ingredientRepository;
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final MenuItemRepository menuItemRepository;
    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("🚀 Cargando datos de prueba...");

        // 1. INGREDIENTS
        Ingredient tomate  = ingredientRepository.save(Ingredient.builder().name("Tomate").build());
        Ingredient cebolla = ingredientRepository.save(Ingredient.builder().name("Cebolla").build());
        Ingredient ajo     = ingredientRepository.save(Ingredient.builder().name("Ajo").build());
        Ingredient aceite  = ingredientRepository.save(Ingredient.builder().name("Aceite de oliva").build());
        Ingredient sal     = ingredientRepository.save(Ingredient.builder().name("Sal").build());
        Ingredient harina  = ingredientRepository.save(Ingredient.builder().name("Harina").build());
        Ingredient huevo   = ingredientRepository.save(Ingredient.builder().name("Huevo").build());
        Ingredient leche   = ingredientRepository.save(Ingredient.builder().name("Leche").build());
        Ingredient pollo   = ingredientRepository.save(Ingredient.builder().name("Pollo").build());
        Ingredient arroz   = ingredientRepository.save(Ingredient.builder().name("Arroz").build());

        // 2. USERS (sin ID fijo, Hibernate genera el UUID)
        User admin = userRepository.save(User.builder()
                .email("admin@chefplanner.com").username("admin")
                .password(passwordEncoder.encode("prueba123"))
                .roles(Set.of(UserRole.ADMIN)).build());
        User chefMaria = userRepository.save(User.builder()
                .email("chef1@chefplanner.com").username("chef_maria")
                .password(passwordEncoder.encode("contrasena123"))
                .roles(Set.of(UserRole.USER)).build());
        User chefPedro = userRepository.save(User.builder()
                .email("chef2@chefplanner.com").username("chef_pedro")
                .password(passwordEncoder.encode("contrasena123"))
                .roles(Set.of(UserRole.USER)).build());

        // 3. USER PROFILES
        UserProfile profileAdmin = userProfileRepository.save(
                UserProfile.builder().userUuid(admin.getId().toString()).build());
        UserProfile profileMaria = userProfileRepository.save(
                UserProfile.builder().userUuid(chefMaria.getId().toString()).build());
        UserProfile profilePedro = userProfileRepository.save(
                UserProfile.builder().userUuid(chefPedro.getId().toString()).build());

        // 4. RECIPES
        Recipe tortilla = recipeRepository.save(Recipe.builder()
                .title("Tortilla Española").description("Tortilla clásica de patata y cebolla")
                .minutes(Duration.ofMinutes(30)).difficulty(Difficulty.EASY)
                .author(profileMaria).featured(true).build());
        Recipe polloAjillo = recipeRepository.save(Recipe.builder()
                .title("Pollo al ajillo").description("Pollo con ajo y vino blanco")
                .minutes(Duration.ofMinutes(45)).difficulty(Difficulty.MEDIUM)
                .author(profileMaria).featured(false).build());
        Recipe arrozTomate = recipeRepository.save(Recipe.builder()
                .title("Arroz con tomate").description("Arroz caldoso con sofrito de tomate")
                .minutes(Duration.ofMinutes(25)).difficulty(Difficulty.EASY)
                .author(profilePedro).featured(true).build());
        Recipe croquetas = recipeRepository.save(Recipe.builder()
                .title("Croquetas caseras").description("Croquetas de pollo con bechamel")
                .minutes(Duration.ofMinutes(90)).difficulty(Difficulty.HARD)
                .author(profilePedro).featured(false).build());

        // 5. RECIPE INGREDIENTS
        crearRecipeIngredient(tortilla,    huevo,  4,   "unidades");
        crearRecipeIngredient(tortilla,    sal,    1,   "cucharadita");
        crearRecipeIngredient(polloAjillo, pollo,  500, "gramos");
        crearRecipeIngredient(polloAjillo, ajo,    6,   "dientes");
        crearRecipeIngredient(polloAjillo, aceite, 3,   "cucharadas");
        crearRecipeIngredient(arrozTomate, arroz,  200, "gramos");
        crearRecipeIngredient(arrozTomate, tomate, 3,   "unidades");
        crearRecipeIngredient(arrozTomate, cebolla,1,   "unidad");
        crearRecipeIngredient(croquetas,   harina, 200, "gramos");
        crearRecipeIngredient(croquetas,   leche,  500, "ml");
        crearRecipeIngredient(croquetas,   pollo,  300, "gramos");

        // 6. PANTRY
        profileMaria.getPantryIngredients().addAll(List.of(tomate, cebolla, ajo, aceite, sal));
        profilePedro.getPantryIngredients().addAll(List.of(sal, harina, huevo, leche));
        userProfileRepository.saveAll(List.of(profileMaria, profilePedro));

        // 7. MENU ITEMS
        menuItemRepository.saveAll(List.of(
                MenuItem.builder().profile(profileMaria).date(LocalDate.of(2026, 2, 24)).mealType(MealType.BREAKFAST).recipe(tortilla).build(),
                MenuItem.builder().profile(profileMaria).date(LocalDate.of(2026, 2, 24)).mealType(MealType.LUNCH).recipe(polloAjillo).build(),
                MenuItem.builder().profile(profileMaria).date(LocalDate.of(2026, 2, 25)).mealType(MealType.DINNER).recipe(arrozTomate).build(),
                MenuItem.builder().profile(profilePedro).date(LocalDate.of(2026, 2, 24)).mealType(MealType.LUNCH).recipe(croquetas).build(),
                MenuItem.builder().profile(profilePedro).date(LocalDate.of(2026, 2, 25)).mealType(MealType.BREAKFAST).recipe(tortilla).build()
        ));

        log.info("✅ Datos cargados correctamente.");
    }

    private void crearRecipeIngredient(Recipe recipe, Ingredient ingredient, int quantity, String unit) {
        RecipeIngredient ri = RecipeIngredient.builder()
                .id(new RecipeIngredientId(recipe.getId(), ingredient.getId()))
                .quantity(quantity).unit(unit)
                .recipe(recipe).ingredient(ingredient)
                .build();
        recipeIngredientRepository.save(ri);
    }
}

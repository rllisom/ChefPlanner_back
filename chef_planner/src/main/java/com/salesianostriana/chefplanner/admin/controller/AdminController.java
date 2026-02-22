package com.salesianostriana.chefplanner.admin.controller;

import com.salesianostriana.chefplanner.admin.controller.Dto.AdminDtoResponse;
import com.salesianostriana.chefplanner.ingredient.service.IngredientService;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
@Tag(name = "Admin Dashboard", description = "Endpoints para la gestión de estadísticas administrativas")
public class AdminController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')") // Seguridad restringida a administradores
    @Operation(summary = "Obtener estadísticas globales",
            description = "Devuelve el conteo total de recetas e ingredientes, y el tiempo medio de preparación.")
    public ResponseEntity<AdminDtoResponse> getDashboardStats() {


        double avgTime = recipeService.tiempoMedioRecetas();
        double recipeCount = recipeService.cantRecetas();
        double ingredientCount = ingredientService.cantidadIngredientes();

        return ResponseEntity.ok(new AdminDtoResponse(
                avgTime,
                recipeCount,
                ingredientCount
        ));
    }
}

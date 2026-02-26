package com.salesianostriana.chefplanner.admin.controller;

import com.salesianostriana.chefplanner.admin.controller.Dto.AdminDtoResponse;
import com.salesianostriana.chefplanner.ingredient.service.IngredientService;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import com.salesianostriana.chefplanner.user.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
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
    private final CustomUserDetailsService customUserDetailsService;

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')") 
    @Operation(
            summary = "Obtener estadísticas globales",
            description = "Devuelve el conteo total de recetas e ingredientes, el tiempo medio de preparación, cantidad total de usuarios y recetas destacadas por usuario."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estadísticas obtenidas exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AdminDtoResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "tiempoMedioRecetas": 25.5,
                                                "cantidadRecetas": 45,
                                                "cantidadIngredientes": 120,
                                                "cantidadUsuarios": 8,
                                                "destacadasPorUsuario": [
                                                    {
                                                        "userId": "a1b2c3d4-0000-0000-0000-000000000001",
                                                        "username": "chef_juan",
                                                        "count": 3
                                                    },
                                                    {
                                                        "userId": "a1b2c3d4-0000-0000-0000-000000000002",
                                                        "username": "chef_maria",
                                                        "count": 2
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Acceso denegado. Se requiere rol ADMIN",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "https://example.com/forbidden",
                                                "title": "Acceso denegado",
                                                "status": 403,
                                                "detail": "No tienes permisos para acceder a este recurso"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<AdminDtoResponse> getDashboardStats() {
        return ResponseEntity.ok(new AdminDtoResponse(
                recipeService.tiempoMedioRecetas(),
                (long) recipeService.cantRecetas(),
                (long) ingredientService.cantidadIngredientes(),
                customUserDetailsService.obtenerCantidadTotalUsuarios(),
                recipeService.obtenerRecetasDestacadasPorUsuario()
        ));
    }
}

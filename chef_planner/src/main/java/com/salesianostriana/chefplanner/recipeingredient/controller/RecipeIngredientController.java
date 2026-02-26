package com.salesianostriana.chefplanner.recipeingredient.controller;

import com.salesianostriana.chefplanner.recipeingredient.dto.CompraRecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.dto.RecipeIngredientRequest;
import com.salesianostriana.chefplanner.recipeingredient.dto.RecipeIngredientResponse;
import com.salesianostriana.chefplanner.recipeingredient.model.RecipeIngredient;
import com.salesianostriana.chefplanner.recipeingredient.service.RecipeIngredientService;
import com.salesianostriana.chefplanner.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Ingredientes de una receta", description = "Peticiones CRUD para la gestión de los ingredientes de una receta")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;


    @Operation(
            summary = "Obtener la cesta de la compra de un usuario",
            description = "Devuelve la lista de ingredientes agrupados con la cantidad total necesaria para todas las recetas del usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de ingredientes obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CompraRecipeIngredient.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "ingredientName": "Tomate",
                                                    "quantityTotal": 5.0,
                                                    "unit": "kg"
                                                },
                                                {
                                                    "ingredientName": "Harina",
                                                    "quantityTotal": 200.0,
                                                    "unit": "g"
                                                },
                                                {
                                                    "ingredientName": "Huevo",
                                                    "quantityTotal": 3.0,
                                                    "unit": "ud"
                                                }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron ingredientes en las recetas del usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontraron ingredientes en tus recetas",
                                                "instance": "/api/v1/ingredient/shopping/basket"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/ingredient/shopping/basket")
    public ResponseEntity<List<CompraRecipeIngredient>> getShoppingBasket(
            @AuthenticationPrincipal User user) {
        List<RecipeIngredient> list = recipeIngredientService.mostrarIngredientesRecetas(user.getId());

        List<CompraRecipeIngredient> response = list.stream()
                .collect(Collectors.toMap(
                        ri -> ri.getIngredient().getName(),
                        ri -> new CompraRecipeIngredient(
                                ri.getIngredient().getName(),
                                ri.getQuantity(),
                                ri.getUnit()
                        ),
                        (existing, current) -> new CompraRecipeIngredient(
                                existing.ingredientName(),
                                existing.quantityTotal() + current.quantityTotal(),
                                existing.unit()
                        )
                ))
                .values().stream()
                .toList();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtiene los ingredientes de una receta",
            description = "Devuelve una lista con todos los ingredientes asociados a una receta específica mediante su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de ingredientes recuperada con éxito",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeIngredientResponse.class)) }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró la receta con el ID proporcionado",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID de receta inválido",
                    content = @Content
            )
    })
    @GetMapping("recipe/{id}/ingredients")
    public ResponseEntity<List<RecipeIngredientResponse>> obtenerRecipeIngredient(
            @PathVariable
            Long id)
    {
        return ResponseEntity.ok(recipeIngredientService.obtenerRecipeIngredients(id).stream().map(RecipeIngredientResponse::of).toList());
    }

    @Operation(summary = "Añadir ingrediente a receta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingrediente añadido correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeIngredientResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "ingredientName": "Espaguetis",
                                                "quantity": 200.0,
                                                "unit": "g",
                                                "recipeId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se ha encontrado la receta con id 4",
                                                "instance": "/api/v1/recipe/4/ingredient"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Ingrediente no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 400,
                                                "detail": "No se ha encontrado el ingrediente con id 2",
                                                "instance": "/api/v1/recipe/1/ingredient"
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping("/recipe/{recipeId}/ingredient")
    public ResponseEntity<RecipeIngredientResponse> addIngredientToRecipe(
            @Parameter(description = "ID de la receta a la que se le va a añadir el ingrediente", example = "1")
            @PathVariable
            Long recipeId,
            @RequestBody
            @Parameter(description = "Datos del ingrediente a añadir a la receta")
            RecipeIngredientRequest request) {

        return ResponseEntity.ok(RecipeIngredientResponse.of(recipeIngredientService.addIngredientToRecipe(recipeId,request)));
    }

    @Operation(summary = "Eliminar ingrediente de receta")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ingrediente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Receta no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se ha encontrado la receta con id 4",
                                                "instance": "/api/v1/recipe/1/ingredient/3"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Ingrediente no encontrado en la receta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se ha encontrado el ingrediente con id 2 en la receta",
                                                "instance": "/api/v1/recipe/1/ingredient/3"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/recipe/{recetaid}/ingredient/{ingredienteid}")
    public ResponseEntity<?> eliminarIngredienteDeReceta(
            @Parameter(description = "ID de la receta a la que pertenece el ingrediente", example = "1")
            @PathVariable
            Long recetaid,
            @Parameter(description = "ID del ingrediente de la receta", example = "1")
            @PathVariable
            Long ingredienteid) {

        recipeIngredientService.eliminarIngredienteDeReceta(recetaid, ingredienteid);
        return ResponseEntity.noContent().build();
    }


}

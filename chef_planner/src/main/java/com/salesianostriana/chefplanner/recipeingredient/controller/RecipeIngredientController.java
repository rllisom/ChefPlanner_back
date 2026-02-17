package com.salesianostriana.chefplanner.recipeingredient.controller;

import com.salesianostriana.chefplanner.recipeingredient.service.RecipeIngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
@RequestMapping("/api/v1")
@Tag(name = "Ingredientes de una recete", description = "Peticiones CRUD para la gestión de los ingredientes de una receta")
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;


    @Operation(summary = "Eliminar película")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ingrediente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Error al obtener la receta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "detail": "No se ha encontrado la receta con id 4",
                                            "instance": "/api/v1/recipe/1/ingredient/3",
                                            "status": 404,
                                            "title": "Entidad no encontrada",
                                            "type": "recipe-ingredient.com/error/no-encontrado"
                                        }
                                    """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Error al obtener el ingrediente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "detail": "No se ha encontrado el ingrediente con id 2",
                                            "instance": "/api/v1/recipe/1/ingredient/3",
                                            "status": 404,
                                            "title": "Entidad no encontrada",
                                            "type": "recipe-ingredient.com/error/no-encontrado"
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

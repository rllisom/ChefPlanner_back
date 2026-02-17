package com.salesianostriana.chefplanner.recipes.controller;

import com.salesianostriana.chefplanner.recipes.Dto.RecipeResponse;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/v1/recipes")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestión de recetas de cocina")

public class RecipeController {

    private final RecipeService service;







    @GetMapping("/buscar")
    @Operation(summary = "Buscar recetas por texto",
            description = "Filtra las recetas cuyo título o descripción coincidan con el término proporcionado.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado recetas",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeResponse.class)),
                            examples = @ExampleObject(value = """
                                    [
                                        {
                                            "id": 1,
                                            "title": "Pasta Carbonara Tradicional",
                                            "minutes": 15,
                                            "difficulty": "EASY",
                                            "featured": true,
                                            "authorName": "Juan Pérez"
                                        },
                                        {
                                            "id": 5,
                                            "title": "Ensalada César",
                                            "minutes": 10,
                                            "difficulty": "EASY",
                                            "featured": false,
                                            "authorName": "Ana García"
                                        }
                                    ]
                                    """)
                    )
            )
    })
    public List<RecipeResponse> search(
            @Parameter(description = "Término de búsqueda (título o descripción)", example = "pasta")
            @RequestParam(name = "s") String term) {

        return service.searchRecipesWithStreams(term).stream()
                .map(RecipeResponse::fromEntity)
                .toList();
    }

}

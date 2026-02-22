package com.salesianostriana.chefplanner.ingredient.controller;

import com.salesianostriana.chefplanner.ingredient.dto.IngredientRequest;
import com.salesianostriana.chefplanner.ingredient.dto.IngredientResponse;
import com.salesianostriana.chefplanner.ingredient.model.Ingredient;
import com.salesianostriana.chefplanner.ingredient.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestControllerAdvice
@RequestMapping("/api/v1")
@Tag(name = "Endpoints de ingrediente" )
public class IngredientController {

    private final IngredientService ingredientService;


    @Operation(
            summary = "Filtrar ingredientes por nombre",
            description = "Devuelve una lista paginada de ingredientes filtrados por nombre. Solo accesible para administradores."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de ingredientes obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Page.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "content": [
                                                { "id": 1, "name": "Tomate" },
                                                { "id": 2, "name": "Cebolla" }
                                            ],
                                            "totalElements": 2,
                                            "totalPages": 1,
                                            "size": 20,
                                            "number": 0
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se han encontrado ingredientes que coincidan con el filtro",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "detail": "No se han encontrado ingredientes con el nombre proporcionado",
                                            "instance": "/api/v1/ingredient/search",
                                            "status": 404,
                                            "title": "Ingredientes no encontrados",
                                            "type": "chefplanner.com/error/ingredientes-no-encontrados"
                                        }
                                        """
                            )
                    )
            ),

    })
    @GetMapping("admin/ingredient/search")
    public Page<IngredientResponse> filtrarIngredientes(@PageableDefault(page = 0,size = 20,sort = "name",direction = Sort.Direction.DESC) Pageable pageable,
                                                        @RequestParam(required = false) String name){
        return ingredientService.mostrarFiltrados(name,pageable).map(IngredientResponse::of);

    }

    @GetMapping("/ingredients")
    public Page<IngredientResponse> getMyPantryIngredients(
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {

        Page<Ingredient> page = ingredientService.getPantryIngredientsOfCurrentUser(pageable);

        return page.map(IngredientResponse::of);
    }

    @Operation(summary = "Agregar un nuevo ingrediente",
            description = "Permite a los administradores agregar un nuevo ingrediente a la base de datos.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Ingrediente agregado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = IngredientResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "id": 3,
                                            "name": "Pimiento"
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
                                            "detail": "No tienes permisos para acceder a este recurso",
                                            "instance": "/api/v1/ingredient/add",
                                            "status": 403,
                                            "title": "Acceso denegado",
                                            "type": "chefplanner.com/error/acceso-denegado"
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Solicitud inválida. El formato del cuerpo de la solicitud es incorrecto o faltan campos obligatorios.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                            "detail": "El campo 'name' es obligatorio y no puede estar vacío",
                                            "instance": "/api/v1/ingredient/add",
                                            "status": 400,
                                            "title": "Solicitud inválida",
                                            "type": "chefplanner.com/error/solicitud-invalida"
                                        }
                                        """
                            )
                    )
            )
    })
    @PostMapping("admin/ingredient/add")
    @PreAuthorize("hasRole('ADMIN')")
    public IngredientResponse addIngredient(@RequestBody IngredientRequest request) {
        return IngredientResponse.of(ingredientService.addIngredient(request));
    }

    @DeleteMapping("/ingredient/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
    }
}

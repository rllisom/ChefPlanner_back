package com.salesianostriana.chefplanner.recipes.controller;

import com.salesianostriana.chefplanner.recipes.Dto.RecipeDetailsResponse;
import com.salesianostriana.chefplanner.recipes.Dto.RecipeRequest;
import com.salesianostriana.chefplanner.recipes.Dto.RecipeResponse;
import com.salesianostriana.chefplanner.recipes.Dto.RecipeSearchRequest;
import com.salesianostriana.chefplanner.recipes.model.Recipe;
import com.salesianostriana.chefplanner.recipes.service.RecipeService;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
@Tag(name = "Recetas", description = "Gestión de recetas de cocina")

public class RecipeController {

    private final RecipeService service;
    private final CustomUserDetailsService customUserDetailsService;



    @GetMapping("recipe/buscar")
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

        return service.searchRecipesText(term).stream()
                .map(recipe -> {

                    String authorUuidStr = recipe.getAuthor().getUserUuid();

                    String authorUsername = customUserDetailsService.findById(UUID.fromString(authorUuidStr))
                            .map(User::getUsername)
                            .orElse("Usuario no encontrado");

                    return RecipeResponse.fromEntity(recipe, authorUsername); // Se lo tengo que seguir pasando porque en recipe no tengo un String nombre usuario y si no no se lo podemos pasar

                })
                .toList();
    }


    @GetMapping("recipe/{id}")
    @Operation(summary = "Obtener el detalle de una receta",
            description = "Devuelve la información completa de una receta, incluyendo su lista de ingredientes y cantidades.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Receta encontrada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDetailsResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "title": "Pasta Carbonara Tradicional",
                                        "description": "Una receta clásica italiana...",
                                        "minutes": 15,
                                        "difficulty": "MEDIUM",
                                        "featured": true,
                                        "authorName": "Juan Pérez",
                                        "ingredients": [
                                            {
                                                "name": "Espaguetis",
                                                "quantity": 200.0,
                                                "unit": "g"
                                            },
                                            {
                                                "name": "Guanciale",
                                                "quantity": 100.0,
                                                "unit": "g"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "La receta solicitada no existe",
                    content = @Content
            )
    })
    public RecipeDetailsResponse findById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {

        Recipe recipe = service.findById(id);
        String username = userDetails != null ? userDetails.getUsername() : "Anónimo";
        return RecipeDetailsResponse.fromEntity(recipe,username);
    }



    //metodo todas recetas admin
    @PreAuthorize("hasRole('ADMIN')") // Solo permite el acceso si el usuario tiene ROLE_ADMIN [Conversación previa]
    @GetMapping("/admin/recipes")
    @Operation(summary = "Endpoint para obtener todas las recetas paginadas para la vista de admin",
            description = "Devuelve una página de recetas con información básica. ")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Se han encontrado recetas",
                    content = @Content(
                            mediaType = "application/json",

                            array = @ArraySchema(schema = @Schema(implementation = RecipeResponse.class)),
                            examples = @ExampleObject(value = """
                                    {
                                        "content": [
                                            {
                                                "id": 1,
                                                "title": "Pasta Carbonara",
                                                "minutes": 15,
                                                "difficulty": "EASY",
                                                "featured": true,
                                                "authorName": "Chef Pro"
                                            }
                                        ],
                                        "page": {
                                            "size": 20,
                                            "totalElements": 1,
                                            "totalPages": 1,
                                            "number": 0
                                        }
                                    }
                                    """)
                    )
            )
    })
    public Page<RecipeResponse> findAllAdmin(
            @Parameter(description = "Configuración de paginación (page, size, sort)",
                    example = "page=0&size=10&sort=title,asc")
            Pageable pageable) {

        Page<Recipe> allRecipes = service.findAll(pageable);

        return allRecipes.map(recipe -> {
            String authorUuid = recipe.getAuthor().getUserUuid();
            String username = customUserDetailsService.findById(UUID.fromString(authorUuid))
                    .map(User::getUsername)
                    .orElse("Usuario no encontrado");

            return RecipeResponse.fromEntity(recipe, username);
        });
    }

    @PutMapping("recipe/edit/{id}")
    @Operation(summary = "Editar una receta existente",
            description = "Actualiza los campos básicos de una receta. Los cambios se sincronizan automáticamente gracias al mecanismo de Dirty Checking de Hibernate.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Receta actualizada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDetailsResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "title": "Pasta Carbonara Premium",
                                        "description": "Versión mejorada",
                                        "minutes": 20,
                                        "difficulty": "MEDIUM",
                                        "featured": false,
                                        "authorName": "Chef Pro",
                                        "ingredients": []
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos de entrada inválidos (error de validación)",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró la receta con el ID proporcionado",
                    content = @Content
            )
    })
    public RecipeDetailsResponse edit(
            @Parameter(description = "ID de la receta a editar", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Recipe recipe = dto.toEntity();
        String username = userDetails != null ? userDetails.getUsername() : "Anónimo";
        Recipe updatedRecipe = service.edit(id, recipe);
        return RecipeDetailsResponse.fromEntity(updatedRecipe,username);
    }

    @PostMapping ("recipe/crear")
    @Operation(summary = "Endpoint para crear y guardar una nueva receta",
            description = "Crea una receta a partir de los datos del cuerpo de la petición y la asocia a un autor existente. " +
                    "Devuelve el detalle completo de la receta creada.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "La receta se ha creado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDetailsResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                        "id": 1,
                                        "title": "Pasta Carbonara Tradicional",
                                        "description": "Receta clásica con guanciale y pecorino",
                                        "minutes": 15,
                                        "difficulty": "EASY",
                                        "featured": true,
                                        "authorName": "Chef Pro",
                                        "ingredients": [
                                            {
                                                "name": "Espaguetis",
                                                "quantity": 200.0,
                                                "unit": "g"
                                            }
                                        ]
                                    }
                                    """)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Los datos de la receta no son válidos",
                    content = @Content
            )
    })
    public ResponseEntity<RecipeDetailsResponse> save(
            @Valid @RequestBody RecipeRequest recipeRequest,
            @Parameter(description = "ID del autor de la receta", example = "1")
            @RequestParam Long authorId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        Recipe recipe = recipeRequest.toEntity();
        Recipe savedRecipe = service.save(recipe, authorId);
        String username = userDetails != null ? userDetails.getUsername() : "Anónimo";
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(RecipeDetailsResponse.fromEntity(savedRecipe,username));
    }


    @DeleteMapping("recipe/delete/{id}")
    @Operation(summary = "Eliminar una receta",
            description = "Elimina de forma permanente una receta y sus ingredientes asociados de la base de datos.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "La receta ha sido eliminada con éxito",
                    content = @Content 
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se pudo eliminar: la receta no existe",
                    content = @Content
            )
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID de la receta a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    @Operation(summary = "Filtrado avanzado con DTO",
            description = "Recibe un objeto con múltiples criterios de búsqueda opcionales.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultados obtenidos satisfactoriamente",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = RecipeResponse.class))
                    )
            )
    })
    public List<RecipeResponse> filter(RecipeSearchRequest search) {

        return service.buscarRecetasConDTO(search).stream()
                .map(recipe -> {
                    String authorUuidStr = recipe.getAuthor().getUserUuid();
                    String authorUsername = customUserDetailsService.findById(UUID.fromString(authorUuidStr))
                            .map(User::getUsername)
                            .orElse("Usuario no encontrado");
                    return RecipeResponse.fromEntity(recipe, authorUsername);
                })
                .toList();
    }


    @GetMapping("recipe/recetasUsuario/{id}")
    @Operation(summary = "Obtener recetas de un usuario específico",
            description = "Recupera todas las recetas asociadas al UUID de un autor. " +
                    "El nombre del autor se resuelve mediante el servicio de usuarios.")
    public Page<RecipeResponse> findByUser(
            @PathVariable UUID id,
            Pageable pageable) {

        String authorUsername = customUserDetailsService.findById(id)
                .map(User::getUsername)
                .orElse("Usuario Desconocido");
        Page<Recipe> userRecipes = service.findByAuthor(id.toString(), pageable);
        return userRecipes.map(recipe ->
                RecipeResponse.fromEntity(recipe, authorUsername));
    }


    @GetMapping("recipe/featured")
    @Operation(summary = "Obtener recetas destacadas paginadas",
            description = "Recupera una página de recetas marcadas como 'featured'. " +
                    "El nombre del autor se resuelve de forma desacoplada consultando el módulo de usuarios.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Página de recetas destacadas recuperada con éxito",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Page.class))
            )
    })
    public Page<RecipeResponse> findByFeatured(
            @Parameter(description = "Parámetros de paginación (page, size, sort)",
                    example = "page=0&size=10")
            Pageable pageable) {

        Page<Recipe> featuredRecipes = service.findByFeatured(pageable);

        return featuredRecipes.map(recipe -> {


            String authorUuidStr = recipe.getAuthor().getUserUuid();

            String authorUsername = customUserDetailsService.findById(UUID.fromString(authorUuidStr))
                    .map(User::getUsername)
                    .orElse("Usuario no encontrado");

            return RecipeResponse.fromEntity(recipe, authorUsername);
        });
    }


}

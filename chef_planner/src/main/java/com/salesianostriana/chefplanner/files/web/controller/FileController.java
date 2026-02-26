package com.salesianostriana.chefplanner.files.web.controller;

import com.salesianostriana.chefplanner.files.shared.utils.MimeTypeDetector;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import com.salesianostriana.chefplanner.recipes.Dto.RecipeDetailsResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Endpoints que maneja la subida y muestra de ficheros")
public class FileController {

    private final StorageService storageService;
    private final RecipeService recipeService;
    private final MimeTypeDetector mimeTypeDetector;
    private final CustomUserDetailsService customUserDetailsService;


    @Operation(
            summary = "Descarga un fichero subido a la aplicación",
            description = "Devuelve el contenido del fichero identificado por su nombre"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Fichero encontrado y devuelto correctamente",
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(implementation = Resource.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "El fichero no se ha encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "gestorficheros.com/error/storage-error",
                                                "title": "Error en el almacenamiento de ficheros",
                                                "status": 404,
                                                "detail": "No se ha encontrado el fichero solicitado"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/files/{id:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource resource = storageService.loadAsResource(filename);
        String mimeType = mimeTypeDetector.getMimeType(resource);

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType);

        try {
            long len = resource.contentLength();
            if (len >= 0) {
                builder.header(HttpHeaders.CONTENT_LENGTH, Long.toString(len));
            }
        } catch (Exception e) {
            throw new IOException(e.toString());
        }

        return builder.body(resource);
    }

    @PutMapping(value = "recipe/{id}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Subir o actualizar la imagen de portada de una receta",
            description = "Recibe un archivo de imagen, extrae sus bytes y los almacena en el campo LOB de la base de datos.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Imagen de portada actualizada con éxito",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RecipeDetailsResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": 1,
                                                "title": "Pasta Carbonara Tradicional",
                                                "description": "Una receta clásica italiana con guanciale y pecorino",
                                                "minutes": 15,
                                                "difficulty": "MEDIUM",
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
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Archivo no válido o vacío",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error/solicitud-invalida",
                                                "title": "Solicitud inválida",
                                                "status": 400,
                                                "detail": "El fichero proporcionado está vacío o no es válido"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receta no encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontró la receta con ID: 999"
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<RecipeDetailsResponse> uploadCover(
            @Parameter(description = "ID de la receta", example = "1")
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Recipe recipe = recipeService.findById(id);

        recipe.setCoverFileData(file.getBytes());
        recipe.setCoverFileType(file.getContentType());

        Recipe savedRecipe = recipeService.saveDirectly(recipe);

        String authorUsername = customUserDetailsService.findById(UUID.fromString(savedRecipe.getAuthor().getUserUuid()))
                .map(User::getUsername)
                .orElse("Usuario Desconocido");
        return ResponseEntity.ok(RecipeDetailsResponse.fromEntity(savedRecipe, authorUsername));
    }

    @Operation(
            summary = "Obtener la foto de portada de una receta",
            description = "Devuelve los bytes de la imagen de portada almacenada en la base de datos para la receta indicada"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Imagen de portada devuelta correctamente",
                    content = @Content(mediaType = "image/*", schema = @Schema(type = "string", format = "binary"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Receta no encontrada o sin imagen de portada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "type": "chefplanner.com/error",
                                                "title": "Entidad no encontrada",
                                                "status": 404,
                                                "detail": "No se encontró la receta con ID: 999 o no tiene imagen de portada"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/recipe/{id}/cover")
    public ResponseEntity<byte[]> getCover(
            @Parameter(description = "ID de la receta", example = "1")
            @PathVariable Long id) {
        Recipe recipe = recipeService.findById(id);

        if (recipe.getCoverFileData() == null || recipe.getCoverFileData().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = recipe.getCoverFileType() != null
                ? recipe.getCoverFileType()
                : "image/jpeg";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(recipe.getCoverFileData());
    }

}

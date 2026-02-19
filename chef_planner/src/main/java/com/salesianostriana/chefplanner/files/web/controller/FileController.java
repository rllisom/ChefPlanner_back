package com.salesianostriana.chefplanner.files.web.controller;

import com.salesianostriana.chefplanner.files.shared.utils.MimeTypeDetector;
import com.salesianostriana.chefplanner.files.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Endpoints que maneja la subida y muestra de ficheros")
public class FileController {

    private final StorageService storageService;
    private final MimeTypeDetector mimeTypeDetector;


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
                                            "detail": "No se ha encontrado el fichero",
                                            "instance": "/api/v1/files/ejemplo.txt",
                                            "status": 404,
                                            "title": "Entidad no encontrada",
                                            "type": "gestorficheros.com/error/no-encontrado"
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

        try{
            long len = resource.contentLength();
            if (len >= 0) {
                builder.header(HttpHeaders.CONTENT_LENGTH, Long.toString(len));
            }
        }catch (Exception e){
            throw new IOException(e.toString());
        }

        return builder.body(resource);
    }
}

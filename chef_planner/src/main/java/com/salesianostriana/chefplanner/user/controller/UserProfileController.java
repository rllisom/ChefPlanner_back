package com.salesianostriana.chefplanner.user.controller;

import com.salesianostriana.chefplanner.user.dto.UserListResponse;
import com.salesianostriana.chefplanner.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(
            summary = "Obtener todos los usuarios",
            description = "Devuelve una lista de todos los usuarios registrados en el sistema, incluyendo su información básica y las recetas asociadas a cada usuario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserListResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": 1,
                                                "username": "chef_maria",
                                                "email": "chef1@chefplanner.com",
                                                "uuid": "a1b2c3d4-0000-0000-0000-000000000002",
                                                "recetas": [
                                                  {
                                                    "id": 1,
                                                    "title": "Tortilla Española"
                                                  }
                                                ]
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "404", description = "No se encontraron perfiles de usuario",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://example.com/profilenotfound",
                                              "title": "Perfil no encontrado",
                                              "status": 404,
                                              "detail": "No se encontraron perfiles de usuario."
                                            }
                                            """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<UserListResponse>> getAllUsers() {
        return ResponseEntity.ok(userProfileService.findAll());
    }

    @Operation(
            summary = "Eliminar usuario por UUID",
            description = "Elimina un usuario y su perfil asociado a partir de su UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://example.com/profilenotfound",
                                              "title": "Usuario no encontrado",
                                              "status": 404,
                                              "detail": "No se encontró el usuario especificado."
                                            }
                                            """
                            )
                    )
            )
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userProfileService.deleteByUuid(uuid);
        return ResponseEntity.noContent().build();
    }
}

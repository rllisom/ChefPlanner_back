package com.salesianostriana.chefplanner.user.controller;

import com.salesianostriana.chefplanner.user.dto.UserListResponse;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import com.salesianostriana.chefplanner.user.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados en el sistema, incluyendo su información básica y las recetas asociadas a cada usuario.")
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
                                                       "title": "Tortilla Española",
                                                       "minutes": 20
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


    @DeleteMapping("/admin/delete/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un usuario por ID", description = "Elimina un usuario específico del sistema utilizando su ID. " +
            "Esta operación también eliminará el perfil de usuario asociado, las recetas, los ingredientes de la despensa y los menús relacionados con ese usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No se encontró el perfil de usuario con el ID proporcionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "type": "https://example.com/profilenotfound",
                                              "title": "Perfil no encontrado",
                                              "status": 404,
                                              "detail": "No se encontró el perfil de usuario con ID: 999"           
                                            }
                                            """
                            )
                    )
            )
    })
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "ID del usuario a eliminar", example = "1")
            @PathVariable Long id) {
        userProfileService.deleteProfileById(id);
        return ResponseEntity.noContent().build();
    }
}

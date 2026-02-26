package com.salesianostriana.chefplanner.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "DTO que representa la respuesta de una autenticación exitosa, incluyendo el token JWT y los roles del usuario.")
public record   LoginResponse(

        @Schema(description = "Nombre de usuario autenticado", example = "chefplanner_user")
        String username,

        @Schema(description = "ID único del usuario autenticado", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "Token JWT generado para el usuario autenticado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token,

        @Schema(description = "Lista de roles asignados al usuario autenticado", example = "[\"ROLE_USER\", \"ROLE_ADMIN\"]")
        List<String> roles
) {
}

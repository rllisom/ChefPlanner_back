package com.salesianostriana.chefplanner.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "DTO que representa la respuesta de una autenticación exitosa, incluyendo el token JWT y los roles del usuario.")
public record   LoginResponse(
        @Schema(name = "Nombre de usuario autenticado", example = "chefplanner_user")
        String username,
        UUID userId,
        String token,
        List<String> roles
) {
}

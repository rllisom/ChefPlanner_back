package com.salesianostriana.chefplanner.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Cuerpo del request necesario para registrar a un nuevo usuario")
public record RegisterRequest(
        @NotNull
        @Email
        @Schema(description = "Correo electrónico del usuario", example = "chefplanner@chef.com")
        String email,
        @NotNull
        @Schema(description = "Nombre de usuario", example = "chefmaster")
        String username,
        @NotNull
        @Schema(description = "Contraseña del usuario", example = "P@ssw0rd")
        String password
) {
}

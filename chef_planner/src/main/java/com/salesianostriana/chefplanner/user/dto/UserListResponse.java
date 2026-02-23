package com.salesianostriana.chefplanner.user.dto;

import com.salesianostriana.chefplanner.recipes.Dto.RecipeResponse;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.model.UserProfile;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta que contiene la información de un usuario y sus recetas")
public record UserListResponse(
        @Schema(description = "ID del usuario", example = "1")
        Long id,
        @Schema(description = "Nombre de usuario", example = "chef123")
        String username,
        @Schema(description = "Correo electrónico del usuario", example = "admin@chefplanner.com")
        String email,
        @Schema(description = "UUID del usuario", example = "550e8400-e29b-41d4-a716-446655440000")
        String uuid,
        @Schema(description = "Lista de recetas asociadas al usuario")
        List<RecipeResponse> recetas
) {

    public static UserListResponse of(UserProfile profile, User user) {
        return new UserListResponse(
                profile.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getId().toString(),
                profile.getRecipes().stream()
                        .map(recipe -> RecipeResponse.fromEntity(recipe, user.getUsername()))
                        .toList()
        );
    }
}

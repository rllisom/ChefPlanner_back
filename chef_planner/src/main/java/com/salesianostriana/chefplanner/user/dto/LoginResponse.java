package com.salesianostriana.chefplanner.user.dto;

import java.util.UUID;

public record   LoginResponse(
        String username,
        UUID userId,
        String token,
        String role
) {
}

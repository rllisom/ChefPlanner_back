package com.salesianostriana.chefplanner.user.dto;

import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String email,
        String username) {
}

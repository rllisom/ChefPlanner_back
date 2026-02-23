package com.salesianostriana.chefplanner.user.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull
        String username,
        @NotNull
        String password
) {
}

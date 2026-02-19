package com.salesianostriana.chefplanner.user.dto;

public record RegisterRequest(
        String email,
        String username,
        String password
) {
}

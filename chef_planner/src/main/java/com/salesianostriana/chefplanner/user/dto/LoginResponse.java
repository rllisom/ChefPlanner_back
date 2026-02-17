package com.salesianostriana.chefplanner.user.dto;

public record LoginResponse(
        String token,
        String refreshToken ) {
}

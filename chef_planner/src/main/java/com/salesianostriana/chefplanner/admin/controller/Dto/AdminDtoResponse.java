package com.salesianostriana.chefplanner.admin.controller.Dto;

import com.salesianostriana.chefplanner.recipes.Dto.FeaturedCountDTO;

import java.util.List;

public record AdminDtoResponse(
        double tiempoMedioRecetas,
        double cantidadRecetas,
        double cantidadIngredientes,
        long cantidadUsuarios,
        List<FeaturedCountDTO> destacadasPorUsuario
){
}

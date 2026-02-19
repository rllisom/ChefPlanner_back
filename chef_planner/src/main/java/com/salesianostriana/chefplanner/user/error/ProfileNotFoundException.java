package com.salesianostriana.chefplanner.user.error;

import jakarta.persistence.EntityNotFoundException;

public class ProfileNotFoundException extends EntityNotFoundException {
    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(Long id){
        super("Perfil con ID " + id + " no encontrado.");
    }
}

package com.salesianostriana.chefplanner.user.service;

import com.salesianostriana.chefplanner.user.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

//Para obtener el usuario actual autenticado en cualquier parte de la aplicación sin necesidad de repetir el código de autenticación
@Component
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof User)) {
            throw new IllegalStateException("No hay usuario autenticado");
        }

        return (User) auth.getPrincipal();
    }

    public String getCurrentUserIdAsString() {
        return getCurrentUser().getId().toString();
    }
}

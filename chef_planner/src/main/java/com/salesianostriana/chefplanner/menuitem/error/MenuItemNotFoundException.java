package com.salesianostriana.chefplanner.menuitem.error;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException(String message) {
        super(message);
    }

    public MenuItemNotFoundException(Long id) {
        super("MenuItem con ID " + id + " no encontrado.");
    }
}

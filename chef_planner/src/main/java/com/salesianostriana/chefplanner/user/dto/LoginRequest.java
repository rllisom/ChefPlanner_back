package com.salesianostriana.chefplanner.user.dto;

import com.salesianostriana.chefplanner.user.validation.ValidPassword;
import com.salesianostriana.chefplanner.user.validation.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotNull
        @NotBlank(message = "{login.username.notblank}")
        @Size(min = 3, max = 50, message = "{login.username.size}")
        @ValidUsername
        String username,


        @NotNull
        @NotBlank(message = "{login.password.notblank}")
        @Size(min = 8, max = 100, message = "{login.password.size}")
        @ValidPassword
        String password
) {
}

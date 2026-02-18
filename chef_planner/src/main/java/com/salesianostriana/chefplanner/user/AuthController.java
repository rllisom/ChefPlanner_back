package com.salesianostriana.chefplanner.user;

import com.salesianostriana.chefplanner.security.auth.AuthService;
import com.salesianostriana.chefplanner.user.dto.LoginRequest;
import com.salesianostriana.chefplanner.user.dto.LoginResponse;
import com.salesianostriana.chefplanner.user.dto.RegisterRequest;
import com.salesianostriana.chefplanner.user.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login de usuario",
            description = "Autentica credenciales y devuelve un token JWT para acceder al resto de la API."
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity
                .status(201)
                .body(authService.doLogin(loginRequest));
    }

    @Operation(
            summary = "Registro de usuario",
            description = "Crea un nuevo usuario en el sistema con email, username y contraseña."
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity
                .status(201)
                .body(authService.register(registerRequest));
    }
}

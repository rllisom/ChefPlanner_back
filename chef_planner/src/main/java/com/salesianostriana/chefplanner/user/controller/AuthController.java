package com.salesianostriana.chefplanner.user.controller;

import com.salesianostriana.chefplanner.user.service.AuthService;
import com.salesianostriana.chefplanner.user.dto.LoginRequest;
import com.salesianostriana.chefplanner.user.dto.LoginResponse;
import com.salesianostriana.chefplanner.user.dto.RegisterRequest;
import com.salesianostriana.chefplanner.user.dto.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints para registro y login de usuarios")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Login de usuario",
            description = "Autentica credenciales y devuelve un token JWT para acceder al resto de la API."

    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Login exitoso, token JWT generado",
            content= @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponse.class),
                    examples = @ExampleObject(
                            value = """
                    {
                        "username": "chefplanner_user",
                        "userId": "123e4567-e89b-12d3-a456-426614174000",
                        "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                        "roles": ["ROLE_USER", "ROLE_ADMIN"]
                    }
                    """
                    )

                )
            ),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos de login incorrectos")
    })
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

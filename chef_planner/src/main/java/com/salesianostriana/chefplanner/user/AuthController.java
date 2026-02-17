package com.salesianostriana.chefplanner.user;

import com.salesianostriana.chefplanner.security.auth.AuthService;
import com.salesianostriana.chefplanner.user.dto.LoginRequest;
import com.salesianostriana.chefplanner.user.dto.LoginResponse;
import com.salesianostriana.chefplanner.user.dto.RegisterRequest;
import com.salesianostriana.chefplanner.user.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> doLogin(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity
                .status(201)
                .body(authService.doLogin(loginRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest) {
        return ResponseEntity
                .status(201)
                .body(authService.register(registerRequest));
    }
}

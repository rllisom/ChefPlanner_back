package com.salesianostriana.chefplanner.user.service;

import com.salesianostriana.chefplanner.security.jwt.JwtAccessTokenService;
import com.salesianostriana.chefplanner.user.dto.LoginRequest;
import com.salesianostriana.chefplanner.user.dto.LoginResponse;
import com.salesianostriana.chefplanner.user.dto.RegisterRequest;
import com.salesianostriana.chefplanner.user.dto.RegisterResponse;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.model.UserRole;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtAccessTokenService jwtAccessTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {
        userRepository.findByUsername(request.username())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Username already in use");
                });
        userRepository.findByEmail(request.email())
                .ifPresent(u -> {
                    throw new IllegalArgumentException("Email already in use");
                });

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles(Set.of(UserRole.USER, UserRole.MANAGER)) // roles por defecto
                .build();

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getEmail(),
                saved.getUsername()
        );
    }

    public LoginResponse doLogin(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(), loginRequest.password()
                )
        );

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found: " + loginRequest.username()
                ));

        String token = jwtAccessTokenService.generateAccessToken(user);

        return new LoginResponse(
                user.getUsername(),
                token
        );
    }
}

package com.salesianostriana.chefplanner.security.jwt;

import com.salesianostriana.chefplanner.user.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Log
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtAccessTokenService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getJwtAccessTokenFromRequest(request);

        try {
            if (StringUtils.hasText(token) && jwtService.validateAccessToken(token)) {
                String userId = jwtService.getUserIdFromAccessToken(token);

                userRepository.findById(UUID.fromString(userId)).ifPresentOrElse(user -> {
                    UsernamePasswordAuthenticationToken
                            authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetails(request)
                    );

                    log.info("Usuario autenticado correctamente: " + userId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }, () -> {;
                    throw new UsernameNotFoundException("User not found with userId: " + userId);
                });

            }

            filterChain.doFilter(request, response);


        } catch (JwtException ex) {
            throw new RuntimeException(ex);
        }


    }

    private String getJwtAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAccessTokenService.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtAccessTokenService.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtAccessTokenService.TOKEN_PREFIX.length());
        }

        return null;
    }
}

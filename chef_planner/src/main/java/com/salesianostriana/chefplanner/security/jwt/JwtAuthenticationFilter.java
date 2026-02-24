package com.salesianostriana.chefplanner.security.jwt;

import com.salesianostriana.chefplanner.user.repository.UserRepository;
import io.jsonwebtoken.JwtException;

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
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetails(request));
                    log.info("Usuario autenticado correctamente: " + userId);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                }, () -> {
                    throw new UsernameNotFoundException("User not found with userId: " + userId);
                });
            }

        } catch (JwtException ex) {
            // Token inválido o malformado → limpiamos el contexto y dejamos pasar
            log.warning("JWT inválido: " + ex.getMessage());
            SecurityContextHolder.clearContext();
        } catch (Exception ex) {  // ← AÑADE ESTO
            log.warning("Error inesperado en el filtro JWT: " + ex.getMessage());
            SecurityContextHolder.clearContext();
        }

        // SIEMPRE continuamos la cadena, tanto si hay token válido como si no
        filterChain.doFilter(request, response);
    }

    private String getJwtAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(JwtAccessTokenService.TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtAccessTokenService.TOKEN_PREFIX)) {
            return bearerToken.substring(JwtAccessTokenService.TOKEN_PREFIX.length());
        }
        return null;
    }
}

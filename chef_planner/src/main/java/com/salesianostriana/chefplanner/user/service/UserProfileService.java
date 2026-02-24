package com.salesianostriana.chefplanner.user.service;

import com.salesianostriana.chefplanner.user.dto.UserListResponse;
import com.salesianostriana.chefplanner.user.error.ProfileNotFoundException;
import com.salesianostriana.chefplanner.user.model.User;
import com.salesianostriana.chefplanner.user.repository.UserProfileRepository;
import com.salesianostriana.chefplanner.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserListResponse> findAll() {
        List<UserListResponse> response = userProfileRepository.findAll().stream()
                .map(profile -> {
                    User user = userRepository.findById(UUID.fromString(profile.getUserUuid()))
                            .orElseThrow(
                                    () -> new ProfileNotFoundException("No se encontró el perfil para el usuario con UUID: " + profile.getUserUuid())
                            );
                    return UserListResponse.of(profile, user);
                })
                .toList();

        if (response.isEmpty()) {
            throw new ProfileNotFoundException("No se encontraron perfiles de usuario.");
        }

        return response;
    }
}

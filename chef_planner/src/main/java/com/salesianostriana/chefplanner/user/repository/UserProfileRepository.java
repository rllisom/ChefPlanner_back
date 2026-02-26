package com.salesianostriana.chefplanner.user.repository;

import com.salesianostriana.chefplanner.user.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile,Long> {


    Optional<UserProfile> findByUserUuid(String userUuid);
}

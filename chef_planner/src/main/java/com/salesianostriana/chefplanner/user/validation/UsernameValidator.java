package com.salesianostriana.chefplanner.user.validation;

import com.salesianostriana.chefplanner.user.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class UsernameValidator implements ConstraintValidator<ValidUsername, String> {

    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_.-]+$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(USERNAME_REGEX);
    }

}

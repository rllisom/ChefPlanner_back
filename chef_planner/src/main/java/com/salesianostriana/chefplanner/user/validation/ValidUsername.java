package com.salesianostriana.chefplanner.user.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {

    String message() default "{login.username.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

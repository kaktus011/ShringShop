package com.example.SpringShop.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = NewAndConfirmPasswordMatchesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NewAndConfirmPasswordMatches {
    String message() default "Passwords does not match";

    Class<?>[] groups() default{} ;

    Class<? extends Payload>[] payload() default {};
}

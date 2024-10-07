package com.example.SpringShop.Validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = NewAndConfirmPasswordMatchesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NewAndConfirmPasswordMatches {
    String message() default "Passwords does not match";

    Class<?>[] groups() default{} ;

    Class<? extends Payload>[] payload() default {};
}

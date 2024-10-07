package com.example.SpringShop.Validation;

import com.example.SpringShop.Dto.ChangePasswordDto;
import com.example.SpringShop.Dto.RegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegisterDto> {
    @Override
    public boolean isValid(RegisterDto registerDto, ConstraintValidatorContext context) {
        return registerDto.getPassword() != null && registerDto.getConfirmPassword() != null && registerDto.getPassword().equals(registerDto.getConfirmPassword());
    }
}

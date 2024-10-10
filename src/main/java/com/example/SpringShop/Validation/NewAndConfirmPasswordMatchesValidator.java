package com.example.SpringShop.Validation;

import com.example.SpringShop.Dto.Customer.ChangePasswordDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NewAndConfirmPasswordMatchesValidator implements ConstraintValidator<NewAndConfirmPasswordMatches, ChangePasswordDto> {

    @Override
    public void initialize(NewAndConfirmPasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(ChangePasswordDto changePasswordDto, ConstraintValidatorContext context) {
        return changePasswordDto.getNewPassword() != null && changePasswordDto.getConfirmPassword() != null && changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword());
    }
}

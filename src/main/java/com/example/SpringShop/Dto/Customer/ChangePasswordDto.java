package com.example.SpringShop.Dto.Customer;

import com.example.SpringShop.Validation.NewAndConfirmPasswordMatches;
import jakarta.validation.constraints.NotBlank;

@NewAndConfirmPasswordMatches
public class ChangePasswordDto {

    @NotBlank(message = "Old password is required.")
    private String oldPassword;
    @NotBlank(message = "New password is required.")
    private String newPassword;
    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    public ChangePasswordDto(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
    public String getOldPassword() {return oldPassword;}

    public void setOldPassword(String oldPassword) {this.oldPassword = oldPassword;}

    public String getNewPassword() {return newPassword;}

    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}

    public String getConfirmPassword() {return confirmPassword;}

    public void setConfirmPassword(String confirmPassword) {this.confirmPassword = confirmPassword;}
}

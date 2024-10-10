package com.example.SpringShop.Dto.Customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ChangeEmailDto {
    @NotBlank(message="Old email is required.")
    @Email(message = "Email should be valid.")
    private String oldEmail;
    @NotBlank(message="New email is required.")
    @Email(message = "Email should be valid.")
    private String newEmail;

    public String getOldEmail() {return oldEmail;}

    public void setOldEmail(String oldEmail) {this.oldEmail = oldEmail;}

    public String getNewEmail() {return newEmail;}

    public void setNewEmail(String newEmail) {this.newEmail = newEmail;}
}

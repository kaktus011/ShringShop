package com.example.SpringShop.Dto.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangeUsernameDto {
    @NotBlank(message = "Old username is required.")
    private String oldUsername;

    @NotBlank(message = "New username is required.")
    @Size(max = 30, message = "New username must be up to 30 characters.")
    private String newUsername;

    public ChangeUsernameDto() {}

    public ChangeUsernameDto(String oldUsername, String newUsername) {
        this.oldUsername = oldUsername;
        this.newUsername = newUsername;
    }

    public String getOldUsername() {return oldUsername;}

    public void setOldUsername(String oldUsername) {this.oldUsername = oldUsername;}

    public String getNewUsername() {return newUsername;}

    public void setNewUsername(String newUsername) {this.newUsername = newUsername;}
}

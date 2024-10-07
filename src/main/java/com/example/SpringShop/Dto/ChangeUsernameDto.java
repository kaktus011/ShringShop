package com.example.SpringShop.Dto;

import jakarta.validation.constraints.NotBlank;

public class ChangeUsernameDto {
    @NotBlank(message = "Old username is required")
    private String oldUsername;

    @NotBlank(message = "New username is required")
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

package com.example.SpringShop.Dto.Customer;

import com.example.SpringShop.Validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class RegisterDto {

    @NotBlank(message = "Mobile number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number must be exactly 10 digits.")
    private String mobileNumber;

    @NotBlank(message = "Name is required.")
    @Size(max = 70, message = "Name must be up to 70 characters.")
    private String name;

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

    @NotBlank(message = "Email is required.")
    @Email(message = "Email should be valid.")
    private String email;

    public RegisterDto() {}
    public RegisterDto(String mobileNumber, String name, String username, String password, String confirmPassword, String email) {
        this.mobileNumber = mobileNumber;
        this.name = name;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.email = email;
    }

    public String getMobileNumber() {return mobileNumber;}

    public void setMobileNumber(String mobileNumber) {this.mobileNumber = mobileNumber;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getConfirmPassword() {return confirmPassword;}

    public void setConfirmPassword(String confirmPassword) {this.confirmPassword = confirmPassword;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}
}

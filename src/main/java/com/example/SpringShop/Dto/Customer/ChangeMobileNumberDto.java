package com.example.SpringShop.Dto.Customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ChangeMobileNumberDto {
    @NotBlank(message= "Old mobile number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Old mobile number must be exactly 10 digits.")
    private String oldMobileNumber;

    @NotBlank(message= "New mobile number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Old mobile number must be exactly 10 digits.")
    private String newMobileNumber;

    public ChangeMobileNumberDto(String oldMobileNumber, String newMobileNumber) {
        this.oldMobileNumber = oldMobileNumber;
        this.newMobileNumber = newMobileNumber;
    }

    public String getOldMobileNumber() {return oldMobileNumber;}
    public void setOldMobileNumber(String oldMobileNumber) {this.oldMobileNumber = oldMobileNumber;}
    public String getNewMobileNumber() {return newMobileNumber;}
    public void setNewMobileNumber(String newMobileNumber) {this.newMobileNumber = newMobileNumber;}
}

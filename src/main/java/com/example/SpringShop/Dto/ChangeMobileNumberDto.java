package com.example.SpringShop.Dto;

import jakarta.validation.constraints.NotBlank;

public class ChangeMobileNumberDto {
    @NotBlank(message= "Old mobile number is required.")
    private String oldMobileNumber;
    @NotBlank(message= "New mobile number is required.")
    private String newMobileNumber;

    public String getOldMobileNumber() {return oldMobileNumber;}
    public void setOldMobileNumber(String oldMobileNumber) {this.oldMobileNumber = oldMobileNumber;}
    public String getNewMobileNumber() {return newMobileNumber;}
    public void setNewMobileNumber(String newMobileNumber) {this.newMobileNumber = newMobileNumber;}
}

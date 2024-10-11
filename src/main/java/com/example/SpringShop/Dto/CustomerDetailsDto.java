package com.example.SpringShop.Dto;

public class CustomerDetailsDto {
    private long id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String mobileNumber;

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getMobileNumber() {return mobileNumber;}

    public void setMobileNumber(String mobileNumber) {this.mobileNumber = mobileNumber;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}

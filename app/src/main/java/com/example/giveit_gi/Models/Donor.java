package com.example.giveit_gi.Models;

public class Donor {
    private String name,email,phoneNumber, password,profilePicture;
    public Donor(){}
    public Donor(String name, String email, String phoneNumber, String password, String profilePicture) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

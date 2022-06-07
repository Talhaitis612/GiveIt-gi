package com.example.giveit_gi.Models;


import java.util.ArrayList;

public class Donor {
    private String uid,name,email,phoneNumber, password,profilePicture;
    private ArrayList<String> donationList;
    public Donor(){}
    public Donor(String uid ,String name, String email, String phoneNumber, String password, String profilePicture, ArrayList<String> donationList) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.profilePicture = profilePicture;
        this.donationList = donationList;

        this.uid = uid;
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

    public ArrayList<String> getDonationList() {
        return donationList;
    }

    public void setDonationList(ArrayList<String> donationList) {
        this.donationList = donationList;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

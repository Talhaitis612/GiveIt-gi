package com.example.giveit_gi.Models;

public class Donation {

    private String title, description, imageURL, location, donorID;

    public Donation(String title, String description, String imageURL, String location, String donorID) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.location = location;
        this.donorID = donorID;
    }
}

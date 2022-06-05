package com.example.giveit_gi.Models;

public class Donation {

    private String title, description, category, location, imageURL, donorID;

    public Donation(String title, String description,String category, String imageURL, String location, String donorID) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageURL = imageURL;
        this.location = location;
        this.donorID = donorID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDonorID() {
        return donorID;
    }

    public void setDonorID(String donorID) {
        this.donorID = donorID;
    }
}

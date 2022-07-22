package com.example.giveit_gi.Models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Donation {

    private String donationID,title, description, category, location, imageURL, donorID;
    private Boolean isDonated;
    private Date createdAt;

    public Donation() {

    }

    public Donation(String donationID,String title, String description,String category, String imageURL, String location, String donorID, Boolean isDonated, Date createdAt) {
        this.donationID = donationID;
        this.title = title;
        this.description = description;
        this.category = category;
        this.imageURL = imageURL;
        this.location = location;
        this.donorID = donorID;
        this.isDonated = isDonated;
        this.createdAt = createdAt;
    }

    public String getDonationID() {
        return donationID;
    }

    public void setDonationID(String donationID) {
        this.donationID = donationID;
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

    public Boolean getisDonated() {
        return isDonated;
    }

    public void setisDonated(Boolean donated) {
        isDonated = donated;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

package com.example.giveit_gi.Models;

import java.util.Date;

public class RequestDonation {
    private String requestDonationID,donationID,name, email, idCardNumber, phoneNumber, location, reason;
    private boolean isSuccessful;
    private Date requestedAt;


    public RequestDonation(String requestDonationID,String donationID,String name, String email, String idCardNumber, String phoneNumber, String location, String reason, boolean isSuccessful, Date requestedAt) {
        this.requestDonationID = requestDonationID;
        this.donationID = donationID;
        this.name = name;
        this.email = email;
        this.idCardNumber = idCardNumber;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.reason = reason;
        this.isSuccessful = isSuccessful;
        this.requestedAt = requestedAt;
    }

    public String getRequestDonationID() {
        return requestDonationID;
    }

    public void setRequestDonationID(String requestDonationID) {
        this.requestDonationID = requestDonationID;
    }

    public String getDonationID() {
        return donationID;
    }

    public void setDonationID(String donationID) {
        this.donationID = donationID;
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

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }
}

package com.example.giveit_gi.Models;

import java.util.Date;

public class ApplyDonation {
    private String aplDonationID, name, email,idCardNumber, phoneNo, problem, location, amountNeeded;
    Date appliedTime;
    private Boolean isApproved;

    public ApplyDonation(String aplDonationID, String name, String email, String idCardNumber, String phoneNo, String problem, String location, String amountNeeded, Boolean isApproved, Date appliedTime) {
        this.aplDonationID = aplDonationID;
        this.name = name;
        this.email = email;
        this.idCardNumber = idCardNumber;
        this.phoneNo = phoneNo;
        this.problem = problem;
        this.location = location;
        this.amountNeeded = amountNeeded;
        this.isApproved = isApproved;
        this.appliedTime = appliedTime;
    }



    public String getAplDonationID() {
        return aplDonationID;
    }

    public void setAplDonationID(String aplDonationID) {
        this.aplDonationID = aplDonationID;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(String amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public Date getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(Date appliedTime) {
        this.appliedTime = appliedTime;
    }
}

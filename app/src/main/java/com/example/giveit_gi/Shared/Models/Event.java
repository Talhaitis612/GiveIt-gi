package com.example.giveit_gi.Shared.Models;

public class Event {
    private String eventID, eventTitle, eventDescription, eventDateTime, eventImageURL,eventLocation, donorID;
    private Boolean isEventCompleted;
    public Event(){}
    public Event(String eventID, String eventTitle, String eventDescription, String eventDateTime, String eventImageURL, String eventLocation, String donorID, Boolean isEventCompleted) {
        this.eventID = eventID;
        this.eventTitle = eventTitle;
        this.eventDescription = eventDescription;
        this.eventDateTime = eventDateTime;
        this.eventImageURL = eventImageURL;
        this.eventLocation = eventLocation;
        this.donorID = donorID;
        this.isEventCompleted = isEventCompleted;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventImageURL() {
        return eventImageURL;
    }

    public void setEventImageURL(String eventImageURL) {
        this.eventImageURL = eventImageURL;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDonorID() {
        return donorID;
    }

    public void setDonorID(String donorID) {
        this.donorID = donorID;
    }

    public Boolean getEventCompleted() {
        return isEventCompleted;
    }

    public void setEventCompleted(Boolean eventCompleted) {
        isEventCompleted = eventCompleted;
    }
}

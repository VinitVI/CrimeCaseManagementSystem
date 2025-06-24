package com.cms.dto;

import java.time.LocalDateTime;

public class ComplaintFormDTO {

    // User fields
    private String fullName;
    private String contactNumber;
    private String email;
    private String address;

    // Complaint fields 
    private String complaintType;
    private String title;
    private LocalDateTime dateTimeOfIncident;
    private String locationOfIncident;
    private String description;

    // Getters and Setters

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDateTimeOfIncident() {
        return dateTimeOfIncident;
    }

    public void setDateTimeOfIncident(LocalDateTime dateTimeOfIncident) {
        this.dateTimeOfIncident = dateTimeOfIncident;
    }

    public String getLocationOfIncident() {
        return locationOfIncident;
    }

    public void setLocationOfIncident(String locationOfIncident) {
        this.locationOfIncident = locationOfIncident;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.dev.shehzadi.startupguidance.models;

import org.joda.time.LocalDate;

import java.io.Serializable;

/**
 * Created by shehzadi on 18/3/18.
 */

public class UserModel implements Serializable, Reviewable {

    private String uid;
    private String fullName;
    private String emailId;
    private String phoneNo;
    private String gender;
    private String dateOfBirth;
    private String photoLocation;
    private boolean isSuperUser = false;
    private boolean isGivingGuidance;
    private float ratingForGuidance;

    private String feedbackType;

    public UserModel() {
    }

    public UserModel(boolean isSuperUser) {
       setSuperUser(isSuperUser);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhotoLocation() {
        return photoLocation;
    }

    public void setPhotoLocation(String photoLocation) {
        this.photoLocation = photoLocation;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
        feedbackType = isSuperUser ? TYPE_SUPER_USER : null;
    }

    public boolean isGivingGuidance() {
        return isGivingGuidance;
    }

    public void setGivingGuidance(boolean givingGuidance) throws Exception {
        if(!isSuperUser) {
            throw new Exception("NormalUser cannot give guidance, only SuperUser can.");
        }
        isGivingGuidance = givingGuidance;
    }

    public float getRatingForGuidance() {
        return ratingForGuidance;
    }

    public void setRatingForGuidance(float ratingForGuidance) throws Exception {
        if(!isSuperUser) {
            throw new Exception("NormalUser cannot give guidance, only SuperUser can.");
        }
        this.ratingForGuidance = ratingForGuidance;
    }
}

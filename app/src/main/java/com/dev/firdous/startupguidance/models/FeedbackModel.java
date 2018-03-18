package com.dev.firdous.startupguidance.models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by firdous on 18/3/18.
 */

public class FeedbackModel implements Serializable {

    private String feedbackId;
    private String givenByUid;
    private String givenToId;
    private Date givenOn;
    private float ratings;

    public FeedbackModel() {
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getGivenByUid() {
        return givenByUid;
    }

    public void setGivenByUid(String givenByUid) {
        this.givenByUid = givenByUid;
    }

    public String getGivenToId() {
        return givenToId;
    }

    public void setGivenToId(String givenToId) {
        this.givenToId = givenToId;
    }

    public Date getGivenOn() {
        return givenOn;
    }

    public void setGivenOn(Date givenOn) {
        this.givenOn = givenOn;
    }

    public float getRatings() {
        return ratings;
    }

    public void setRatings(float ratings) {
        this.ratings = ratings;
    }
}

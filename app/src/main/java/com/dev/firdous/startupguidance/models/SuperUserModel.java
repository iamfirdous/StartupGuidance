package com.dev.firdous.startupguidance.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by firdous on 18/3/18.
 */

public class SuperUserModel extends UserModel implements Serializable, Reviewable {

    private boolean isGivingGuidance;
    private float ratingForGuidance;
    private String feedbackType;

    public SuperUserModel() {
        feedbackType = TYPE_SUPER_USER;
    }

    public boolean isGivingGuidance() {
        return isGivingGuidance;
    }

    public void setGivingGuidance(boolean givingGuidance) {
        isGivingGuidance = givingGuidance;
    }

    public float getRatingForGuidance() {
        return ratingForGuidance;
    }

    public void setRatingForGuidance(float ratingForGuidance) {
        this.ratingForGuidance = ratingForGuidance;
    }

    public String getFeedbackType() {
        return feedbackType;
    }
}

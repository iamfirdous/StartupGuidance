package com.dev.shehzadi.startupguidance.models;

import java.io.Serializable;

/**
 * Created by shehzadi on 18/3/18.
 */

public interface Reviewable extends Serializable {
    String TYPE_SUPER_USER = "SuperUser";
    String TYPE_EVENT = "Event";
    String TYPE_STARTUP_STORY = "StartupStory";
}

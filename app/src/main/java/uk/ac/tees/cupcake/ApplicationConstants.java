package uk.ac.tees.cupcake;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */

public final class ApplicationConstants {

    private ApplicationConstants() {
        // prevents instantiation.
    }
    
    public static final String PREFERENCES_NAME = "PocketPreferences";
    
    public static final String STEPS_PREFERENCE_KEY = "steps";
    
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.UK);
    
    public static final String STEP_COUNT_BROADCAST_INTENT_ACTION = "uk.ac.tees.cupcake.home.steps.StepCountUpdate";
    
    public static final int STEP_COUNT_RESET_JOB_ID = 1;
    
    public static final int STEP_COUNT_RESET_STARTER_JOB_ID = 2;
    
    public static final int STEP_COUNTING_EVENT_START_THRESHOLD = 5;
    
    public static final int CREATE_POST_ADD_IMAGE_REQUEST_CODE = 1;
    
    public static final int CREATE_POST_ADD_LOCATION_REQUEST_CODE = 2;
    
}

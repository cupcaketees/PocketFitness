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
    
    public static final String BROADCAST_INTENT_ACTION = "uk.ac.tees.cupcake.home.steps.StepCountUpdate";
    
}

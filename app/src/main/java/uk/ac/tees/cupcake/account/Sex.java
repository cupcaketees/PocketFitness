package uk.ac.tees.cupcake.account;

import android.support.annotation.IdRes;

import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public enum Sex {
    
    MALE, FEMALE;
    
    /**
     * Gets an instance of {@link Sex} for the given resource Id.
     *
     * @param resourceId the resource id associated with the desired {@link Sex}
     * @return {@link #MALE} or {@link #FEMALE}
     */
    public static Sex get(@IdRes int resourceId) {
        switch (resourceId) {
            case R.id.health_stats_sex_male:
                return MALE;
            
            case R.id.health_stats_sex_female:
                return FEMALE;
            
            default:
                throw new NullPointerException();
        }
    }
}
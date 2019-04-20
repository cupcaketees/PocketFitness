package uk.ac.tees.cupcake.home.health;

import android.support.annotation.IdRes;

import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public enum Sex {
    
    MALE(176.8f, 83.6f, .415f),
    
    FEMALE(163.7f, 70.2f, .413f);
    
    private final float averageHeight;
    
    private final float averageWeight;
    
    private final float strideMultiplier;
    
    Sex(float averageHeight, float averageWeight, float strideMultiplier) {
        this.averageHeight = averageHeight;
        this.averageWeight = averageWeight;
        this.strideMultiplier = strideMultiplier;
    }
    
    public float getAverageHeight() {
        return averageHeight;
    }
    
    public float getAverageWeight() {
        return averageWeight;
    }
    
    public float getStrideMultiplier() {
        return strideMultiplier;
    }
    
    /**
     * Gets an instance of {@link Sex} for the given resource Id.
     *
     * @param resourceName the resource id associated with the desired {@link Sex}
     * @return {@link #MALE} or {@link #FEMALE}
     */
    public static Sex get(String resourceName) {
        switch (resourceName) {
            case "health_stats_sex_male":
                return MALE;
            
            case "health_stats_sex_female":
                return FEMALE;
            
            default:
                throw new NullPointerException();
        }
    }
}
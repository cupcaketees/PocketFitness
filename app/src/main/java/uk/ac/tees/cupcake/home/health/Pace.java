package uk.ac.tees.cupcake.home.health;

public enum Pace {
    
    WALK_VERY_SLOW(0.89408f, 2.8f),
    
    WALK_LEISURELY_PACE(1.251712f, 3.0f),
    
    WALK_MODERATE_PACE(1.430528f, 3.5f),
    
    WALK_MODERATELY_BRISK(1.56464f, 4.3f),
    
    WALK_BRISK(1.78816f, 5.0f),
    
    WALK_VERY_BRISK(2.01168f, 7.0f),
    
    WALK_FAST(2.2352f, 8.3f),
    
    RUNNING_6MPH(2.32461f, 9.8f),
    
    RUNNING_7MPH(3.12928f, 11.0f),
    
    RUNNING_8MPH(3.57632f, 11.8f),
    
    RUNNING_9MPH(4.02336f, 12.8f),
    
    RUNNING_10MPH(4.4704f, 14.5f),
    
    RUNNING_11MPH(4.91744f, 16.0f),
    
    RUNNING_12MPH(5.36448f, 19.0f),
    
    RUNNING_13MPH(5.81152f, 19.8f),
    
    RUNNING_14MPH(6.25856f, 23.0f);
    
    /**
     * The upper bound, speed in m/s, of the bracket, inclusive.
     */
    private final float upperBound;
    
    /**
     * The ratio of the work metabolic rate to the resting metabolic rate.
     */
    private final float metabolicEquivalent;
    
    Pace(float upperBound, float metabolicEquivalent) {
        this.upperBound = upperBound;
        this.metabolicEquivalent = metabolicEquivalent;
    }
    
    public float getMetabolicEquivalent() {
        return metabolicEquivalent;
    }
    
    /**
     * Gets the appropriate walking speed for the given speed.
     *
     * @param speed the speed to get the bracket for.
     * @return the right {@link Pace}
     */
    public static Pace getBracket(float speed) {
        for (Pace bracket : values()) {
            if (speed <= bracket.upperBound) {
                return bracket;
            }
        }
        
        return RUNNING_14MPH;
    }
    
}
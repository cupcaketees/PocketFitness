package uk.ac.tees.cupcake.home.health;

public enum MetsWalkingBracket {
    
    VERY_SLOW(0.89408f, 2.8f),
    
    LEISURELY_PACE(1.251712f, 3.0f),
    
    MODERATE_PACE(1.430528f, 3.5f),
    
    MODERATELY_BRISK(1.56464f, 4.3f),
    
    BRISK(1.78816f, 5.0f),
    
    VERY_BRISK(2.01168f, 7.0f),
    
    FAST(2.2352f, 8.3f);
    
    /**
     * The upper bound, speed in m/s, of the bracket, inclusive.
     */
    private final float upperBound;
    
    /**
     * The ratio of the work metabolic rate to the resting metabolic rate.
     */
    private final float metabolicEquivalent;
    
    MetsWalkingBracket(float upperBound, float metabolicEquivalent) {
        this.upperBound = upperBound;
        this.metabolicEquivalent = metabolicEquivalent;
    }
    
    public float getMetabolicEquivalent() {
        return metabolicEquivalent;
    }
    
    /**
     * Gets the appropriate walking bracket for the given pace.
     *
     * @param pace the pace to get the bracket for.
     * @return the right {@link MetsWalkingBracket}
     */
    public static MetsWalkingBracket getBracket(float pace) {
        for (MetsWalkingBracket bracket : values()) {
            if (pace <= bracket.upperBound) {
                return bracket;
            }
        }
        
        return FAST;
    }
    
}
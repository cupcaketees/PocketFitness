package uk.ac.tees.cupcake.home.health.exercise;

import android.support.annotation.DrawableRes;

import uk.ac.tees.cupcake.R;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public enum Exercise {
    
    RUNNING(5, 20, R.drawable.exercise_selection_running),
    
    WALKING(2, 10, R.drawable.exercise_selection_walking),
    
    CYCLING(10, 50, R.drawable.exercise_selection_cycling);
    
    private final int minDisplacement;
    
    private final int maxDisplacement;
    
    @DrawableRes
    private final int drawableId;

    Exercise(int minDisplacement, int maxDisplacement, int drawableId) {
        this.minDisplacement = minDisplacement;
        this.maxDisplacement = maxDisplacement;
        this.drawableId = drawableId;
    }
    
    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
    
    public int getMinDisplacement() {
        return minDisplacement;
    }
    
    public int getMaxDisplacement() {
        return maxDisplacement;
    }
    
    public int getDrawableId() {
        return drawableId;
    }
    
}
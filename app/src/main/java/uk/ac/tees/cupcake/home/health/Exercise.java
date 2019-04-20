package uk.ac.tees.cupcake.home.health;

import android.support.annotation.DrawableRes;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class Exercise {

    private final String title;

    private final String description;
    
    @DrawableRes
    private final int drawableId;

    public Exercise(String title, String description, int drawableId) {
        this.title = title;
        this.description = description;
        this.drawableId = drawableId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getDrawableId() {
        return drawableId;
    }

}
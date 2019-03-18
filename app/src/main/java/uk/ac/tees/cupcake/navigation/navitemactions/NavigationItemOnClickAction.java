package uk.ac.tees.cupcake.navigation.navitemactions;

import android.content.Context;

/**
 * An action that is invoked upon clicking a Navigation item.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public interface NavigationItemOnClickAction {
    
    /**
     * Invoked when pressing/clicking a navigation item.
     *
     * @param context the context in which the navigation item is selected.
     */
    void itemSelected(Context context);

}
package uk.ac.tees.cupcake.navigation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.navitemactions.NavigationItemOnClickAction;

/**
 * Listens for item selection events on a navbar.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class NavigationItemSelectionListener implements NavigationView.OnNavigationItemSelectedListener {
    
    /**
     * Context in which the selection is made.
     */
    private final Context context;
    
    /**
     * <p>Menu item ids mapped to a resulting action class.</p>
     *
     * <p>Use {@link R.id}</p>
     */
    private final Map<Integer, NavigationItemOnClickAction> actions;
    
    public NavigationItemSelectionListener(Context context, Map<Integer, NavigationItemOnClickAction> actions) {
        this.context = context;
        this.actions = actions;
    }
    
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!actions.containsKey(item.getItemId())) {
            return false;
        }
    
        NavigationItemOnClickAction action = actions.get(item.getItemId());
        action.itemSelected(context);
        return true;
    }
    
}
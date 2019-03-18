package uk.ac.tees.cupcake.navigation.navitemactions;

import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * Represents an action that opens a new activity, invoked when selecting a navigation item.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class StartIntentNavigationItemAction<T> implements NavigationItemOnClickAction {
    
    /**
     * The class to be invoked for the intended activity.
     */
    private final Class<T> activityClass;
    
    /**
     * Extra data to be passed to the activity.
     */
    private final Map<String, ? extends Serializable> extras;
    
    public StartIntentNavigationItemAction(Class<T> activityClass, Map<String, ? extends Serializable> extras) {
        this.activityClass = activityClass;
        this.extras = extras;
    }
    
    public StartIntentNavigationItemAction(Class<T> activityClass) {
        this(activityClass, new HashMap<>());
    }
    
    @Override
    public void itemSelected(Context context) {
        if (extras.isEmpty()) {
            IntentUtils.invokeBaseView(context, activityClass);
        } else {
            IntentUtils.invokeViewWithExtras(context, activityClass, extras);
        }
    }
    
}
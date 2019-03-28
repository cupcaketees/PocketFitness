package uk.ac.tees.cupcake.utils.views;

import android.widget.Checkable;

import java.util.Map;

/**
 * An {@link CheckableLinearViewGroup.OnCheckStrategy} where checking is
 * limited to one child.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class OneSelectedOnCheckStrategy implements CheckableLinearViewGroup.OnCheckStrategy {
    
        @Override
        public void checked(int checkedId, CheckableLinearViewGroup group) {
            Map<Integer, Checkable> views = group.getCheckableViews();
            
            if (!views.containsKey(checkedId)) {
                CheckableConstraintView view = group.findViewById(checkedId);
    
                views.put(checkedId, view);
            }
    
            for (Map.Entry<Integer, Checkable> e : views.entrySet()) {
                if (e.getKey() == checkedId) {
                    continue;
                }
        
                e.getValue().setChecked(false);
            }
        }
    }
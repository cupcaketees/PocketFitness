package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link LinearLayout} that contains checkable views.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class CheckableLinearViewGroup extends LinearLayout {
    
    /**
     * Cache checkableViews for better performance.
     */
    private final Map<Integer, Checkable> checkableViews = new HashMap<>();
    
    /**
     * This is invoked upon one of the child checkableViews being checked.
     */
    private final List<OnCheckStrategy> onCheckStrategies = new ArrayList<>();
    
    public CheckableLinearViewGroup(Context context) {
        super(context);
    }
    
    public CheckableLinearViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void addOnCheckStrategy(OnCheckStrategy onCheckStrategy) {
        onCheckStrategies.add(onCheckStrategy);
    }
    
    /**
     * Called when one of the checkable children has been clicked.
     *
     * @param id the id of the clicked checkable.
     */
    public void childClicked(int id) {
        if (!checkableViews.containsKey(id)) {
            CheckableConstraintView checkable = findViewById(id);
    
            if (checkable == null) {
                throw new RuntimeException(id + " does not exist in this view group");
            }
            
            checkableViews.put(id, checkable);
        }
        
        for (OnCheckStrategy strategy : onCheckStrategies) {
            strategy.checked(id, this);
        }
    }
    
    public Map<Integer, Checkable> getCheckableViews() {
        return checkableViews;
    }
    
    /**
     * Gets a list of the ids of checked children in this view.
     *
     * @return a list of resource ids.
     */
    public List<Integer> getChecked() {
        List<Integer> checked = new ArrayList<>();
        
        for (Map.Entry<Integer, Checkable> entry : checkableViews.entrySet()) {
            if (entry.getValue().isChecked()) {
                checked.add(entry.getKey());
            }
        }
        
        return checked;
    }

    /**
     * Strategy for when one child of a group is checked.
     *
     * @author Sam-Hammersley <q5315908@tees.ac.uk>
     */
    public interface OnCheckStrategy {
        
        void checked(int checkedId, CheckableLinearViewGroup group);
        
    }
    
}
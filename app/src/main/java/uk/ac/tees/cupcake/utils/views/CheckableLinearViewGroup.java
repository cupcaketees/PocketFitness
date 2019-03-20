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
    
    private int checkedChild;
    
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
     * Called when one of the checkables have been clicked.
     *
     * @param id the id of the clicked checkable.
     */
    protected void childClicked(int id) {
        checkedChild = id;
        for (OnCheckStrategy strategy : onCheckStrategies) {
            strategy.checked(id, this);
        }
    }
    
    /**
     * Sets the {@link Checkable} associated with the specified id to the specified checked value.
     *
     * @param id the id of the checkable view
     * @param checked {@code true} if the checkable should be checked.
     */
    public void setChecked(int id, boolean checked) {
        Checkable checkable = checkableViews.containsKey(id) ? checkableViews.get(id) : findViewById(id);
        
        if (checkable == null) {
            throw new RuntimeException(id + " does not exist in this view group");
        }
        
        checkable.setChecked(checked);
        childClicked(id);
    }
    
    public Map<Integer, Checkable> getCheckableViews() {
        return checkableViews;
    }
    
    public int getCheckedChild() {
        return checkedChild;
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
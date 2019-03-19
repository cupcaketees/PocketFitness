package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

import java.util.HashMap;
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
    private OnCheckStrategy onCheckStrategy;
    
    public CheckableLinearViewGroup(Context context) {
        super(context);
    }
    
    public CheckableLinearViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public void setOnCheckStrategy(OnCheckStrategy onCheckStrategy) {
        this.onCheckStrategy = onCheckStrategy;
    }
    
    public void checked(int id) {
        checkedChild = id;
        onCheckStrategy.checked(id, this);
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
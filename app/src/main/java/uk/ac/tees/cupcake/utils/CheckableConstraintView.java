package uk.ac.tees.cupcake.utils;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a {@link View} that can be checked.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class CheckableConstraintView extends ConstraintLayout implements Checkable {
    
    /**
     * Denotes the checked state of this {@link CheckableConstraintView}.
     */
    private volatile boolean checked;
    
    /**
     * A list of {@link OnCheckedListener}s that are invoked upon checking this view.
     */
    private List<OnCheckedListener> onCheckedListeners = new ArrayList<>();
    
    public CheckableConstraintView(Context context) {
        super(context);
        inflateView();
    }
    
    public CheckableConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }
    
    /**
     * Adds an {@link OnCheckedListener} to be invoked upon checking.
     *
     * @param checkListener the listener to add.
     */
    public void addOnCheckListener(OnCheckedListener checkListener) {
        onCheckedListeners.add(checkListener);
    }
    
    @Override
    public void setChecked(boolean checked) {
        if (checked != this.checked) {
            if (!onCheckedListeners.isEmpty()) {
                for (OnCheckedListener listener : onCheckedListeners) {
                    listener.onChecked(checked);
                }
            }
            
            this.checked = checked;
            
            if (checked && getParent() instanceof CheckableLinearViewGroup) {
                CheckableLinearViewGroup parent = (CheckableLinearViewGroup) getParent();
        
                parent.checked(getId());
            }
        }
    }
    
    @Override
    public boolean isChecked() {
        return checked;
    }
    
    @Override
    public void toggle() {
        if (!checked) {
            setChecked(!checked);
        }
    }
    
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        
        if (isChecked()) {
            mergeDrawableStates(drawableState, new int[] { android.R.attr.state_checked });
        }
        
        return drawableState;
    }
    
    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
    
    /**
     * Implementation of this method should inflate the view.
     */
    public abstract void inflateView();
    
    /**
     * @author Sam-Hammersley <q5315908@tees.ac.uk>
     */
    public interface OnCheckedListener {
        
        /**
         * Called when a {@link CheckableConstraintView} is checked.
         *
         * @param isChecked the checked state of the {@link CheckableConstraintView}
         */
        void onChecked(boolean isChecked);
    }
    
}
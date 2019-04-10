package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * A {@link HorizontalScrollView} that can be attached to by listeners, listening for scroll changes.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class ObservableHorizontalScrollView extends HorizontalScrollView {
    
    private OnScrollChangedListener onScrollChangedListener;
    
    public ObservableHorizontalScrollView(Context context) {
        super(context);
    }
    
    public void setOnScrollChangedListener(OnScrollChangedListener l) {
        onScrollChangedListener = l;
    }

    @Override
    protected void onScrollChanged(int currentX, int currentY, int oldX, int oldY) {
        super.onScrollChanged(currentX, currentY, oldX, oldY);
        
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(currentX);
        }
    }
    
    /**
     * Listener for events whereby this scroll view is scrolled i.e.
     * {@link #onScrollChanged(int, int, int, int)} is called.
     */
    public interface OnScrollChangedListener {
    
        /**
         * Invoked when scroll changes.
         */
        void onScrollChanged(int currentX);
        
    }

}
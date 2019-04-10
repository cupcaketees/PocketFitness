package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import uk.ac.tees.cupcake.R;

/**
 * Represents a {@link CheckableConstraintView} that has an {@link ImageView} of some type {@link T}
 * that must be instantiated in the {@link #inflateView()} method.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class CheckableImageConstraintView<T extends ImageView> extends CheckableConstraintView {
    
    /**
     * The {@ImageView} part of the layout this must be instantiated.
     */
    protected T imageView;
    
    public CheckableImageConstraintView(Context context) {
        super(context);
    }
    
    public CheckableImageConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    
        applyAttributes(attrs);
    }
    
    /**
     * Gets the attributes specified in the layout and applies them to the components of this view.
     *
     * @param attrs attribute set containing the attributes
     */
    private final void applyAttributes(AttributeSet attrs) {
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.CheckableImageConstraintViewAttrs, 0, 0);
        
        try {
            imageView.setImageDrawable(a.getDrawable(R.styleable.CheckableImageConstraintViewAttrs_checkable_image_src));
        } finally {
            a.recycle();
        }
    }
    
}
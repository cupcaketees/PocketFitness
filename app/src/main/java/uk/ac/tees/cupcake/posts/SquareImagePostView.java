package uk.ac.tees.cupcake.posts;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 * Used for non static images.
 */
public class SquareImagePostView extends AppCompatImageView {

    public SquareImagePostView(Context context) {
        super(context);
    }

    public SquareImagePostView(Context context, AttributeSet attribute) {
        super(context, attribute);
    }

    public SquareImagePostView(Context context, AttributeSet attribute, int styleAttribute) {
        super(context, attribute, styleAttribute);
    }

    /**
     *The size you want each square to be.
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}

package uk.ac.tees.cupcake.posts;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

}

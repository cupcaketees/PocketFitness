package uk.ac.tees.cupcake.home.health.heartrate;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.ColourUtility;
import uk.ac.tees.cupcake.utils.views.CheckableConstraintView;
import uk.ac.tees.cupcake.utils.views.CheckableImageConstraintView;

/**
 * An implementation of {@link CheckableImageConstraintView} specifically for heart rate measurement selection.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateTypeConstraintView extends CheckableImageConstraintView<CircleImageView> {
    
    public HeartRateTypeConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnCheckListener(new HeartRateTypeCheckedListener());
    
        applyAttributes(attrs);
    }
    
    @Override
    public void inflateView() {
        View root = inflate(getContext(), R.layout.heart_rate_measurement_type_layout, this);
        root.setClickable(true);
    
        imageView = findViewById(R.id.measurement_type_image);
        imageView.setCircleBackgroundColor(0xFFD0D0D0);
    }
    
    /**
     * Gets the attributes specified in the layout and applies them to the components of this view.
     *
     * @param attrs attribute set containing the attributes
     */
    private void applyAttributes(AttributeSet attrs) {
        TextView label = findViewById(R.id.measurement_type_label);
        
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HeartRateTypeConstraintViewAttrs, 0, 0);
    
        try {
            label.setText(a.getString(R.styleable.HeartRateTypeConstraintViewAttrs_label_text));
        } finally {
            a.recycle();
        }
    }
    
    /**
     * An {@link CheckableConstraintView.OnCheckedListener} specifically for {@link HeartRateTypeConstraintView}
     * where the appropriate graphical changes are made upon checking/clicking the view.
     */
    private final class HeartRateTypeCheckedListener implements OnCheckedListener {
        
        @Override
        public void onChecked(boolean isChecked) {
            int colour = isChecked ?
                    ColourUtility.setAlpha(0xF0, getResources().getColor(R.color.colorPrimaryDark))
                    : 0xFFD0D0D0;
            
            imageView.setCircleBackgroundColor(colour);
            LayerDrawable d = (LayerDrawable) imageView.getBackground().mutate();
            
            for (int index = 0; index < 5; index++) {
                int adjustedColour = ColourUtility.setAlpha((index + 1) * 0x10, colour);
                ((GradientDrawable) d.getDrawable(index)).setColor(adjustedColour);
            }
        }
    }
    
}
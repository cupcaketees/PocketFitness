package uk.ac.tees.cupcake.home.health.heartrate;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.views.CheckableConstraintView;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HeartRateTypeConstraintView extends CheckableConstraintView {
    
    private CircleImageView imageView;
    
    private final OnCheckedListener checkedListener = new HeartRateTypeCheckedListener();
    
    final class HeartRateTypeCheckedListener implements OnCheckedListener {
    
        @Override
        public void onChecked(boolean isChecked) {
            imageView.setCircleBackgroundColor(isChecked ? 0x20FF0000 : 0x00000000);
            imageView.setBorderColor(isChecked ? 0x50FF0000 : 0x00000000);
        }
    }
    
    public HeartRateTypeConstraintView(Context context) {
        super(context);
        addOnCheckListener(checkedListener);
    }
    
    public HeartRateTypeConstraintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnCheckListener(checkedListener);
    
        applyAttributes(attrs);
    }
    
    @Override
    public void inflateView() {
        View root = inflate(getContext(), R.layout.heart_rate_measurement_type_layout, this);
        root.setClickable(true);
    
        imageView = findViewById(R.id.measurement_type_image);
    }
    
    private void applyAttributes(AttributeSet attrs) {
        TextView label = findViewById(R.id.measurement_type_label);
        
        TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.HeartRateTypeConstraintViewAttrs, 0, 0);
    
        try {
            label.setText(a.getString(R.styleable.HeartRateTypeConstraintViewAttrs_label_text));
            imageView.setImageDrawable(a.getDrawable(R.styleable.HeartRateTypeConstraintViewAttrs_image_src));
        } finally {
            a.recycle();
        }
    }
    
}
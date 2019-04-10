package uk.ac.tees.cupcake.account.healthstats;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.ColourUtility;
import uk.ac.tees.cupcake.utils.views.CheckableImageConstraintView;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HealthStatsSexCheckable extends CheckableImageConstraintView<ImageView> {
    
    public HealthStatsSexCheckable(Context context) {
        super(context);
        addOnCheckListener(checkedListener);
    }
    
    public HealthStatsSexCheckable(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnCheckListener(checkedListener);
    }
    
    @Override
    public void inflateView() {
        View root = inflate(getContext(), R.layout.health_stats_sex_checkable, this);
        root.setClickable(true);
        
        imageView = root.findViewById(R.id.health_stats_image_sex);
    }

    private final OnCheckedListener checkedListener = isChecked -> {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int colour = isChecked ? ColourUtility.setAlpha(255, primaryDark) : 0xFFD0D0D0;
        
        imageView.setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
    };
}
package uk.ac.tees.cupcake.account.healthstats;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
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
        imageView.setColorFilter(0xFFD0D0D0, PorterDuff.Mode.SRC_ATOP);
    }

    private final OnCheckedListener checkedListener = isChecked -> {
        int primaryDark = getResources().getColor(R.color.colorPrimaryDark);
        int colour = isChecked ? ColourUtility.setAlpha(255, primaryDark) : 0xFFD0D0D0;
        
        if (isChecked) {
            Animation animation = new ScaleAnimation(
                    1.2f, 1f, 1.2f, 1f,
                    ScaleAnimation.RELATIVE_TO_SELF, .5f,
                    ScaleAnimation.RELATIVE_TO_SELF, .5f);
            animation.setInterpolator(t -> (float) -(Math.pow(Math.E, -t*5) * Math.cos(20 * t)) + 1);
            animation.setDuration(1500);
            
            startAnimation(animation);
        }
        
        imageView.setColorFilter(colour, PorterDuff.Mode.SRC_ATOP);
    };
}
package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import uk.ac.tees.cupcake.R;

/**
 * An extension of {@link FrameLayout} to contain a number scale where a value can be selected.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class ScaleValuePickerView extends FrameLayout {
    
    /**
     * The colour of the scale.
     */
    private int scaleColor;
    
    private Paint paint;
    
    /**
     * A {@link Path} for drawing the pointer for the scale, re-used.
     */
    private Path scalePointerPath;
    
    /**
     * The {@link ScaleView} to pick the value from.
     */
    private ScaleView scaleView;
    
    private ObservableHorizontalScrollView scrollView;
    
    /**
     * The size of the scale with respect to the display size.
     */
    private float derivedScaleWidth;
    
    /**
     * The maximum scale value.
     */
    private float maxValue;
    
    /**
     * The minimum scale value.
     */
    private float minValue;
    
    /**
     * The value to start at.
     */
    private float initialValue;

    public float getInitialValue() {
        return initialValue;
    }
    
    public ScaleValuePickerView(Context context) {
        super(context);
        setup(context);
    }

    public ScaleValuePickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
        applyAttributes(attrs);
    }

    public ScaleValuePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
        applyAttributes(attrs);
    }
    
    private void applyAttributes(AttributeSet attrs) {
        TypedArray a = getContext()
                .getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ScrollingValuePickerAttrs, 0, 0);
        
        try {
            scaleColor = a.getColor(R.styleable.ScrollingValuePickerAttrs_scale_color, 0xFFFFFF);
            scaleView.setScaleColor(scaleColor);
            paint.setColor(scaleColor);
        } finally {
            a.recycle();
        }
    }

    public float getCurrentValue() {
        // increment size in pixels
        float incrementSize = (float) scrollView.getWidth() * (derivedScaleWidth - 1) / (maxValue - minValue);

        float scaleValue = (scrollView.getScrollX() / incrementSize) + minValue;
    
        // ensure scale value is within the bounds of min and max values
        return Math.max(minValue, Math.min(scaleValue, maxValue));
    }
    
    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        scrollView.setOnScrollChangedListener(currentX -> onValueChangedListener.onValueChanged(getCurrentValue()));
    }
    
    public void setMinValue(float minValue) {
        this.minValue = minValue;
        scaleView.setMinValue(minValue);
    }
    
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        scaleView.setMaxValue(maxValue);
    }
    
    public void setScaleDivisionStep(int scaleDivisionStep) {
        scaleView.setScaleDividerIncrement(scaleDivisionStep);
    }
    
    public void setScaleStep(int scaleStep) {
        scaleView.setScaleIncrement(scaleStep);
    }

    public void setDerivedScaleWidth(int derivedScaleWidth) {
        this.derivedScaleWidth = derivedScaleWidth;
        scaleView.setDerivedScaleWidth(derivedScaleWidth);
    }
    
    public void setInitialValue(float initialValue) {
        this.initialValue = initialValue;
    }
    
    private void setup(Context context) {
        scrollView = new ObservableHorizontalScrollView(context);
        scrollView.setHorizontalScrollBarEnabled(false);
        addView(scrollView);

        final LinearLayout container = new LinearLayout(context);
        scrollView.addView(container);

        scaleView = new ScaleView(context);
        container.addView(scaleView);
    
        paint = new Paint();
        paint.setColor(scaleColor);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    
        scalePointerPath = new Path();
    }
    
    /**
     * Sets a path for drawing the scale pointer.
     */
    private void setPointerPath() {
        final int width = 80;
        final int x = getWidth() / 2;
        final int y = 0;
        final int height = 50;
    
        scalePointerPath.reset();
        scalePointerPath.moveTo(x - width / 2, y);
        scalePointerPath.lineTo(x, y + height);
        scalePointerPath.lineTo(x + width / 2, y);
        scalePointerPath.lineTo(x, y + height / 2);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        setPointerPath();
        canvas.drawPath(scalePointerPath, paint);
        
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            final ViewGroup.LayoutParams scaleViewParams = scaleView.getLayoutParams();
    
            scaleViewParams.width = (int) (getWidth() * derivedScaleWidth);
            scaleView.setLayoutParams(scaleViewParams);
            scaleView.invalidate();

            invalidate();
        }
    }
    
    /**
     * Listener for value changing events.
     */
    public interface OnValueChangedListener {
    
        /**
         * Invoked when the scroll view is scrolled and subsequently
         * the value the pointer is pointing to changes.
         *
         * @param newValue the new value this scale is pointing to.
         */
        void onValueChanged(float newValue);
        
    }
}
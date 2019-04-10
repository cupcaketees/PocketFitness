package uk.ac.tees.cupcake.utils.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * A {@link View} that contains a number scale.
 */
public class ScaleView extends View {
    
    private Paint paint;
    private Paint textPaint;
    
    private float maxValue;
    private float minValue;
    
    private int textSize;
    
    /**
     * The size of the increment for this scale.
     */
    private int scaleIncrement;
    
    /**
     * The size of the increment for the dividing points.
     */
    private int scaleDividerIncrement;
    
    /**
     * The size of this scale in multiples of the display width.
     */
    private int derivedScaleWidth;
    
    private static final float SCALE_INCREMENT_LINE_HEIGHT = .60f;
    
    public ScaleView(Context context) {
        super(context);
        setup(context);
    }
    
    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }
    
    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup(context);
    }
    
    private void setup(Context context) {
        paint = new Paint();
        paint.isAntiAlias();
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
    
        final float scaleFactor = context.getResources().getDisplayMetrics().scaledDensity;
        textSize = Math.round(14 * scaleFactor);
        
        textPaint = new Paint();
        textPaint.isAntiAlias();
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        
        invalidate();
    }
    
    public void setScaleColor(int scaleColor) {
        paint.setColor(scaleColor);
        textPaint.setColor(scaleColor);
    }
    
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
    
    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }
    
    public void setScaleDividerIncrement(int scaleDividerIncrement) {
        this.scaleDividerIncrement = scaleDividerIncrement;
    }
    
    public void setScaleIncrement(int scaleIncrement) {
        this.scaleIncrement = scaleIncrement;
    }
    
    public void setDerivedScaleWidth(int derivedScaleWidth) {
        this.derivedScaleWidth = derivedScaleWidth;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        final int scaleHeight = getMeasuredHeight();
        final int scaleWidth = getMeasuredWidth();
        final int scaleSteps = (int) (maxValue - minValue);
        
        // x gap between each line, length of usable scale / the number of steps
        final float viewInterval = (float) (scaleWidth * (derivedScaleWidth - 1) / derivedScaleWidth) / scaleSteps;
    
        // the scale spans the width of the display * 4, xOffset is half the display's width
        final int xOffset = (scaleWidth / derivedScaleWidth) / 2;
        
        // number of fading out lines on the scale (at the ends of the scale)
        final int carryOver = (int) (xOffset / viewInterval);
        
        for (int i = 0, alpha = 255; i < carryOver; i++, alpha -= (255 / carryOver)) {
            float x = xOffset - (viewInterval * i);
            boolean longLine = ((int) (i + minValue) * scaleIncrement) % scaleDividerIncrement == 0;
            
            paint.setAlpha(alpha);
            
            canvas.drawLine(x, 30, x, scaleHeight * SCALE_INCREMENT_LINE_HEIGHT * (longLine ? 1 : .60f), paint);
        }
        
        paint.setAlpha(255);
        for (int i = 0; i < scaleSteps + 1; i++) {
            float x = xOffset + (viewInterval * i);

            if (((int) (i + minValue) * scaleIncrement) % scaleDividerIncrement == 0) {
                canvas.drawLine(x, 30, x, scaleHeight * SCALE_INCREMENT_LINE_HEIGHT, paint);
                canvas.drawText("" + ((int) (i + minValue) * scaleIncrement), x, scaleHeight * SCALE_INCREMENT_LINE_HEIGHT + textSize, textPaint);
            } else {
                canvas.drawLine(x, 30, x, scaleHeight * SCALE_INCREMENT_LINE_HEIGHT * .6f, paint);
            }
        }

        for (int i = 0, alpha = 255; i < carryOver; i++, alpha -= (255 / carryOver)) {
            float x = xOffset + viewInterval * (scaleSteps + i);
            boolean longLine = ((int) (i + minValue) * scaleIncrement) % scaleDividerIncrement == 0;

            paint.setAlpha(alpha);

            canvas.drawLine(x, 30, x, scaleHeight * SCALE_INCREMENT_LINE_HEIGHT * (longLine ? 1 : .60f), paint);
        }
        
        super.onDraw(canvas);
    }
}
package uk.ac.tees.cupcake.utils.views;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.List;

import uk.ac.tees.cupcake.utils.ColourUtility;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class GraphViewUtility {
    
    private static final int GRADIENT_START_ALPHA = 0x90;
    
    public static void setupLineChart(LineChart chart) {
        chart.getDescription().setEnabled(false);
        chart.setTouchEnabled(false);
        chart.setPinchZoom(true);
        
        chart.getAxisRight().setEnabled(false);
        chart.getAxisRight().setAxisMaximum(220);
        chart.getViewPortHandler().setMaximumScaleX(2f);
        chart.getViewPortHandler().setMaximumScaleY(2f);
        
        chart.getAxisLeft().setSpaceBottom(75f);
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        
        chart.getLegend().setEnabled(false);
        
        chart.setBackgroundColor(Color.TRANSPARENT);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
    }
    
    public static void setChartData(LineChart chart, int colour, List<Entry> data) {
        chart.getAxisLeft().removeAllLimitLines();
        
        int[] colours = {ColourUtility.setAlpha(GRADIENT_START_ALPHA, colour), Color.TRANSPARENT};
        
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colours);
        
        LineDataSet dataSet = new LineDataSet(data, "");
        dataSet.setFillDrawable(gd);
        dataSet.setColor(colour);
        
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(true);
        dataSet.setDrawFilled(true);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        LimitLine maxLine = new LimitLine(dataSet.getYMax());
        maxLine.setLabel("Max " + (int) dataSet.getYMax());
    
        LimitLine minLine = new LimitLine(dataSet.getYMin());
        minLine.setLabel("Min " + (int) dataSet.getYMin());
        
        chart.getAxisLeft().addLimitLine(maxLine);
        chart.getAxisLeft().addLimitLine(minLine);
        chart.invalidate();
    }
    
}
package uk.ac.tees.cupcake.home;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateMeasurement;
import uk.ac.tees.cupcake.sensors.SensorAdapter;
import uk.ac.tees.cupcake.sensors.StepCounterSensorListener;
import uk.ac.tees.cupcake.utils.IntentUtils;

public final class HomeFragment extends OnChangeFragment {

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private static final List<String> DAYS = Arrays.asList( "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

    private View stepsCard;

    private BarChart barChart;

    private TextView lastMeasurement;

    private TextView lastMeasurementDate;

    private TextView lastMeasurementType;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        barChart = view.findViewById(R.id.home_steps_bar_chart);
        setupBarChart(barChart);

        stepsCard = view.findViewById(R.id.home_steps_card);
        lastMeasurement = view.findViewById(R.id.home_heart_last_measurement);
        lastMeasurementDate = view.findViewById(R.id.home_heart_last_measurement_date);
        lastMeasurementType = view.findViewById(R.id.home_heart_last_measurement_type);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getStepsData(barChart);
        getHeartData(lastMeasurement, lastMeasurementType, lastMeasurementDate);

        SensorAdapter sensorAdapter = new SensorAdapter(getContext(), Sensor.TYPE_STEP_COUNTER);
        if (!sensorAdapter.setupSensors()) {
            Toast.makeText(getContext(), "Failed to setup sensors", Toast.LENGTH_SHORT).show();
        } else {
            sensorAdapter.registerListener(SensorManager.SENSOR_DELAY_NORMAL, Sensor.TYPE_STEP_COUNTER, new StepCounterSensorListener(stepsCard));
        }
    }

    @Override
    public void onChange() {
        barChart.animateY(1000, Easing.Linear);
    }

    private void setupBarChart(BarChart barChart) {
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter((value, axis) -> DAYS.get((int) value));
        xAxis.setGranularity(1f);

        barChart.setFitBars(true);
        barChart.setDrawGridBackground(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getAxisLeft().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
    }

    private void setChartData(BarChart barChart, List<BarEntry> entries) {
        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setValueFormatter(new DefaultValueFormatter(0));
        barDataSet.setForm(Legend.LegendForm.NONE);
        barDataSet.setValueTextSize(12f);

        int color = getResources().getColor(R.color.colorPrimary);
        barDataSet.setColor(color);

        BarData barData = new BarData(barDataSet);
        barData.setHighlightEnabled(false);
        barData.setBarWidth(.2f);

        barChart.setData(barData);
        barChart.invalidate();
    }

    private void getStepsData(final BarChart barChart) {
        firestore.collection("UserStats")
                .document(auth.getCurrentUser().getUid())
                .collection("StepCounts")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(7)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<StepCountMeasurement> measurements = documentSnapshots.toObjects(StepCountMeasurement.class);
                    List<BarEntry> entries = new ArrayList<>(measurements.size());

                    for (int index = 0; index < measurements.size(); index++) {
                        StepCountMeasurement measurement = measurements.get(index);

                        BarEntry barEntry = new BarEntry(measurement.getTimestamp().getDay(), measurement.getCount());

                        entries.add(barEntry);
                    }

                    setChartData(barChart, entries);
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void getHeartData(TextView value, TextView type, TextView date) {
        firestore.collection("UserStats")
                .document(auth.getCurrentUser().getUid())
                .collection("HeartRates")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<HeartRateMeasurement> measurements = documentSnapshots.toObjects(HeartRateMeasurement.class);

                    if (!measurements.isEmpty()) {
                        HeartRateMeasurement measurement = measurements.get(0);
                        String measurementType = measurement.getMeasurementType();

                        value.setText(getResources().getString(R.string.heart_rate_text, measurement.getBpm()));
                        type.setText(Character.toUpperCase(measurementType.charAt(0)) + measurementType.substring(1));
                        date.setText(DATE_FORMAT.format(new Date(measurement.getTimestamp())));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm");

}
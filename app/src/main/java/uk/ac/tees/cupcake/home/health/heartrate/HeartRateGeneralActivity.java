package uk.ac.tees.cupcake.home.health.heartrate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.views.CheckableLinearViewGroup;
import uk.ac.tees.cupcake.utils.views.GraphViewUtility;
import uk.ac.tees.cupcake.utils.views.OneSelectedOnCheckStrategy;

public class HeartRateGeneralActivity extends AppCompatActivity {
    
    /**
     * The view group containing checkable heart rate measurement types.
     */
    private CheckableLinearViewGroup measurementTypes;
    
    /**
     * Stores data retrieved from the database; mapping a measurement type name to a set of data.
     */
    private final Map<String, List<Entry>> cachedGraphingData = new HashMap<>();
    
    /**
     * A linechart to graph previous measurements.
     */
    private LineChart graph;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_general);
    
        graph = findViewById(R.id.heart_rate_general_chart);
        GraphViewUtility.setupLineChart(graph);
        
        measurementTypes = findViewById(R.id.heart_rate_general_measurement_types);
    
        getData().addOnCompleteListener(e -> measurementTypes.childClicked(R.id.heart_rate_general_all_measurement_type));
        
        measurementTypes.addOnCheckStrategy(new OneSelectedOnCheckStrategy());
        measurementTypes.addOnCheckStrategy((id, v) -> {
            String resourceId = getResources().getResourceEntryName(id).substring(19);
            String typeName = resourceId.substring(0, resourceId.indexOf("_"));
            
            if (typeName.toLowerCase().contains("all")) {
                List<Entry> entries = new ArrayList<>();
                
                for (List<Entry> l : cachedGraphingData.values()) {
                    entries.addAll(l);
                }
                
                applyGraphData(entries);
            } else {
                applyGraphData(cachedGraphingData.get(typeName));
            }
        });
    }
    
    /**
     * Takes the average of the given data set and then sets the text value of the appropriate {@link TextView}.
     */
    private void applyGraphData(List<Entry> entries) {
        int colour = getResources().getColor(R.color.heart_rate_measure);
        GraphViewUtility.setChartData(graph, colour, entries);
    
        if (entries != null && entries.size() > 0) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(1000);
            graph.startAnimation(fadeIn);
        }
    }
    
    /**
     * Gets the heart rate data to plot in the graph of previous heart rate measurementTypes.
     *
     * @return a {@link List} of {@link Entry} to be plot in a graph.
     */
    private Task<QuerySnapshot> getData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return FirebaseFirestore.getInstance()
                .collection("UserStats")
                .document(auth.getCurrentUser().getUid())
                .collection("HeartRates")
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    final int childCount = measurementTypes.getChildCount();
                    for (int i = 0; i < childCount; i++) {
                        int type = measurementTypes.getChildAt(i).getId();
                        
                        String resourceId = getResources().getResourceEntryName(type).substring(19);
                        String typeName = resourceId.substring(0, resourceId.indexOf("_"));
                        if (!typeName.toLowerCase().contains("all")) {
                            cachedGraphingData.put(typeName, new ArrayList<>());
                        }
                    }
                    
                    List<HeartRateMeasurement> measurements = documentSnapshots.toObjects(HeartRateMeasurement.class);
                    for (int i = 0; i < measurements.size(); i++) {
                        cachedGraphingData
                                .get(measurements.get(i).getMeasurementType())
                                .add(new Entry(i, measurements.get(i).getBpm()));
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(HeartRateGeneralActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
    public void measureOnClick(View view) {
        startActivity(new Intent(this, HeartRateActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
    
}
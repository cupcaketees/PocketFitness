package uk.ac.tees.cupcake.home.health;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.utils.CheckableLinearViewGroup;
import uk.ac.tees.cupcake.utils.GraphViewUtility;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.OneSelectedOnCheckStrategy;

public final class SaveHeartRateActivity extends AppCompatActivity {
    
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_heart_rate);
        getData();
        
        TextView measurementTextView = findViewById(R.id.save_heart_rate_bpm);
        
        HeartRateMeasurement measurement = (HeartRateMeasurement) getIntent().getSerializableExtra("heart_rate_measurement");
        measurementTextView.setText(Integer.toString(measurement.getBpm()));
    
        CheckableLinearViewGroup measurementTypes = findViewById(R.id.measurement_types);
        measurementTypes.setOnCheckStrategy(new OneSelectedOnCheckStrategy());
    }
    
    /**
     * Takes the average of the given data set and then sets the text value of the appropriate {@link TextView}.
     */
    private void setAverage(List<Entry> entries) {
        TextView graphText = findViewById(R.id.graph_heart_rate);
        
        float average = 0;
        for (Entry e : entries) {
            average += e.getY();
        }
        
        String averageValue = Integer.toString(Math.round(average / entries.size()));
        graphText.setText(averageValue);
    
        GraphViewUtility.setupChart(findViewById(R.id.heart_rate_chart), "#FF0000", "heart-rate", entries);
    }
    
    /**
     * Gets the heart rate data to plot in the graph of previous heart rate measurementTypes.
     *
     * @return a {@link List} of {@link Entry} to be plot in a graph.
     */
    private void getData() {
        String uid = auth.getCurrentUser().getUid();
        
        firestore.collection("UserStats")
                .document(uid)
                .collection("HeartRates")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    List<Entry> entries = new ArrayList<>();
                    float index = 0;
                    
                    for (HeartRateMeasurement heartRateMeasurement : documentSnapshots.toObjects(HeartRateMeasurement.class)) {
                        entries.add(new Entry(index++, heartRateMeasurement.getBpm()));
                    }
                    
                    setAverage(entries);
                })
                .addOnFailureListener(doc -> Toast.makeText(SaveHeartRateActivity.this, doc.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
    /**
     * Invoked upon pressing the save button.
     * @param view
     */
    public void saveMeasurement(View view) {
        Intent intent = getIntent();
        HeartRateMeasurement measurement = (HeartRateMeasurement) intent.getSerializableExtra("heart_rate_measurement");
        
        CheckableLinearViewGroup viewGroup = findViewById(R.id.measurement_types);
        
        measurement.setMeasurementType(viewGroup.getCheckedChild());
        
        String uid = auth.getCurrentUser().getUid();
        
        firestore.collection("UserStats")
                .document(uid)
                .collection("HeartRates")
                .add(measurement)
                .addOnSuccessListener(doc -> {
                    IntentUtils.invokeBaseView(SaveHeartRateActivity.this, HomeActivity.class);
                    finish();
                })
                .addOnFailureListener(doc -> Toast.makeText(SaveHeartRateActivity.this, doc.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
    /**
     * Invoked upon pressing the cancel button.
     * @param view
     */
    public void cancelMeasurement(View view) {
        IntentUtils.invokeBaseView(SaveHeartRateActivity.this, HomeActivity.class);
        finish();
    }
    
}
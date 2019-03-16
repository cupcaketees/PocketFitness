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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.home.HomeActivity;
import uk.ac.tees.cupcake.utils.GraphViewUtility;
import uk.ac.tees.cupcake.utils.IntentUtils;

public final class SaveHeartRateActivity extends AppCompatActivity {
    
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    private String uid;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_heart_rate);
        
        TextView measurementTextView = findViewById(R.id.save_heart_rate_bpm);
        
        HeartRateMeasurement measurement = (HeartRateMeasurement) getIntent().getSerializableExtra("heart_rate_measurement");
        measurementTextView.setText(Integer.toString(measurement.getBpm()));
        
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        List<Entry> data = getData();
    
        setAverage(data);
    
        GraphViewUtility.setupChart(findViewById(R.id.heart_rate_chart), "#FF0000", "heart-rate", data);
    }
    
    /**
     * Takes the average of the given data set and then sets the text value of the appropriate {@link TextView}.
     *
     * @param data the data to calculate an average from.
     */
    private void setAverage(List<Entry> data) {
        TextView graphText = findViewById(R.id.graph_heart_rate);
        
        float average = 0;
        for (Entry e : data) {
            average += e.getY();
        }
        
        String averageValue = Integer.toString(Math.round(average / data.size()));
        graphText.setText(averageValue);
    }
    
    /**
     * Gets the heart rate data to plot in the graph of previous heart rate measurements.
     *
     * @return a {@link List} of {@link Entry} to be plot in a graph.
     */
    private List<Entry> getData() {
        List<Entry> entries = new ArrayList<>();
    
        firestore.collection("UserStats")
                .document(uid)
                .collection("HeartRates")
                .get()
                .addOnSuccessListener(documentSnapshots -> {
                    
                    for (HeartRateMeasurement heartRateMeasurement : documentSnapshots.toObjects(HeartRateMeasurement.class)) {
                        entries.add(heartRateMeasurement.toChartEntry());
                    }
                    
                })
                .addOnFailureListener(doc -> Toast.makeText(SaveHeartRateActivity.this, doc.getMessage(), Toast.LENGTH_SHORT).show());
        
        if (entries.isEmpty()) {
            for (int i = 0; i < 20; i++) {
                int val = 30 + (int) (Math.random() * 5) - (int) (Math.random() * 5);
                entries.add(new Entry(i, val, null));
            }
        }
        
        return entries;
    }
    
    public void saveMeasurement(View view) {
        Intent intent = getIntent();
        HeartRateMeasurement measurement = (HeartRateMeasurement) intent.getSerializableExtra("heart_rate_measurement");
        
        firestore.collection("UserStats")
                .document(uid)
                .collection("HeartRates")
                .add(measurement)
                .addOnSuccessListener(doc -> IntentUtils.invokeBaseView(SaveHeartRateActivity.this, HomeActivity.class))
                .addOnFailureListener(doc -> Toast.makeText(SaveHeartRateActivity.this, doc.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
}

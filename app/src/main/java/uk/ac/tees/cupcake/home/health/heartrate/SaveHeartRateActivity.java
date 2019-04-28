package uk.ac.tees.cupcake.home.health.heartrate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.views.CheckableLinearViewGroup;
import uk.ac.tees.cupcake.utils.views.GraphViewUtility;
import uk.ac.tees.cupcake.utils.views.OneSelectedOnCheckStrategy;

/**
 * On this view the user is given the choice to save the taken heart rate measurement.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public final class SaveHeartRateActivity extends AppCompatActivity {
    
    /**
     * Firestore database interface.
     */
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    /**
     * Firebase authentication system interface.
     */
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    
    /**
     * The view group containing checkable heart rate measurement types.
     */
    private CheckableLinearViewGroup measurementTypes;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_heart_rate_action_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.heart_rate_action_bar_save) {
            saveMeasurement();
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_heart_rate);

        TextView measurementTextView = findViewById(R.id.save_heart_rate_bpm);
        HeartRateMeasurement measurement = (HeartRateMeasurement) getIntent().getSerializableExtra("heart_rate_measurement");
        measurementTextView.setText(Integer.toString(measurement.getBpm()));
        
        measurementTypes = findViewById(R.id.measurement_types);
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce_anim);
        measurementTypes.startAnimation(bounce);
        measurementTypes.addOnCheckStrategy(new OneSelectedOnCheckStrategy());
        measurementTypes.childClicked(R.id.general_measurement_type);
    }
    
    /**
     * Invoked upon pressing the save button.
     */
    public void saveMeasurement() {
        Intent intent = getIntent();
        HeartRateMeasurement measurement = (HeartRateMeasurement) intent.getSerializableExtra("heart_rate_measurement");
        
        String resourceId = getResources().getResourceName(measurementTypes.getChecked().get(0));
        measurement.setMeasurementType(resourceId.substring(resourceId.indexOf("/") + 1, resourceId.indexOf("_")));
        
        firestore.collection("UserStats")
                .document(auth.getCurrentUser().getUid())
                .collection("HeartRates")
                .add(measurement)
                .addOnSuccessListener(doc -> {
                    IntentUtils.invokeBaseView(SaveHeartRateActivity.this, HeartRateGeneralActivity.class);
                    finish();
                })
                .addOnFailureListener(doc -> Toast.makeText(SaveHeartRateActivity.this, doc.getMessage(), Toast.LENGTH_SHORT).show());
    }
    
}
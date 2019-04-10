package uk.ac.tees.cupcake.account.healthstats;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.views.CheckableLinearViewGroup;
import uk.ac.tees.cupcake.utils.views.OneSelectedOnCheckStrategy;
import uk.ac.tees.cupcake.utils.views.ScaleValuePickerView;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class HealthStatsSetupActivity extends AppCompatActivity {
    
    private boolean recordHeight;
    
    private boolean recordWeight;
    
    private CheckableLinearViewGroup checkableGroup;
    
    private ScaleValuePickerView heightPicker;
    
    private ScaleValuePickerView weightPicker;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_stats_setup);
    
        heightPicker = findViewById(R.id.health_stats_height_picker);
        weightPicker = findViewById(R.id.health_stats_weight_picker);
        
        checkableGroup = findViewById(R.id.health_stats_setup_content_sex_group);
        checkableGroup.addOnCheckStrategy(new OneSelectedOnCheckStrategy());
        
        addHeightPicker();
        addWeightPicker();
    }
    
    public enum Sex {
        
        MALE, FEMALE;
        
        static Sex get(int resourceId) {
            switch (resourceId) {
                case R.id.health_stats_sex_male:
                    return MALE;
                    
                case R.id.health_stats_sex_female:
                    return FEMALE;
                    
                default:
                    throw new NullPointerException();
            }
        }
    }
    
    public void onClickFinish(View view) {
        
        if (!checkableGroup.getChecked().isEmpty()) {
            Sex selected = Sex.get(checkableGroup.getChecked().get(0));
        }
        
        if (recordHeight) {
            heightPicker.getCurrentValue();
        }
        
        if (recordWeight) {
            weightPicker.getCurrentValue();
        }
    }
    
    public void onHeightCheckBoxClick(View view) {
        recordHeight = !recordHeight;
    }
    
    public void onWeightCheckBoxClick(View view) {
        recordWeight = !recordWeight;
    }
    
    private static final int MIN_HEIGHT = 100;
    
    private static final int MAX_HEIGHT = 220;
    
    private void addHeightPicker() {
        TextView height = findViewById(R.id.health_stats_setup_height_label);
    
        ScaleValuePickerView heightPicker = findViewById(R.id.health_stats_height_picker);
        heightPicker.setMinValue(MIN_HEIGHT);
        heightPicker.setMaxValue(MAX_HEIGHT);
        heightPicker.setScaleStep(1);
        heightPicker.setScaleDivisionStep(5);
        heightPicker.setDerivedScaleWidth(4);
        heightPicker.setInitialValue(100);
    
        height.setText((int) heightPicker.getInitialValue() + "cm");
    
        heightPicker.setOnValueChangedListener(newValue -> height.setText((int) newValue + "cm"));
    }
    
    private static final int MIN_WEIGHT = 40;
    
    private static final int MAX_WEIGHT = 200;
    
    private void addWeightPicker() {
        TextView weight = findViewById(R.id.health_stats_setup_weight_label);
        
        ScaleValuePickerView weightPicker = findViewById(R.id.health_stats_weight_picker);
        weightPicker.setMinValue(MIN_WEIGHT);
        weightPicker.setMaxValue(MAX_WEIGHT);
        weightPicker.setScaleStep(1);
        weightPicker.setScaleDivisionStep(10);
        weightPicker.setDerivedScaleWidth(4);
        weightPicker.setInitialValue(MIN_WEIGHT);
        
        weight.setText((int) weightPicker.getInitialValue() + "kg");
        
        weightPicker.setOnValueChangedListener(newValue -> weight.setText((int) newValue + "kg"));
    }
    
}
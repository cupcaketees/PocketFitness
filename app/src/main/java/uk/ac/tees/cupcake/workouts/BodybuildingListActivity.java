package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout Activity Class
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingListActivity extends AppCompatActivity {

    /**
     * Stores workout types in an Array list
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Bodypart> bodyparts = new ArrayList<>();

        bodyparts.add(new Bodypart("Arms", "Bicep, tricep and forearm exercises", R.drawable.temp_man_running));

        bodyparts.add(new Bodypart("Back", "Workouts for upper and lower back", R.drawable.temp_man_running));

        bodyparts.add(new Bodypart("Chest", "Chest workouts", R.drawable.temp_man_running));

        bodyparts.add(new Bodypart("Leg", "Leg workouts", R.drawable.temp_man_running));

        bodyparts.add(new Bodypart("Shoulder", "Shoulder workouts", R.drawable.temp_man_running));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingPartsAdapter(bodyparts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
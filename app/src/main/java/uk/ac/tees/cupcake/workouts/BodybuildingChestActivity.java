package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout Chest Activity Class
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingChestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Barbell Bench Press", "Lie back on a flat bench holding a barbell in the rack above you with a shoulder-width, overhand grip. Lift the bar off the rack and position it above your chest with arms fully extended. From the starting position, breathe in and lower the bar slowly until it skims the middle of your chest.", R.drawable.temp_man_running, "3", "Hard"));

        workouts.add(new Workout("Flat Bench Dumbbell Press", "Lie on a flat bench and hold a dumbbell in one hand, extending your arm until it's straight and the weight is in line with your shoulder. Slowly bend your arm and lower the weight towards the side of your chest. Return to starting position and repeat.", R.drawable.temp_man_running, "5", "Easy"));

        workouts.add(new Workout("Incline Dumbbell Press", "Sit on the incline bench holding the dumbbells in each hand. Press up with both hands, just as you would in a standard incline press. Pause and hold the weights above your chest. Lower the weight in one hand down slowly, counting for 2 beats until its about 2 inches away from your chest.", R.drawable.temp_man_running, "1", "Medium"));

        workouts.add(new Workout("Bar Dip", "Grab the bars of a dip station with your palms facing inward and your arms straight. Slowly lower until your elbows are at right angles, ensuring they stay tucked against your body and don't flare out. Drive yourself back up to the top and repeat.", R.drawable.temp_man_running, "5", "Hard"));

        workouts.add(new Workout("Incline Dumbbell Fly", "Hold a dumbbell in each hand and lie on an incline bench. Start with your arms extended directly above you and then slowly lower them out to the side, keeping a slight bend at the elbow.", R.drawable.temp_man_running, "5", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

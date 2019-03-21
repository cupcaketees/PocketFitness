package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout Arm Activity Class
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingArmsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Cocentration Curl", "Sit down on bench and rest your right arm against your right leg, letting the weight hang down. Curl the weight up, pause, then lower. Repeat with the other arm.", R.drawable.temp_man_running, "3", "Medium"));

        workouts.add(new Workout("EZ-Bar Curl", "Hold the EZ bar in front of your thighs with an underhand, shoulder-width grip. As you breathe in, curl the bar until your hands are at your shoulders. Squeeze your bicep, then lower under control.", R.drawable.temp_man_running, "5", "Easy"));

        workouts.add(new Workout("Crossbody Hammer Curl", "Stand up straight with a dumbbell in each hand. Your hands should be down at your side with your palms facing in. While keeping your palms facing in and without twisting your arm, curl the dumbbell of the right arm up towards your left shoulder as you exhale.", R.drawable.temp_man_running, "1", "Easy"));

        workouts.add(new Workout("Cable Rope Pushdown", "Attach a rope handle to the high pulley of a cable station. Keeping your elbows tucked in at your sides grab the handle, tense your core, and bring your hands down until your arms are fully extended, then return to the starting position. Only your forearms should move.", R.drawable.temp_man_running, "5", "Easy"));

        workouts.add(new Workout("Skullcrusher", "Load an EZ bar and grab it with your hands turned in toward each other. Lie back on a bench with your arms straight and perpendicular to your torso. (You can also start with your upper arms angled back slightly toward your head, which stretches the triceps even more.) Set your feet on the floor. Bend your elbows and slowly lower the bar toward your forehead, stopping just short of catastrophe. Straighten your arms and repeat.", R.drawable.temp_man_running, "5", "Hard"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
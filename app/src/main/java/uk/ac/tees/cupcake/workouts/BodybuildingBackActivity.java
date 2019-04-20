package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout Back Activity Class
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingBackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        setTitle("Back Workouts");

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Close Grip Lat Pulldowns", "Sit down at a lat pulldown machine or kneel in front of the cable machine and face away. Grab the bar with an underhand grip with your palms right beside each other. Pull the bar down to your chest, then return slowly to the start position. Your torso should remain static throughout.", R.drawable.temp_man_running, "3", "Easy"));

        workouts.add(new Workout("Barbell Deadlift", "Squat down and grasp a barbell with your hands roughly shoulder-width apart. Keep your chest up, pull your shoulders back and look straight ahead as you lift the bar. Focus on taking the weight back onto your heels and keep the bar as close as possible to your body at all times. Lift to thigh level, pause, then return under control to the start position.", R.drawable.temp_man_running, "5", "Medium"));

        workouts.add(new Workout("Wide-Grip Seated Cable Row", "Bend your knees and hold the bar with an overhand grip, wider than shoulder-width apart. Lean back slightly, keeping your back straight, then use your back muscle to pull the bar towards your belly button. Return the bar to starting position and repeat.", R.drawable.temp_man_running, "1", "Easy"));

        workouts.add(new Workout("Single-Arm Dumbbell Row", "Bend forward at the hips, keeping your back straight, and rest one hand on the bench. Pick up the dumbbell with the other hand. Start with your arm hanging straight down from your shoulder. Pull the dumbbell up to the side of your torso without rotating your shoulders.", R.drawable.temp_man_running, "5", "Easy"));

        workouts.add(new Workout("Standing T-Bar Row", "Load one side of a barbell with weight and secure the other end in a corner of the gym. Straddle the bar and grip it at the weighted end. Pull the bar towards your chest, keeping your elbows tight to your body and squeezing your shoulder blades together at the top. Lower to the starting position and repeat.", R.drawable.temp_man_running, "5", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
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
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingShouldersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Seated Bent-Over Rear Delt Fly", "Sit down, lean forward and hold a dumbbell in either hand so that they're resting above your feet. Stay bent forward as you raise your arms to the side, lining the dumbbells with your shoulders. Bring the weights back down and repeat.", R.drawable.temp_man_running, "3", "Easy"));

        workouts.add(new Workout("Dumbbell Shoulder Press", "Standing holding a dumbbell in each hand at shoulder height, palms facing forward. One at a time, raise each arm to push the weight up until it is fully extended. Lower to the start position and repeat. Keep your core tensed throughout.", R.drawable.temp_man_running, "5", "Easy"));

        workouts.add(new Workout("Cable Lateral Raise", "Attach a handle to the lowest pulley and stand to one side of the cable machine. Grab the handle with one hand and the tower with the other, to brace yourself. With your elbow slightly bent, raise your arm to to the side until your elbow is at shoulder height. Lower and repeat.", R.drawable.temp_man_running, "1", "Easy"));

        workouts.add(new Workout("Barbell Rear Delt Row", "Grab a barbell with an overhand grip, hands slightly wider than shoulder width apart. With your legs slightly bent, keep your back perfectly straight and bend your upper body forward until it’s almost perpendicular to the floor. From here row the weight upwards into the lower part of your chest. Pause. And return under control to the start position.", R.drawable.temp_man_running, "5", "Medium"));

        workouts.add(new Workout("Dumbbell Shrug", "Hold a dumbbell in each hand by your side with your palms facing inwards. Raise your shoulders as high as you can, then lower them back down and repeat.", R.drawable.temp_man_running, "5", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

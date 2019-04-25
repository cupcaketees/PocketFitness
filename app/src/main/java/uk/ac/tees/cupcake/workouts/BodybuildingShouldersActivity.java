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

        workouts.add(new Workout("Smith Machine Shoulder Press", "Position body so bar is in front and sit in good body alignment (abs tight, chest up, back straight). Grip bar so hands remain over elbow joints during exercise. Just before execution, rotate bar to release from latch and press (palms forward) over head, with elbows bent and upper arms vertical. In a controlled motion, lower bar toward chest, bending elbows as far as possible without compromising form. Hands should be over the elbow joints. While maintaining the controlled motion, press to starting position. Do not allow muscles to relax before next repetition.",  "http://www.makeoverfitness.com/images/seated-machine-press.gif", "Easy"));

        workouts.add(new Workout("Dumbbell Shoulder Press", "Standing holding a dumbbell in each hand at shoulder height, palms facing forward. One at a time, raise each arm to push the weight up until it is fully extended. Lower to the start position and repeat. Keep your core tensed throughout.", "http://www.makeoverfitness.com/images/seated-dumbbell-press.gif", "Easy"));

        workouts.add(new Workout("Dumbbell Lateral Raise", "Stand holding a pair of dumbbells at your sides with an overhand grip, your elbows slightly bent. Raise your arms up and out to the side until they're parallel to the floor, keeping the same bend in your elbows. Pause, then slowly return to the starting position.",  "http://www.makeoverfitness.com/images/dumbbell-lateral-raise.gif", "Easy"));

        workouts.add(new Workout("Seated Barbell Military Press", " Grab a barbell with a full, overhand grip that's shoulder width or a little wider. Press the bar overhead until your arms are straight but not locked. Slowly return to the starting position.",  "http://www.makeoverfitness.com/images/barbell-military-press.gif", "Medium"));

        workouts.add(new Workout("Barbell Upright Row", "Grab a barbell with a false, overhand grip that's shoulder width or a little wider Stand and let the bar hang at arm's length in front of your thighs. Pull the bar up to your lower chest, or until your upper arms are parallel to the floor. Pause, then slowly return to the starting position.", "http://www.makeoverfitness.com/images/upright-row-barbell.gif", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

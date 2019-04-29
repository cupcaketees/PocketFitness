package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
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

        setTitle("Arm Workouts");

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        workouts.add(new Workout("Cocentration Curl", "Instructions: \n" + "Sit down on bench and rest your right arm against your right leg, letting the weight hang down. Curl the weight up, pause, then lower. Repeat with the other arm.",  "http://www.makeoverfitness.com/images/concentration-curls.gif", "Difficulty: Medium"));

        workouts.add(new Workout("Standing Cable Curls", "Instructions: \n" + "Stand, facing cable, in good body alignment (abs tight, chest up, back straight) with feet in comfortable position. Keeping arms at side, grip bar (palms forward) slightly wider than shoulder width. In a controlled motion, keeping upper arm perpendicular to the floor, curl bar up. Contract biceps fully, without compromising form. While maintaining the controlled motion, return bar to starting position.", "http://www.makeoverfitness.com/images/cable-curls.gif", "Difficulty: Easy"));

        workouts.add(new Workout("Barbell Preacher Curl", "Instructions: \n" + "Sit on preacher bench placing back of arms on pad. The seat should be adjusted to allow the arm pit to rest near the top of the pad. Grasp curl bar with shoulder width underhand grip. Raise the bar until forearms are perpendicular to floor with the back of the upper arm remaining on the pad. Lower the barbell until arm is fully extended.", "http://www.makeoverfitness.com/images/preacher-curls-barbell.gif", "Difficulty: Easy"));

        workouts.add(new Workout("Cable Rope Pushdown", "Instructions: \n" + "Attach a rope handle to the high pulley of a cable station. Keeping your elbows tucked in at your sides grab the handle, tense your core, and bring your hands down until your arms are fully extended, then return to the starting position. Only your forearms should move.", "http://www.makeoverfitness.com/images/curvy-pushdowns.gif", "Difficulty: Easy"));

        workouts.add(new Workout("Machine Tricep Extension","Instructions: \n" +  "Extend your arms straight out moving only your forearms and hands. Hold, and slightly squeeze your triceps. Return the weight back to the starting point and repeat.", "http://www.makeoverfitness.com/images/machine-tricep-extensions.gif", "Difficulty: Easy"));

        workouts.add(new Workout("Barbell Overhead Tricep Extension","Instructions: \n" +  "Grasp a barbell and press it overhead. Keeping your upper arms as close to your head as possible, lower the bar until it almost touches the base of your neck. Press back up until your arms are conpletely straight.", "http://www.makeoverfitness.com/images/standing-barbell-tricep-extensions.gif", "Difficulty: Hard"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}
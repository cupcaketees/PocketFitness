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
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        workouts.add(new Workout("Bent Over Row", "Instructions: \n" + "Grab a barbell with an overhand grip that's just beyond shoulder-width and hold it at arm's length. Stand with your feet shoulder-width apart and knees slightly bent. Bend at the hips, lowering your torso about 45 degrees, and let the bar hang straight down from your shoulders. Pull the bar up to your torso, pause, then slowly lower it.  ", "http://www.makeoverfitness.com/images/bent-over-row.gif", "Difficulty: Hard"));

        workouts.add(new Workout("Close-Grip Seated Cable Row", "Instructions: \n" + "Bend your knees and hold the bar with an overhand grip, wider than shoulder-width apart. Lean back slightly, keeping your back straight, then use your back muscle to pull the bar towards your belly button. Return the bar to starting position and repeat.", "http://www.makeoverfitness.com/images/seated-machine-rows.gif", "Difficulty: Easy"));

        workouts.add(new Workout("T-Bar Row", "Instructions: \n" + "Keep your feet firmly planted on the platform and bend forward at the hips keeping your back flat and knees bent.Grasp the lever/T-bar with both hands.Keep your head as high as possible and keep your torso parallel to the floor.Fully extend your arms.Pull the lever/T-bar until it touches your chest remembering to keep your torso parallel to the ground.Slowly lower the lever/bar until your arms straight. Repeat movement.", "http://www.makeoverfitness.com/images/t-bar-rows.gif", "Difficulty: Easy"));

        workouts.add(new Workout("Back Extensions", "Instructions: \n" + "Lock feet in, with pad comfortably on hips. Cross arms over chest. If using weight, hold weight plate at chest level. Pressing hips into the pad, relax the spine. Squeeze together (retract) shoulder blades. In a controlled motion, lift head and shoulders up until spine is comfortably hyperextended.",  "http://www.makeoverfitness.com/images/curvy-extensions.gif", "Difficulty: Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
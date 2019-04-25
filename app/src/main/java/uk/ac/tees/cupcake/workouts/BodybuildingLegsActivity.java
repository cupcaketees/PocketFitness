package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout Legs Activity Class
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingLegsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        setTitle("Leg Workouts");

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Leg Press", "In the leg press machine position your feet shoulder-width apart on the platform and raise until your legs are outstretched without locking your knees. Slowly lower the platform until your knees are at 90 degrees to the floor, then push back to the start position through your heels.", "http://www.makeoverfitness.com/images/stories/curvy-women-leg-exercises.gif", "Easy"));

        workouts.add(new Workout("Barbell Squat", "Position a barbell on the back of the shoulders and grasp bar to the sides. Put your feet at shoulder width with your toes and knees slightly pointed outwards. Descend until knees and hips are fully bent. Extend knees and hips until legs are straight. Return and repeat. ",  "http://www.makeoverfitness.com/images/barbell-squats.gif", "Medium"));

        workouts.add(new Workout("Leg Extensions", "Sit in a leg extension machine with your ankles against the lower pad. Use your quad to push forward and straighten your leg in front of you, then return to the start position and repeat on the other side.", "http://www.makeoverfitness.com/images/leg-extensions.gif", "Easy"));

        workouts.add(new Workout("Lying Leg Curl", "Lie face down on the leg curl machine with the pad resting just under your calves. Grab the side handles and keep your body flat to the bench as you curl your legs up as far as possible. Lower and repeat.", "http://www.makeoverfitness.com/images/curvy-leg-curls.gif", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

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

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Leg Press", "In the leg press machine position your feet shoulder-width apart on the platform and raise until your legs are outstretched without locking your knees. Slowly lower the platform until your knees are at 90 degrees to the floor, then push back to the start position through your heels.", R.drawable.temp_man_running, "3", "Easy"));

        workouts.add(new Workout("Barbell Squat", "Stand with your feet more than shoulder-width apart and hold a barbell across your upper back with an overhand grip – avoid resting it on your neck. Hug the bar into your traps to engage your upper back muscles. Slowly sit back into a squat with head up, back straight and backside out. Lower until your hips are aligned with your knees, with your legs at 90 degrees – a deeper squat will be more beneficial but get the strength and flexibility first. Drive your heels into the floor to push yourself explosively back up. Keep form until you’re stood up straight: that’s one.", R.drawable.temp_man_running, "5", "Medium"));

        workouts.add(new Workout("Leg Extensions", "Sit in a leg extension machine with your ankles against the lower pad. Use your quad to push forward and straighten your leg in front of you, then return to the start position and repeat on the other side.", R.drawable.temp_man_running, "1", "Easy"));

        workouts.add(new Workout("Standing Calf Raises", "Stand upright holding two dumbbells by your sides. Place the balls of your feet on an exercise step or weight plate with your heels touching the floor. With your toes pointing forwards, raise your heels off the floor and contract your calves. Slowly return to the starting position.", R.drawable.temp_man_running, "5", "Medium"));

        workouts.add(new Workout("Lying Leg Curl", "Lie face down on the leg curl machine with the pad resting just under your calves. Grab the side handles and keep your body flat to the bench as you curl your legs up as far as possible. Lower and repeat.", R.drawable.temp_man_running, "5", "Easy"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

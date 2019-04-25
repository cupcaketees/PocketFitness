package uk.ac.tees.cupcake.workouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import uk.ac.tees.cupcake.R;

/**
 * Bodybuilding Workout chest Activity Class
 *
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingChestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bodybuilding_bodyparts_list);

        setTitle("chest Workouts");

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);
        ArrayList<Workout> workouts = new ArrayList<>();

        workouts.add(new Workout("Barbell Bench Press", "Lie back on a flat bench holding a barbell in the rack above you with a shoulder-width, overhand grip. Lift the bar off the rack and position it above your chest with arms fully extended. From the starting position, breathe in and lower the bar slowly until it skims the middle of your chest.", "http://www.makeoverfitness.com/images/flat-bench-press.gif", "Hard"));

        workouts.add(new Workout("Flat Bench Dumbbell Press", "Lie on a flat bench and hold a dumbbell in one hand, extending your arm until it's straight and the weight is in line with your shoulder. Slowly bend your arm and lower the weight towards the side of your chest. Return to starting position and repeat.", "http://www.makeoverfitness.com/images/dumbbell-bench-press.gif", "Easy"));

        workouts.add(new Workout("Push Ups", "Kneel down and place hands slightly wider than shoulder width. Keeping legs straight, push body up. Keep abs tight and back straight. Keep arms extended and in line with the chest. Shoulders can be slightly squeezed together (retracted). Throughout motion, shoulders form 90 degree angle to body. In a controlled motion, start lowering body until upper arms are approximately parallel to floor. Elbows must remain over hands. While maintaining the controlled motion, push body up to starting position without locking out elbows. Do not allow muscles to relax before next repetition.",  "http://www.makeoverfitness.com/images/push-ups.gif", "Medium"));

        workouts.add(new Workout("Close Grip Push Ups", "Keeping body straight, lower body to floor by bending arms. Push body up until arms are extended. Repeat. ",  "http://www.makeoverfitness.com/images/narrow-grip-push-ups.gif", "Hard"));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingWorkoutAdapter(workouts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}

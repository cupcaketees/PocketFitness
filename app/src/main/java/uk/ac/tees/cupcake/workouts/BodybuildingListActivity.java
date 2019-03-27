package uk.ac.tees.cupcake.workouts;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

/**
 * Bodybuilding Workout Activity Class
 * @author Michael Small <s6065431@tees.ac.uk>
 */
public class BodybuildingListActivity extends NavigationBarActivity {

    private ArrayList<Bodypart> bodyparts = new ArrayList<>();

    @Override
    protected int layoutResource() {
        return R.layout.bodybuilding_bodyparts_list;
    }

    @Override
    public void setup() {

        setTitle("Bodybuilding Workouts");

        RecyclerView recyclerView = findViewById(R.id.bodyparts_view);

        bodyparts.add(new Bodypart("Arms", "Biceps: This large muscle of the upper arm is formally known as the biceps brachii muscle, and rests on top of the humerus bone. It rotates the forearm and also flexes the elbow. Triceps: This large muscle in the back of the upper arm helps straighten the arm. It is formally known as the triceps brachii muscle.", R.drawable.arms, BodybuildingArmsActivity.class));

        bodyparts.add(new Bodypart("Back", "Soft tissues around the spine also play a key role in the health of the back. A large, complex group of muscles work together to support the trunk and hold the body upright.", R.drawable.back,BodybuildingBackActivity.class));

        bodyparts.add(new Bodypart("Chest", "The pectoralis major and minor make up the pecs or chest muscle. The chest muscles are predominantly used to move and control the arm.", R.drawable.chest, BodybuildingChestActivity.class));

        bodyparts.add(new Bodypart("Legs", "The anterior muscles, such as the quadriceps femoris, iliopsoas, and sartorius, work as a group to flex the thigh at the hip and extend the leg at the knee.", R.drawable.legs,BodybuildingLegsActivity.class));

        bodyparts.add(new Bodypart("Shoulders", "The muscles in the shoulder aid in a wide range of movement and help protect and maintain the main shoulder joint, known as the glenohumeral joint.", R.drawable.shoulders, BodybuildingShouldersActivity.class));

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter adapter = new BodybuildingPartsAdapter(bodyparts, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
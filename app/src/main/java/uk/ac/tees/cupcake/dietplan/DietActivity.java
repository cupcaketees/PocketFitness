package uk.ac.tees.cupcake.dietplan;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.DietListAdapter;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

public class DietActivity extends NavigationBarActivity {
    private static final String TAG = "DietActivity";

    @Override
    public void setup() {

        stub.setLayoutResource(R.layout.activity_video_list);
        stub.inflate();
        setupDays();
    }

    public void setupDays() {
        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);
        ArrayList<Diet> mDiets = new ArrayList<>();
//        for(int i = 0;  i <= 30; i++) {
//            mDiets.add(new Diet("Day: " + i));
//        }
        mDiets.add(new Diet("Day: 1","MUSHROOM BARLEY SOUP", "kcal: 174.4", "70 Minutes","Mexican penne with avocado","kcal: 495", "30 Minutes","Lean turkey burger with sweet potato wedges", "kcal: 428", "40 Minutes"));
        mDiets.add(new Diet("Day: 2","FISHNSH", "kcal: 1234.4", "702 Minutes","PIZZA ON TOAST","kcal: 42295", "330 Minutes","CHOCKLATE CAKE", "kcal: 4228", "0 Minutes"));

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new DietListAdapter(mDiets, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}

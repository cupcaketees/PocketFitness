package uk.ac.tees.cupcake.dietplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;

/**
 * DietDetails Activity
 *
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class DietDetailsActivity extends AppCompatActivity {
    private static final String TAG = "DietDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);
        setTitle("30 Day Diet");
        populateView();

    }

    /**
     * Populate the view in the details page showing all Diet information.
     */
    public void populateView() {
        Diet mDiets = getIntent().getParcelableExtra("DIET");
        Log.d(TAG, "populateView: Stored Diet" + mDiets.toString());
        TextView mDay = findViewById(R.id.foodDay);
        TextView mStarter = findViewById(R.id.foodStarter);
        TextView mStarterDesc = findViewById(R.id.foodStarterDesc);
        TextView mStarterTime = findViewById(R.id.foodStarter_time);
        TextView mLunch = findViewById(R.id.foodLunch);
        TextView mLunchDesc = findViewById(R.id.foodLunchDesc);
        TextView mLunchTime = findViewById(R.id.foodLunch_time);
        TextView mDinner = findViewById(R.id.foodDinner);
        TextView mDinnerDesc = findViewById(R.id.foodDinnerDesc);
        TextView mDinnerTime = findViewById(R.id.foodDinner_time);

        mDay.setText(mDiets.getDay());
        mStarter.setText(mDiets.getStarter());
        mStarterDesc.setText(mDiets.getStarterDisc());
        mStarterTime.setText(mDiets.getStarterTime());
        mLunch.setText(mDiets.getLunch());
        mLunchDesc.setText(mDiets.getLunchDesc());
        mLunchTime.setText(mDiets.getLunchTime());
        mDinner.setText(mDiets.getDinner());
        mDinnerDesc.setText(mDiets.getDinnerDesc());
        mDinnerTime.setText(mDiets.getDinnerTime());
    }

}

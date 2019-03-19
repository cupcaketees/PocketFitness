package uk.ac.tees.cupcake.dietplan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;

public class DietDetailsActivity extends NavigationBarActivity {
    private static final String TAG = "DietDetailsActivity";

    private DrawerLayout layout;

    @Override
    public void setup() {
        stub.setLayoutResource(R.layout.activity_food_description);
        stub.inflate();

        populateView();
    }

    public void populateView() {

        Diet mDiets = getIntent().getParcelableExtra("DIET");

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

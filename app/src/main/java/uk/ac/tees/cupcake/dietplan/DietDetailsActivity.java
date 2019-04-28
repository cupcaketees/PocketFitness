package uk.ac.tees.cupcake.dietplan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

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

        ImageView button = findViewById(R.id.exitDiet);

        button.setOnClickListener(v -> {
            finish();
        });
        populateView();

    }

    /**
     * Populate the view in the details page showing all Diet information.
     */
    public void populateView() {
        TextView title = findViewById(R.id.food_detail_title);
        Diet mDiets = getIntent().getParcelableExtra("DIET");
        Log.d(TAG, "populateView: Stored Diet" + mDiets.toString());
        title.setText(mDiets.getDay());

        TextView mStarter = findViewById(R.id.foodStarter);
        TextView mStarterDesc = findViewById(R.id.foodStarterDesc);
        TextView mStarterTime = findViewById(R.id.foodStarter_time);
        TextView mLunch = findViewById(R.id.foodLunch);
        TextView mLunchDesc = findViewById(R.id.foodLunchDesc);
        TextView mLunchTime = findViewById(R.id.foodLunch_time);
        TextView mDinner = findViewById(R.id.foodDinner);
        TextView mDinnerDesc = findViewById(R.id.foodDinnerDesc);
        TextView mDinnerTime = findViewById(R.id.foodDinner_time);

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

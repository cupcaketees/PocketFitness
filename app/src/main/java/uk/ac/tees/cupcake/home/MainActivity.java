package uk.ac.tees.cupcake.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.home.health.exercise.ExerciseSelectionActivity;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateActivity;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class MainActivity extends NavigationBarActivity {

    private ViewPager viewPager;

    private BottomNavigationViewEx bottomNavigationView;

    private final SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(),
            Arrays.asList(new HomeFragment(), new NewsFeedFragment(), new ProfileFragment()));

    @Override
    protected int layoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void setup() {
        if (!PermissionCheck.checkPermissionsList(Permissions.PERMISSIONS, getApplicationContext())) {
            PermissionCheck.verifyPermissions(Permissions.PERMISSIONS, MainActivity.this);
        }

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setupWithViewPager(viewPager);

        Bundle extras = getIntent().getExtras();

        // Checks if the fragment choice is specified and sends user to that page.
        if (extras != null) {
            setPage(extras.getInt("index"));
        }
    }

    /**
     * Sets the current page to the page associated with the given id.
     *
     * @param pageId the page id to go to.
     */
    public void setPage(int pageId) {
        viewPager.setCurrentItem(pageId, true);
        bottomNavigationView.setCurrentItem(pageId);
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    
    /**
     * Ensures no matter how the user gets to the page it resets the menu to the correct menu item highlighted.
     * Closes drawer when reaching this page
     */
    @Override
    protected void onResume() {
        super.onResume();
        
        navigationView.getMenu().getItem(0).setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        viewPager.addOnPageChangeListener(adapter);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
        viewPager.removeOnPageChangeListener(adapter);
    }
    
    @Override
    public void onBack() {
        if (viewPager.getCurrentItem() == 0) {
            finish();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    public void goToHeartRate(View view) {
        startActivity(new Intent(view.getContext(), HeartRateActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
    
    public void goToExerciseSelection(View view) {
        startActivity(new Intent(view.getContext(), ExerciseSelectionActivity.class));
    }

}
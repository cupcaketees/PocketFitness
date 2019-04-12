package uk.ac.tees.cupcake.home;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import java.util.Arrays;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateActivity;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class MainActivity extends NavigationBarActivity {

    private ViewPager viewPager;

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
        
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setupWithViewPager(viewPager);
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
        Intent intent = new Intent(view.getContext(), HeartRateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        view.getContext().startActivity(intent);
    }
}
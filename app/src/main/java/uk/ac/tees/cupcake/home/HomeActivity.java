package uk.ac.tees.cupcake.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.utils.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";

    private DrawerLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);
        
        layout = findViewById(R.id.drawerLayout);
        initialiseView();
        setupFragments();
        Log.d(TAG, "onCreate: onEnd");
    }

    /**
     * Initialises Toolbar and Drawer sets the Toolbar for the View
     */
    public void initialiseView() {
        Log.d(TAG, "initialiseView: onStart");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        layout.addDrawerListener(toggle);
        
        toggle.syncState();
        Log.d(TAG, "initialiseView: Navigation Bar and Toolbar added");

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Log.d(TAG, "initialiseView: onEnd");
    }

    /**
     * Setup fragments by calling SectionPagerAdapter
     * sets up bottom navigation bar with viewpager to move to each fragment.
     */
    private void setupFragments() {
        Log.d(TAG, "setupFragments: onStart");

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);
        
        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_NavBar);
        bottomNavigationView.setupWithViewPager(viewPager);
        
        Log.d(TAG, "setupFragments: onEnd");
    }
    
    /**
     * @param item - that was selected (NavigationView)
     * @return - true if successful and takes to new view.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected: onStart");

        switch (item.getItemId()) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            default:
                Log.d(TAG, "onNavigationItemSelected: Error no item Selected");
                return false;
        }
        
        layout.closeDrawer(GravityCompat.START);
        return true;
    }

}

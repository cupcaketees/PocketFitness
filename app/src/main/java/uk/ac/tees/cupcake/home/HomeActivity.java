package uk.ac.tees.cupcake.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.VideoPlayer.NavigationDrawerAdapter;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.utils.SectionsPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private DrawerLayout layout;
    NavigationView navigationView;
    ViewPager viewPager;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        mAuth = FirebaseAuth.getInstance();
        /*
         * Checks if user is signed in and updates UI accordingly.
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    sendUserToLoginActivity();
                }
            }
        };

        layout = findViewById(R.id.drawerLayout);
        viewPager = findViewById(R.id.container);

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
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        layout.addDrawerListener(toggle);

        toggle.syncState();
        Log.d(TAG, "initialiseView: Navigation Bar and Toolbar added");


        navigationView.setNavigationItemSelectedListener(new NavigationDrawerAdapter(HomeActivity.this));
        Log.d(TAG, "initialiseView: onEnd");
    }

    /**
     * Setup fragments by calling SectionPagerAdapter
     * sets up bottom navigation bar with viewpager to move to each fragment.
     */
    private void setupFragments() {
        Log.d(TAG, "setupFragments: onStart");

        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());


        viewPager.setAdapter(adapter);

        BottomNavigationViewEx bottomNavigationView = findViewById(R.id.bottom_NavBar);
        bottomNavigationView.setupWithViewPager(viewPager);

        Log.d(TAG, "setupFragments: onEnd");
    }

    /*
     * Send user to login activity.
     */
    private void sendUserToLoginActivity() {
        Intent homeIntent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(homeIntent);
        finish();
    }


    /**
     * Ensures no matter how the user gets to the page it resets the menu to the correct menu item highlighted.
     * Closes drawer when reaching this page
     */
    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
        layout.closeDrawer(GravityCompat.START);
    }

    /**
     * Closes drawers when back button is pressed if its open.
     * If its on main menu button and back button is pressed it closes app if any other fragment it'll return to home page.
     */
    @Override
    public void onBackPressed() {
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            if(viewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    }

}

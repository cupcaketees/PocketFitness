package uk.ac.tees.cupcake.videoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import java.util.ArrayList;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.NavigationDrawerAdapter;


/**
 * VideoList Activity
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class VideoListActivity extends AppCompatActivity {
    private static final String TAG = "VideoListActivity";

    DrawerLayout layout;
    ViewPager viewPager;

    /**
     * Temporarily stores videos in an Array list in future will get from db.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        layout = findViewById(R.id.drawerLayout);
        viewPager = findViewById(R.id.container);

        RecyclerView mRecyclerView = findViewById(R.id.myRecycleView);
        ArrayList<Video> mVideos = new ArrayList<>();
        mVideos.add(new Video("Warm up", "The warming up is a preparation for physical exertion or a performance by exercising or practising gently beforehand.", R.drawable.temp_stretch, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675453105001_5675442412001.mp4", "7.26"));

        mVideos.add(new Video("Aerobic Cardio", "Aerobic exercise is physical exercise of low to high intensity that depends primarily on the aerobic energy-generating process.",
                R.drawable.temp_man_running, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675479616001_5675461827001.mp4", "12.11"));

        mVideos.add(new Video("Body Conditioning", "exercises that increase your strength, speed, endurance or any other physical attribute",
                R.drawable.temp_weight, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5675476796001_5675439266001.mp4", "14.50"));

        mVideos.add(new Video("Core Focus", "This beginner abs workout is fast and effective." +
                "Following a balanced core routine and starting with modified version of complex exercises will help you to build up your exercise confidence as you build up your abs.",
                R.drawable.temp_situp, "link", "https://httpsak-a.akamaihd.net/4005328972001/4005328972001_5468369588001_5401238075001.mp4", "7.15"));

        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView.Adapter mAdapter = new VideoListAdapter(mVideos, getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        initialiseView();
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
        navigationView.getMenu().getItem(2).setChecked(true);
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerAdapter(getApplicationContext()));
        Log.d(TAG, "initialiseView: onEnd");
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
            super.onBackPressed();
        }
    }
}

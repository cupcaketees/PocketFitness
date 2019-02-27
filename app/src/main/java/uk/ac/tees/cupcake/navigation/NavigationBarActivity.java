package uk.ac.tees.cupcake.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewStub;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.VideoPlayer.VideoPlayerActivity;
import uk.ac.tees.cupcake.navigation.navitemactions.NavigationItemOnClickAction;
import uk.ac.tees.cupcake.navigation.navitemactions.StartIntentNavigationItemAction;

/**
 * An {@link Activity} that includes a navigation bar.
 *
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class NavigationBarActivity extends AppCompatActivity {
    
    protected ViewStub stub;
    
    public static final Map<Integer, NavigationItemOnClickAction> NAV_BAR_ACTIONS = new HashMap<>();
    
    static {
        /**
         * This entry will not exist in release because uri is determined at run time
         */
        Map<String, String> uri = new HashMap<>();
        uri.put("VIDEO_NAME", "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4");
        
        NAV_BAR_ACTIONS.put(R.id.nav_slideshow, new StartIntentNavigationItemAction<>(VideoPlayerActivity.class, uri));
    }
    
    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);
        
        stub = findViewById(R.id.layout_stub);
        
        addNavigationView();
        setup();
    }
    
    /**
     * Sets up and adds the navigation view.
     */
    private void addNavigationView() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        NavigationView navigationView = findViewById(R.id.nav_view);
    
        setSupportActionBar(toolbar);
    
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        
        drawerLayout.addDrawerListener(toggle);
    
        toggle.syncState();
    
        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectionListener(this, NAV_BAR_ACTIONS));
    }
    
    /**
     * This method is called in the {@link #onCreate}
     */
    public abstract void setup();
    
}
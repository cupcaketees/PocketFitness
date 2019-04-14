package uk.ac.tees.cupcake.navigation;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SettingsActivity;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.food.SearchFoodActivity;
import uk.ac.tees.cupcake.dietplan.DietActivity;
import uk.ac.tees.cupcake.healthnews.NewsActivity;
import uk.ac.tees.cupcake.home.MainActivity;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.navigation.navitemactions.NavigationItemOnClickAction;
import uk.ac.tees.cupcake.navigation.navitemactions.StartIntentNavigationItemAction;
import uk.ac.tees.cupcake.posts.PostActivity;
import uk.ac.tees.cupcake.videoplayer.VideoListActivity;
import uk.ac.tees.cupcake.workouts.BodybuildingListActivity;

/**
 * An {@link Activity} that includes a navigation bar.
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public abstract class NavigationBarActivity extends AppCompatActivity {
    
    protected ViewStub stub;
    
    protected NavigationView navigationView;
    
    protected DrawerLayout drawerLayout;
    
    public static final Map<Integer, NavigationItemOnClickAction> NAV_BAR_ACTIONS = new HashMap<>();

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;

    static {
        NAV_BAR_ACTIONS.put(R.id.nav_home, new StartIntentNavigationItemAction(MainActivity.class));
        
        NAV_BAR_ACTIONS.put(R.id.nav_workout_videos, new StartIntentNavigationItemAction(VideoListActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_bodybuilding, new StartIntentNavigationItemAction(BodybuildingListActivity.class));

        NAV_BAR_ACTIONS.put(R.id.news_list_view, new StartIntentNavigationItemAction(NewsActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_post, new StartIntentNavigationItemAction(PostActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_30diet, new StartIntentNavigationItemAction(DietActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_settings, new StartIntentNavigationItemAction(SettingsActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_search_food, new StartIntentNavigationItemAction(SearchFoodActivity.class));

        NAV_BAR_ACTIONS.put(R.id.nav_signout, c -> FirebaseAuth.getInstance().signOut());
    }

    /**
     * Adds listener on activity
     */
    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    /**
     * Removes listener on activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);

        stub = findViewById(R.id.layout_stub);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        addNavigationView();

        stub.setLayoutResource(layoutResource());
        stub.inflate();

        setup();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(NavigationBarActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        setNameAndImage();
    }
    /**
     * Creates path to current user document. (User Uid is key.)
     * On success if the document snapshot exists gets users profile name, last name and image url values and sets them to textView and ImageView on nav bar.
     */
    private void setNameAndImage() {

        mFirestore.collection("Users")
                  .document(mAuth.getCurrentUser().getUid())
                  .get()
                  .addOnSuccessListener(documentSnapshot -> {
                      if(documentSnapshot.exists()){
                          // Initialise
                          TextView profileNameTextView = findViewById(R.id.nav_bar_name_text_view);
                          CircleImageView profilePictureImageView = findViewById(R.id.nav_bar_profile_picture_image_view);

                          // Get Values
                          UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                          // Set Values
                          String profileName = profile.getFirstName() + " " + profile.getLastName();
                          profileNameTextView.setText(profileName);

                          if (profile.getProfilePictureUrl() != null) {

                              Picasso.with(NavigationBarActivity.this)
                                     .load(profile.getProfilePictureUrl())
                                     .into(profilePictureImageView);
                          }
                      }
                  })
                  .addOnFailureListener(e -> Toast.makeText(NavigationBarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    /**
     * Sends user to profile fragment.
     */
    public void navProfileNameOnClick(View view){

        Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.putExtra("fragmentChoice", 2);
        startActivity(intent);
    }

    @LayoutRes
    protected abstract int layoutResource();

    /**
     * Sets up and adds the navigation view.
     */
    private void addNavigationView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
    
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
    
    /**
     * Invoked in {@link #onBackPressed()} after {@link #drawerLayout} has been closed.
     */
    public void onBack() {
        super.onBackPressed();
    }
    
    @Override
    public final void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            onBack();
        }
    }
}
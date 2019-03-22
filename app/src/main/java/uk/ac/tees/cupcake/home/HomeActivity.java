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
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.adapters.NavigationDrawerAdapter;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

public class HomeActivity extends AppCompatActivity {
    
    private static final String TAG = "HomeActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore firebaseFirestore;
    
    private DrawerLayout layout;
    private NavigationView navigationView;
    
    private ViewPager viewPager;
    
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: onStart");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navbar);
        
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        /*
         * Sends user to login page if current user is null.
         * Sends user to setup profile if account does not exist in firestore collection.
         * if it does exist it sets nav bar name text view and profile picture image view to users values.
         */
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
                } else {
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    firebaseFirestore.collection("Users")
                                     .document(currentUserId)
                                     .get()
                                     .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                         @Override
                                         public void onSuccess(DocumentSnapshot documentSnapshot) {
                                             if (documentSnapshot.exists()) {
                                                 TextView navBarProfileNameTextView = findViewById(R.id.nav_bar_name_text_view);
                                                 CircleImageView profilePictureImageView = findViewById(R.id.nav_bar_profile_picture_image_view);
                                                 UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                                                 navBarProfileNameTextView.setText(profile.getFirstName() +" " + profile.getLastName());

                                                 if(profile.getProfilePictureUrl() != null){
                                                     Picasso.with(HomeActivity.this)
                                                            .load(profile.getProfilePictureUrl())
                                                            .into(profilePictureImageView);
                                                 }
                                             }else{
                                                 Intent setupIntent = new Intent(HomeActivity.this, SetupProfileActivity.class);
                                                 startActivity(setupIntent);
                                                 finish();
                                             }
                                         }
                                     });
                }
            }
        };
        layout = findViewById(R.id.drawer_layout);
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
        
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerAdapter(getApplicationContext()));
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

    /*
     * Signs out user
     * //TODO add google auth signout
     */
    @Override
    public void onBackPressed() {
        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
        } else {
            if (viewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                viewPager.setCurrentItem(0);
            }
        }
    }
}
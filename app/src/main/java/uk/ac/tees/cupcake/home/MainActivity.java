package uk.ac.tees.cupcake.home;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import java.util.Arrays;

import uk.ac.tees.cupcake.home.health.ExerciseSelectionActivity;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateActivity;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class MainActivity extends NavigationBarActivity {

    private ViewPager viewPager;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    
    private final SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager(),
            Arrays.asList(new HomeFragment(), new NewsFeedFragment(), new ProfileFragment()));
    
    /**
     * Sends user to login page if they are not logged in.
     * Sends user to setup profile if account does not exist in firestore collection.
     */
    private FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            // if current user is null. user send to login activity.
            if(firebaseAuth.getCurrentUser() == null){
                IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
            }else{
                String currentUserUid = firebaseAuth.getCurrentUser().getUid();
                // If user document is not found they are sent to setup profile activity.
                firestore.collection("Users")
                         .document(currentUserUid)
                         .get()
                         .addOnFailureListener(e -> {
                             IntentUtils.invokeBaseView(MainActivity.this, SetupProfileActivity.class);
                             finish();
                         });
            }
        }
    };

    /**
     * Adds auth listener on activity
     */
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    /**
     * Removes auth listener on activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        auth.removeAuthStateListener(authListener);
    }

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
        startActivity(new Intent(view.getContext(), HeartRateActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
    
    public void goToExerciseSelection(View view) {
        startActivity(new Intent(view.getContext(), ExerciseSelectionActivity.class));
        finish();
    }

}
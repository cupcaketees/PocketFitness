package uk.ac.tees.cupcake.home;

import android.support.v4.view.GravityCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.account.UserProfile;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.home.health.heartrate.HeartRateActivity;
import uk.ac.tees.cupcake.login.LoginActivity;
import uk.ac.tees.cupcake.navigation.NavigationBarActivity;
import uk.ac.tees.cupcake.utils.IntentUtils;

/**
 * @author Sam-Hammersley <q5315908@tees.ac.uk>
 */
public class MainActivity extends NavigationBarActivity {

    private ViewPager viewPager;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    /**
     * Sends user to login page if current user is null.
     * Sends user to setup profile if account does not exist in firestore collection.
     * if it does exist it sets nav bar name text view and profile picture image view to users values.
     */
    private final FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
        if (firebaseAuth.getCurrentUser() == null) {
            IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
        } else {
            String currentUserId = auth.getCurrentUser().getUid();
            firestore.collection("Users")
                    .document(currentUserId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                TextView navBarProfileNameTextView = findViewById(R.id.nav_bar_name_text_view);
                                CircleImageView profilePictureImageView = findViewById(R.id.nav_bar_profile_picture_image_view);
                                UserProfile profile = documentSnapshot.toObject(UserProfile.class);

                                navBarProfileNameTextView.setText(profile.getFirstName() + " " + profile.getLastName());

                                if (profile.getProfilePictureUrl() != null) {
                                    Picasso.with(MainActivity.this)
                                            .load(profile.getProfilePictureUrl())
                                            .into(profilePictureImageView);
                                }
                            } else {
                                IntentUtils.invokeBaseView(MainActivity.this, SetupProfileActivity.class);
                                finish();
                            }
                        }
                    });
        }
    };
    
    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    protected int layoutResource() {
        return R.layout.activity_main;
    }

    @Override
    public void setup() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BODY_SENSORS}, 0);
        }
        
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(new HomeFragment(), new NewsFeedFragment(), new ProfileFragment())));
        
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
    }
    
    @Override
    public void onBack() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBack();
        } else {
            viewPager.setCurrentItem(0);
        }
    }

    public void goToHeartRate(View view) {
        IntentUtils.invokeBaseView(view.getContext(), HeartRateActivity.class);
    }

}
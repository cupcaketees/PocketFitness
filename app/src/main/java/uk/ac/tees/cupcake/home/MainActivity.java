package uk.ac.tees.cupcake.home;

import android.support.v4.view.ViewPager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.account.SetupProfileActivity;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
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
    
    private final FirebaseAuth.AuthStateListener authListener = firebaseAuth -> {
        if (firebaseAuth.getCurrentUser() == null) {
            IntentUtils.invokeBaseView(getApplicationContext(), LoginActivity.class);
        } else {
            String currentUserId = auth.getCurrentUser().getUid();
            
            firestore
                .collection("Users")
                .document(currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().exists()) {
                        IntentUtils.invokeBaseView(MainActivity.this, SetupProfileActivity.class);
                        finish();
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
    }
    
    @Override
    public void onBack() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(0);
        }
    }
    
}
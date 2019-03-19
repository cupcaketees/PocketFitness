package uk.ac.tees.cupcake.posts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.PostPagerAdapter;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private static final int ACTIVITY_NUM = 2;
    private ViewPager mViewPager;
    private DrawerLayout mLayout;

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        mViewPager = findViewById(R.id.container);
        if (PermissionCheck.checkPermissionsList(Permissions.PERMISSIONS, getApplicationContext())) {
            setupFragments();
        } else {
            PermissionCheck.verifyPermissions(Permissions.PERMISSIONS, PostActivity.this);
        }

        setupFragments();

        mLayout = findViewById(R.id.drawer_layout);
    }

    /**
     * @return the current fragment number
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    private void setupFragments() {
        Log.d(TAG, "setupFragments: started");

        PostPagerAdapter postPagerAdapter = new PostPagerAdapter(getSupportFragmentManager());

        Toolbar toolbar = findViewById(R.id.postToolbar);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(postPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.bottom_tab);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");

    }

}

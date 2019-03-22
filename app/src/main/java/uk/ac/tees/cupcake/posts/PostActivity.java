package uk.ac.tees.cupcake.posts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;
import uk.ac.tees.cupcake.utils.PermissionCheck;
import uk.ac.tees.cupcake.utils.Permissions;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_create);
        mViewPager = findViewById(R.id.container);

        setupFragments();

    }

    /**
     * @return the current fragment number
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    private void setupFragments() {
        Log.d(TAG, "setupFragments: started");

        SectionsPagerAdapter postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
                Arrays.asList(new GalleryFragment(), new CameraFragment()));

        Toolbar toolbar = findViewById(R.id.postToolbar);
        setSupportActionBar(toolbar);

        mViewPager.setAdapter(postPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.bottom_tab);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("GALLERY");
        tabLayout.getTabAt(1).setText("PHOTO");

    }

}

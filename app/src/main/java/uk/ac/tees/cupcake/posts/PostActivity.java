package uk.ac.tees.cupcake.posts;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.Arrays;

import uk.ac.tees.cupcake.R;
import uk.ac.tees.cupcake.adapters.SectionsPagerAdapter;

/**
 *@author Hugo Tomas <s6006225@live.tees.ac.uk>
 *Sets up the fragments for creating a post.
 */
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

    /**
     * Creates a tab layout on the bottom that can navigate to the three fragments.
     */
    private void setupFragments() {
//        Log.d(TAG, "setupFragments: started");
//
//        SectionsPagerAdapter postPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),
//                Arrays.asList(new GalleryFragment(), new CameraFragment(), new TextFragment()));
//
//        Toolbar toolbar = findViewById(R.id.postToolbar);
//        setSupportActionBar(toolbar);
//
//        mViewPager.setAdapter(postPagerAdapter);
//
//        TabLayout tabLayout = findViewById(R.id.bottom_tab);
//        tabLayout.setupWithViewPager(mViewPager);
//        tabLayout.getTabAt(0).setText("GALLERY");
//        tabLayout.getTabAt(1).setText("PHOTO");
//        tabLayout.getTabAt(2).setText("TEXT");

    }

}

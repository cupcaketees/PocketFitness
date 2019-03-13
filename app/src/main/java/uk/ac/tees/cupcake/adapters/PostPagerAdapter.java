package uk.ac.tees.cupcake.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import uk.ac.tees.cupcake.posts.CameraFragment;
import uk.ac.tees.cupcake.posts.GalleryFragment;

public class PostPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionPagerAdapter";

    private final List<Fragment> POST_FRAGMENTS = Arrays.asList(new GalleryFragment(), new CameraFragment());


    public PostPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position of the selected item in navigation bar
     * @return selected fragment
     */
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: getting fragment at position: " + position);

        return POST_FRAGMENTS.get(position);
    }

    /**
     * @return amount of fragments
     */
    @Override
    public int getCount() {
        return POST_FRAGMENTS.size();
    }

}


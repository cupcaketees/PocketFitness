package uk.ac.tees.cupcake.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.Arrays;
import java.util.List;

import uk.ac.tees.cupcake.HomeFragment;
import uk.ac.tees.cupcake.NewsFeedFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = "SectionPagerAdapter";

    private final List<Fragment> fragments = Arrays.asList(new HomeFragment(), new NewsFeedFragment());


    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * @param position of the selected item in navigation bar
     * @return selected fragment
     */
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: getting fragment at position: " + position);
        return fragments.get(position);
    }


    /**
     * @return amount of fragments
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

}
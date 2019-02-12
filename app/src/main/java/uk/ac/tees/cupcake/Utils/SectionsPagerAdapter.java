package uk.ac.tees.cupcake.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

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
     * @param position
     * @return selected fragment
     */
    @Override
    public Fragment getItem(int position) {


        return fragments.get(position);
    }


    /**
     * @return amount of fragments
     */
    @Override
    public int getCount() {
        return fragments.size();
    }


    /**
     * add Fragments manually (TBD if necessary)
     */
    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
}
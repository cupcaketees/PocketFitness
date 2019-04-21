package uk.ac.tees.cupcake.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

import uk.ac.tees.cupcake.home.OnChangeFragment;

/**
 * SectionPager Adapter
 * @author Hugo Tomas <s6006225@live.tees.ac.uk>
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final String TAG = "SectionPagerAdapter";

    private final List<Fragment> fragments;

    public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
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
    
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    
    }
    
    @Override
    public void onPageSelected(int position) {
        Fragment fragment = fragments.get(position);
        
        if (fragment instanceof OnChangeFragment) {
            ((OnChangeFragment) fragment).onChange();
        }
    }
    
    @Override
    public void onPageScrollStateChanged(int state) {
    
    }
}
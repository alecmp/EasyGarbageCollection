package com.alessandro.easygarbagecollection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public void SectionsPagerAdapter() {

    }

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a ClusterFragment (defined as a static inner class below).

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new EGCMapFragment();

                break;
            case 1:
                fragment = new BlankFragment();
                break;
            default:
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
        }
        return null;
    }
}

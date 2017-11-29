package com.alessandro.easygarbagecollection;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

 class SectionsPagerAdapter extends FragmentPagerAdapter {


     SectionsPagerAdapter(FragmentManager fm) {
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
                fragment = new ListFragment();
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
                return "Map";
            case 1:
                return "List";

            default: return "Map";
        }
    }
}

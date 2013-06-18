package io.r.a.dio;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment = null;
        if (i == 0) {
            fragment = new LastPlayedFragment();
        } else if (i == 1) {
            fragment = new HomeFragment();
        } else if (i == 2) {
            fragment = new QueueFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Last Played";
        } else if (position == 1) {
            return "Home";
        } else if (position == 2) {
            return "Queue";
        }
        return "";
    }
}


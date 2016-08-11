package com.jerry.bus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jerry.bus.fragment.BackTabFragment;
import com.jerry.bus.fragment.OutTabFragment;
import com.jerry.bus.fragment.TabFragment1;
import com.jerry.bus.fragment.TabFragment2;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                OutTabFragment tab1 = new OutTabFragment();
                //TabFragment1 tab1 = new TabFragment1();
                return tab1;
            case 1:
                BackTabFragment tab2 = new BackTabFragment();
                //TabFragment2 tab2 = new TabFragment2();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}

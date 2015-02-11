package com.smallow.android.news.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by smallow on 2015/2/11.
 */
public class ListFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;

    public ListFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> mFragments) {
        super(fragmentManager);
        this.mFragments = mFragments;

    }

    @Override
    public Fragment getItem(int position) {
        if (mFragments != null && mFragments.size() > 0)
            return mFragments.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (mFragments != null && mFragments.size() > 0)
            return mFragments.size();
        return 0;
    }
}

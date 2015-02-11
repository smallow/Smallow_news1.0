package com.smallow.android.news.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallow.android.news.R;
import com.smallow.android.news.adapter.ListFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends BaseFragment {

    private ViewPager viewPager;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private List<Fragment> fragmentList;
    private int[] ids = {R.id.category_1, R.id.category_2, R.id.category_3, R.id.category_4, R.id.category_5};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    protected void onInitWidgets(View rootView, Bundle savedInstanceState) {
        initFragments();
        initViewPager();
    }

    private void initFragments() {
        fragmentList = new ArrayList<Fragment>();
        CategoryFragment categoryFragment1 =CategoryFragment.newInstance("11");
        CategoryFragment categoryFragment2 =CategoryFragment.newInstance("41");
        CategoryFragment categoryFragment3 =CategoryFragment.newInstance("14");
        CategoryFragment categoryFragment4 =CategoryFragment.newInstance("71");
        CategoryFragment categoryFragment5 =CategoryFragment.newInstance("70");
        fragmentList.add(categoryFragment1);
        fragmentList.add(categoryFragment2);
        fragmentList.add(categoryFragment3);
        fragmentList.add(categoryFragment4);
        fragmentList.add(categoryFragment5);
    }
    private void initViewPager() {
        viewPager = findViewById(R.id.news_viewPager);
        viewPager.setOffscreenPageLimit(1);
        fragmentPagerAdapter = new ListFragmentPagerAdapter(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                reSetTitleColor();
                ((TextView)findViewById(ids[i])).setTextColor(Color.parseColor("#ffffffff"));
                ((TextView)findViewById(ids[i])).setBackgroundColor(Color.parseColor("#ff4d9eff"));
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void reSetTitleColor() {
        for(int i=0;i<ids.length;i++){
            ((TextView)findViewById(ids[i])).setTextColor(Color.parseColor("#ffaaaaaa"));
            ((TextView)findViewById(ids[i])).setBackgroundColor(Color.parseColor("#ffffffff"));
        }
    }


    @Override
    protected void lazyLoad() {
    }
}

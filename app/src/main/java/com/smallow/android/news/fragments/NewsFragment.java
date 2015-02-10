package com.smallow.android.news.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallow.android.news.R;

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
        viewPager = findViewById(R.id.news_viewPager);
        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return fragmentList.get(i);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        };
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

    private void initFragments() {
        fragmentList = new ArrayList<Fragment>();
        CategoryFragment categoryFragment1 = new CategoryFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("title", "重要新闻");
        bundle1.putString("categoryCode", "11");
        categoryFragment1.setArguments(bundle1);

        CategoryFragment categoryFragment2 = new CategoryFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("title", "财经新闻");
        bundle2.putString("categoryCode", "41");
        categoryFragment2.setArguments(bundle2);


        CategoryFragment categoryFragment3 = new CategoryFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("title", "体育新闻");
        bundle3.putString("categoryCode", "14");
        categoryFragment3.setArguments(bundle3);


        CategoryFragment categoryFragment4 = new CategoryFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putString("title", "科技新闻");
        bundle4.putString("categoryCode", "71");
        categoryFragment4.setArguments(bundle4);


        CategoryFragment categoryFragment5 = new CategoryFragment();
        Bundle bundle5 = new Bundle();
        bundle5.putString("title", "娱乐新闻");
        bundle5.putString("categoryCode", "70");
        categoryFragment5.setArguments(bundle5);
        fragmentList.add(categoryFragment1);
        fragmentList.add(categoryFragment2);
        fragmentList.add(categoryFragment3);
        fragmentList.add(categoryFragment4);
        fragmentList.add(categoryFragment5);


    }
}

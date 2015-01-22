package com.smallow.android.news;

import android.app.Activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallow.android.news.adapter.ViewPagerAdpter;

import java.util.ArrayList;
import java.util.List;


public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private ViewPagerAdpter adpter;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids = {R.id.id_pager1, R.id.id_pager2, R.id.id_pager3};
    private TextView tvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        initView();
        initDots();
    }


    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.welcome_1, null));
        views.add(inflater.inflate(R.layout.welcome_2, null));
        views.add(inflater.inflate(R.layout.welcome_3, null));
        viewPager = (ViewPager) findViewById(R.id.id_viewPager);
        adpter = new ViewPagerAdpter(views, this);
        viewPager.setAdapter(adpter);
        viewPager.setOnPageChangeListener(this);
        tvStart= (TextView) views.get(2).findViewById(R.id.tv_start);
        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initDots() {
        dots = new ImageView[views.size()];
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) findViewById(ids[i]);
        }
    }




    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < ids.length; i++) {
            if (position == i) {
                dots[i].setImageResource(R.drawable.login_point_selected);
            } else {
                dots[i].setImageResource(R.drawable.login_point);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}

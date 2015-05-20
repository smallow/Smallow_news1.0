package com.smallow.community.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.smallow.community.R;
import com.smallow.community.adapter.ViewPagerAdpter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smallow on 2015/5/20.
 */
public  class SlideShowView extends FrameLayout implements ViewPager.OnPageChangeListener{

    //轮播图图片数量

    private final static int IMAGE_COUNT = 5;

    //自动轮播的时间间隔

    private final static int TIME_INTERVAL = 5;

    //自动轮播启用开关

    private final static boolean isAutoPlay = true;


    //自定义轮播图的资源ID

    private int[] imagesResIds;

    //放轮播图片的ImageView 的list

    private List<View> imageViewsList;

    //放圆点的View的list

    private List<View> dotViewsList;


    private ViewPager viewPager;

    //当前轮播页

    private int currentItem = 0;
    private ViewPagerAdpter adpter;

    public SlideShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.slide_show_view, this, true);
        imageViewsList=new ArrayList<View>();
        if(imagesResIds!=null && imagesResIds.length>0){
            for(int imageID : imagesResIds){
                ImageView view =  new ImageView(context);
                view.setImageResource(imageID);
                view.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViewsList.add(view);
            }
        }

        dotViewsList = new ArrayList<View>();
        dotViewsList.add(findViewById(R.id.v_dot1));
        dotViewsList.add(findViewById(R.id.v_dot2));
        dotViewsList.add(findViewById(R.id.v_dot3));
        dotViewsList.add(findViewById(R.id.v_dot4));
        dotViewsList.add(findViewById(R.id.v_dot5));

        viewPager = (ViewPager) findViewById(R.id.id_viewPager);
        adpter = new ViewPagerAdpter(imageViewsList,context);
        viewPager.setFocusable(true);
        viewPager.setAdapter(adpter);
        viewPager.setOnPageChangeListener(this);
    }

    public  void initData(){
        imagesResIds = new int[]{
                R.drawable.pic1,
                R.drawable.pic2,
                R.drawable.pic3,
                R.drawable.pic4,
                R.drawable.pic5,
        };
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotViewsList.size(); i++) {
            if (position == i) {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.login_point_selected);
            } else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.login_point);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

package com.smallow.community.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by smallow on 2015/1/22.
 */
public class ViewPagerAdpter extends PagerAdapter {

    private Context context;
    private List<ImageView> list;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;


    public ViewPagerAdpter(List<ImageView> list, Context context,ImageLoader imageLoader,DisplayImageOptions options) {
        this.list = list;
        this.context = context;
        this.imageLoader=imageLoader;
        this.options=options;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = list.get(position);
        imageLoader.displayImage(imageView.getTag() + "", imageView,options);
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return (view == o);
    }
}

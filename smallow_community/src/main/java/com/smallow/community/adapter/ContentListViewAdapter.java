package com.smallow.community.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smallow.common.AsyncImageLoader;
import com.smallow.community.R;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smallow on 2015/5/20.
 */
@Deprecated
public class ContentListViewAdapter extends InnerBaseAdapter<Content> {
    //public ImageLoader imageLoader; //用来下载图片的类，后面有介绍



    // 异步加载图片的线程
    private AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
    //当前的缓存
    private Map<Integer, View> viewMap = new HashMap<Integer, View>();



    public ContentListViewAdapter(List<Content> data,Context context) {
        setData(data, false);
        asyncImageLoader=new AsyncImageLoader();
    }

    public void notifyDataSetChanged(List<Content> data) {
        setData(data,true);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_listview_item,null);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.title);
            holder.tvDescription = (TextView) convertView
                    .findViewById(R.id.description);
            holder.tvMoney = (TextView) convertView
                    .findViewById(R.id.money);
            holder.titleImg= (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Content bean=getData(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvDescription.setText(bean.getDescription());
        holder.tvMoney.setText("￥"+bean.getMoney());
        String titleImgUrl=bean.getTitleImg();
        ImageView imageView = holder.getTitleImg();
        Drawable cachedImage = asyncImageLoader.loadDrawable(Api.conParam+titleImgUrl,imageView, new AsyncImageLoader.ImageCallback() {
            @Override
            public void imageLoaded(Drawable imageDrawable, ImageView imageView, String imageUrl) {
                imageView.setImageDrawable(imageDrawable);
            }
        });
        if (cachedImage == null) {
            imageView.setImageResource(R.drawable.pic1);
        }else{
            imageView.setImageDrawable(cachedImage);
        }

        return convertView;
    }




    private class ViewHolder {
        TextView tvTitle;
        TextView tvMoney;
        TextView tvDescription;
        ImageView titleImg;


        public ImageView getTitleImg(){
            return titleImg;
        }
    }
}


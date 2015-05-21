package com.smallow.community.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smallow.common.ImageLoader;
import com.smallow.community.R;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;

import java.util.List;

/**
 * Created by smallow on 2015/5/20.
 */
public class ContentListViewAdapter extends InnerBaseAdapter<Content> {
    public ImageLoader imageLoader; //用来下载图片的类，后面有介绍

    public ContentListViewAdapter(List<Content> data,Context context) {
        setData(data, false);
        imageLoader=new ImageLoader(context);
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
        String titleImg=bean.getTitleImg();
        int index=titleImg.lastIndexOf("/");
        String titleImgName=titleImg.substring(index+1,titleImg.length());
        //imageLoader.DisplayImage(titleImgName,holder.titleImg);
        holder.titleImg.setImageResource(R.drawable.pic2);
        return convertView;
    }




    private class ViewHolder {
        TextView tvTitle;
        TextView tvMoney;
        TextView tvDescription;
        ImageView titleImg;
    }
}


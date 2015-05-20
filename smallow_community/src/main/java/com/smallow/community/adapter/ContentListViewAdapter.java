package com.smallow.community.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smallow.community.R;
import com.smallow.community.bean.Content;

/**
 * Created by smallow on 2015/5/20.
 */
public class ContentListViewAdapter extends InnerBaseAdapter<Content> {


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_listview_item,null);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_content);
            convertView.setTag(holder);
        }

        return null;
    }




    private class ViewHolder {
        TextView tvTitle;
        TextView tvMoney;
        TextView tvDescription;
        ImageView titleImg;
    }
}


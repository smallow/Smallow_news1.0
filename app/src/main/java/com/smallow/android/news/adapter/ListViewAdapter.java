package com.smallow.android.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallow.android.news.GetNetJsonData.ItemAttri;
import com.smallow.android.news.R;
import com.smallow.android.news.entity.ContentBean;

import java.util.List;

/**
 * Created by smallow on 2015/1/28.
 */
public class ListViewAdapter extends InnerBaseAdapter<ContentBean> {



    public ListViewAdapter(List<ContentBean> data) {
        setData(data, false);
    }

    public void notifyDataSetChanged(List<ContentBean> data) {
        setData(data,true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_view, null);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_content);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ContentBean obj=getData(position);
        holder.tvTitle.setText(obj.getTitle());
        holder.tvTime.setText(obj.getPublishDate());
        holder.tvContent.setText(obj.getContent());
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;
    }
}

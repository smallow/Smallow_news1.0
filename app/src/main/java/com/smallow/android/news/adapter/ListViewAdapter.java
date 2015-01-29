package com.smallow.android.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallow.android.news.GetNetJsonData.ItemAttri;
import com.smallow.android.news.R;

import java.util.List;

/**
 * Created by smallow on 2015/1/28.
 */
public class ListViewAdapter extends InnerBaseAdapter<ItemAttri> {



    public ListViewAdapter(List<ItemAttri> data) {
        setData(data, false);
    }

    public void notifyDataSetChanged(List<ItemAttri> data) {
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

        holder.tvTitle.setText(getData(position).title);
        holder.tvTime.setText(getData(position).time);
        holder.tvContent.setText(getData(position).content);
        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;
    }
}

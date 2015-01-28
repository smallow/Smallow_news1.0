package com.smallow.android.news.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smallow.android.news.R;
import com.smallow.android.news.entity.ContentBean;

import java.util.List;

/**
 * Created by smallow on 2015/1/28.
 */
public class ListViewAdapter extends InnerBaseAdapter<ContentBean> {
    private List<ContentBean> mListData;
    private Context context;


    public ListViewAdapter(List<ContentBean> data, Context context) {
        this.context = context;
        mListData=data;
        setData(data, false);
    }


    @Override
    public void setData(List<ContentBean> data, boolean notifyDataSetChanged) {
        super.setData(mListData, notifyDataSetChanged);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_view, null);
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

        holder.tvTitle.setText(mListData.get(position).getAuthor());
        //holder.tvTime.setText(mListData.get(position).getId());
        holder.tvContent.setText(mListData.get(position).getTitle());

        return convertView;
    }

    private class ViewHolder {
        TextView tvTitle;
        TextView tvTime;
        TextView tvContent;
    }
}

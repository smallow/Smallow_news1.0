package com.smallow.community.adapter;


import android.database.DataSetObserver;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by smallow on 2015/5/19.
 */
public abstract class InnerBaseAdapter<Data> extends BaseAdapter {
    protected List<Data> mData;

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return getData(position);
    }

    public Data getData(int position) {
        if (mData != null && position >= 0 && position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }


    public void setData(List<Data> data, boolean notifyDataSetChanged) {
        mData = data;
        if (notifyDataSetChanged) {
            notifyDataSetChanged();
        }
    }


}

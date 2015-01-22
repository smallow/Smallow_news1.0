package com.smallow.android.news.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.smallow.android.news.R;

import org.w3c.dom.Text;

/**
 * Created by smallow on 2015/1/22.
 */
public class CategoryFragment extends BaseFragment {

    private String title;
    private String categoryCode;

    public CategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            categoryCode = bundle.getString("categoryCode");
        }
        return inflater.inflate(R.layout.news_category_layout, container, false);
    }

    @Override
    protected void onInitWidgets(View rootView, Bundle savedInstanceState) {
        ((TextView) findViewById(R.id.category_name)).setText(title + " : " + categoryCode);
    }
}

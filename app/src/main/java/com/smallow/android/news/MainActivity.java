package com.smallow.android.news;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.smallow.android.news.fragments.NewsFragment;

/**
 * 主程序入口
 */

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private Button btnNews;
    private Button btnSubject;
    private Button btnFind;
    private Button btnSetting;

    private NewsFragment newsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        fragmentManager = getSupportFragmentManager();
        setTabSelection(0);

    }

    private void initView() {
        btnNews = (Button) findViewById(R.id.tab_news);
        btnSubject = (Button) findViewById(R.id.tab_subject);
        btnFind = (Button) findViewById(R.id.tab_find);
        btnSetting = (Button) findViewById(R.id.tab_setting);
        btnNews.setOnClickListener(this);
        btnSubject.setOnClickListener(this);
        btnFind.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }


    private void setTabSelection(int i) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hiddenFragments(transaction);
        switch (i) {
            case 0:
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.id_content, newsFragment);
                } else {
                    transaction.show(newsFragment);
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

        transaction.commit();
    }

    private void hiddenFragments(FragmentTransaction transaction) {
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_news:
                setTabSelection(0);
                break;
            case R.id.tab_subject:
                setTabSelection(1);
                break;
            case R.id.tab_find:
                setTabSelection(2);
                break;
            case R.id.tab_setting:
                setTabSelection(3);
                break;
        }
    }
}

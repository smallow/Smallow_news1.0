package com.smallow.community;

import android.app.Activity;

import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.smallow.community.ui.DataStateBox;

import java.util.ArrayList;
import java.util.List;


public class MainActivity2 extends Activity {

    private ListView mListView;
    private ArrayAdapter mAdapter;// listView的适配器
    List<String> data = new ArrayList<String>();
    private DataStateBox stateView;
    private PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            mAdapter.notifyDataSetChanged();
            setListViewHeightBasedOnChildren(mListView);
            if(mAdapter.getCount()>0){
                stateView.setState(DataStateBox.State.HIDE);
            }else{
                stateView.setState(DataStateBox.State.EMPTY_DATA);
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        mListView= (ListView) findViewById(R.id.contentListView);
        mAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        mListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(mListView);
        stateView= (DataStateBox) findViewById(R.id.stateView);
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mScrollView=mPullRefreshScrollView.getRefreshableView();
        startLoadData();
    }

    private void startLoadData() {
        data.clear();
        if(mAdapter.getCount()==0){
            stateView.setState(DataStateBox.State.INIT_LOADING);
        }
        getRemoteData();
    }




    private void getRemoteData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<10;i++){
                    //data.add("测试:"+i);
                }
                try{
                    Thread.sleep(3000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                Message m = mHandler.obtainMessage();
                mHandler.sendMessage(m);
            }
        }).start();


    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}

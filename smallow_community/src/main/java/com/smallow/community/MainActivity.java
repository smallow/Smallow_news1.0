package com.smallow.community;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;
import com.smallow.community.ui.CommonTitleBar;
import com.smallow.community.ui.SlideShowView;
import com.smallow.community.ui.SubMenuWindow;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    private List<SubMenuWindow.SubMenu> subMenuData = new ArrayList<SubMenuWindow.SubMenu>();
    private ListView contentListView;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initSubMenuData();
        final CommonTitleBar bar1= (CommonTitleBar) findViewById(R.id.titleBar1);
        bar1.setOnCommonTitleBarOnClickListener(new CommonTitleBarOnClickLinstener() {
            @Override
            public void leftOnclick() {
                Toast.makeText(MainActivity.this,"上-左边",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void rightOnclick() {
               // Toast.makeText(MainActivity.this,"上-右边",Toast.LENGTH_SHORT).show();
                SubMenuWindow subMenuWindow=new SubMenuWindow(MainActivity.this);
                subMenuWindow.setMenuItem(subMenuData);
                subMenuWindow.showBelowAndRight(bar1);

            }
        });
        initContentListView();
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
                mPullRefreshScrollView.onRefreshComplete();
            }
        });

        mScrollView = mPullRefreshScrollView.getRefreshableView();
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // Do some stuff here

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshScrollView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }




    private void initContentListView() {
        contentListView= (ListView) findViewById(R.id.contentListView);
        contentListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getData()));
        setListViewHeightBasedOnChildren(contentListView);
    }
    private List<String> getData(){
        List<String> data = new ArrayList<String>();
        for(int i=1;i<100;i++){
            data.add("数据"+i);
        }
        return data;
    }


    private void initSubMenuData(){
        subMenuData.add(new MainSubMenuData("功能1"));
        subMenuData.add(new MainSubMenuData("功能2"));
        subMenuData.add(new MainSubMenuData("功能3"));
    }


    class MainSubMenuData implements SubMenuWindow.SubMenu{
        private String name;

        public MainSubMenuData(String name){
            this.name=name;
        }

        @Override
        public String getName() {
            return name;
        }
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

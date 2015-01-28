package com.smallow.android.news;

/**
 * Created by smallow on 2015/1/28.
 */
/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/


        import java.util.ArrayList;
        import java.util.List;

        import android.annotation.SuppressLint;
        import android.app.ListActivity;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.text.format.DateUtils;
        import android.util.Log;
        import android.view.ContextMenu;
        import android.view.ContextMenu.ContextMenuInfo;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView.AdapterContextMenuInfo;
        import android.widget.BaseAdapter;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.handmark.pulltorefresh.library.PullToRefreshBase;
        import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
        import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
        import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
        import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
        import com.handmark.pulltorefresh.library.PullToRefreshListView;
        import com.handmark.pulltorefresh.library.extras.SoundPullEventListener;
        import com.smallow.android.news.entity.ContentBean;


/**
 * ①布局文件中使用PullToRefreshListView，findViewById
 *
 * ②设置滑动模式
 *
 * ③设置刷新监听事件
 *
 * a、getCurrentMode 判断当前刷新的Mode：上拉、下拉
 *
 * b、上拉刷新只请求首页数据 toPage = 1、isMore = true
 *
 * c、下拉刷新 toPage++,根据isMore来提示显示信息
 *
 * ④异步线程请求数据
 *
 * a、有返回数据isMore=true;
 *
 * b、否则，isMore=false 并通知主线程
 *
 * ⑤异步线程完成 执行onRefreshComplete操作
 *
 */

@SuppressLint("HandlerLeak")
public final class PullToRefreshListActivity extends ListActivity {
    private final String TAG = "PullToRefreshListActivity";
    /** 首次网络请求页码 */
    private static final int FIRST_PAGE = 1;
    /** 数据请求页码 **/
    private int toPage = 1;
    /** 更多的网络数据 **/
    private boolean isMore = true;

    private List<ContentBean> mListData;// 存储网络数据

    private PullToRefreshListView mPullRefreshListView;
    private ListViewAdapter mAdapter;// listView的适配器

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ptr_list);

        initView();

        // 初始化监听
        initEvent();

        // ListView actualListView = mPullRefreshListView.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        // registerForContextMenu(actualListView);

        // 获取首页数据并设置listView
        mListData = new ArrayList<ContentBean>();
        new GetDataTask().execute(FIRST_PAGE);

    }

    private void initView() {
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        // 滑动模式设置为双向滑动
        mPullRefreshListView
                .setMode(mPullRefreshListView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START
                        : Mode.BOTH);

    }

    /**
     * 初始化监听事件
     */
    private void initEvent() {
        // 设置listView的滑动刷新监听
        mPullRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {

                        // 获取当前时间并格式化
                        String label = DateUtils.formatDateTime(
                                getApplicationContext(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);

                        // Update the LastUpdatedLabel
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);

                        if (PullToRefreshBase.Mode.PULL_FROM_START == mPullRefreshListView
                                .getCurrentMode()) {// 下拉刷新

                            mPullRefreshListView.getLoadingLayoutProxy()
                                    .setRefreshingLabel("请稍等...");
                            mPullRefreshListView.getLoadingLayoutProxy()
                                    .setPullLabel("下拉刷新...");
                            mPullRefreshListView.getLoadingLayoutProxy()
                                    .setReleaseLabel("松开自动刷新");

                            // 重置集合数据
                            mListData = new ArrayList<ContentBean>();
                            new GetDataTask().execute(FIRST_PAGE);

                            // 还原toPage初始值
                            toPage = 1;
                            // 还原上拉加载控制变量
                            isMore = true;

                        } else if (PullToRefreshBase.Mode.PULL_FROM_END == mPullRefreshListView
                                .getCurrentMode()) {// 上拉刷新

                            // 上拉刷新时，逐步加载新界面
                            toPage++;

                            if (isMore) {// 上一次请求有数据
                                // 自定义上拉header内容

                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setPullLabel("上拉刷新...");
                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setRefreshingLabel("正在为你加载更多赛程内容...");
                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setReleaseLabel("松开自动刷新...");
                            } else {
                                // 上一次请求已经没有数据了
                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setPullLabel("没有更多了...");
                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setRefreshingLabel("没有更多了...");
                                mPullRefreshListView.getLoadingLayoutProxy()
                                        .setReleaseLabel("没有更多了...");
                            }
                            new GetDataTask().execute(toPage);
                        }

                    }
                });

        // Add an end-of-list listener
        mPullRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        // listView最后一个item可见时触发
                        Toast.makeText(PullToRefreshListActivity.this,
                                "End of List!", Toast.LENGTH_SHORT).show();
                    }
                });

        /**
         * Add Sound Event Listener
         */
        //SoundPullEventListener<ListView> soundListener = new SoundPullEventListener<ListView>(
        //		this);
        //soundListener.addSoundEvent(State.PULL_TO_REFRESH, R.raw.pull_event);
        //soundListener.addSoundEvent(State.RESET, R.raw.reset_sound);
        //soundListener.addSoundEvent(State.REFRESHING, R.raw.refreshing_sound);
        //mPullRefreshListView.setOnPullEventListener(soundListener);

    }

    private class GetDataTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            // 本次请求的数据集合
            List<ContentBean> currData = new ArrayList<ContentBean>();
          //  currData = new GetNetJsonData().getDataFromJson(params[0]);
            if (!currData.isEmpty()) {
                // 有数据返回
                // 数据加入集合中
                mListData.addAll(currData);
            } else {
                // 没有数据
                isMore = false;
                // 向主线程发送通知
                mHandler.sendEmptyMessage(0);
                // 没有数据toPage--
                toPage--;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {

            if (mAdapter == null) {
                mAdapter = new ListViewAdapter();

                // You can also just use setListAdapter(mAdapter) or
                // mPullRefreshListView.setAdapter(mAdapter)
                mPullRefreshListView.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

            Log.i(TAG, "page：" + toPage);
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();// 完成刷新动作

            super.onPostExecute(v);
        }
    }

    /**
     * 接收子线程传递出来的信息
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(PullToRefreshListActivity.this, "没有更多了",
                    Toast.LENGTH_SHORT).show();
        }
    };

    private class ListViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(
                        R.layout.item_list_view, null);
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

           // holder.tvTitle.setText(mListData.get(position).title);
           /// holder.tvTime.setText(mListData.get(position).time);
           // holder.tvContent.setText(mListData.get(position).content);

            return convertView;
        }

        private class ViewHolder {
            TextView tvTitle;
            TextView tvTime;
            TextView tvContent;
        }

    }

}

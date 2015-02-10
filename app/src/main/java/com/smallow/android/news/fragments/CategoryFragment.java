package com.smallow.android.news.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.smallow.android.news.GetNetJsonData;
import com.smallow.android.news.R;
import com.smallow.android.news.SystemConst;
import com.smallow.android.news.adapter.ListViewAdapter;
import com.smallow.android.news.common.DataLoadControler;
import com.smallow.android.news.entity.ContentBean;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import com.smallow.android.news.GetNetJsonData.ItemAttri;
import com.smallow.android.news.utils.network.NetSpirit;
import com.smallow.android.news.utils.network.RequestReceiver;

/**
 * Created by smallow on 2015/1/22.
 */
public class CategoryFragment extends BaseFragment implements DataLoadControler<GetNetJsonData.ItemAttri>{
    private static final int req_type_refersh = 0;
    private static final int req_type_load_more = 1;
    private String title;
    private String categoryCode;
    private PullToRefreshListView mPullRefreshListView;
    /**
     * 首次网络请求页码
     */
    private static final int FIRST_PAGE = 1;
    /**
     * 数据请求页码 *
     */
    private int toPage = 1;
    /**
     * 更多的网络数据 *
     */
    private boolean isMore = true;

    private List<ContentBean> mListData;// 存储网络数据
    private ListViewAdapter mAdapter;// listView的适配器



    public CategoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString("title");
            categoryCode = bundle.getString("categoryCode");
        }else{
            categoryCode="11";
        }
        return inflater.inflate(R.layout.news_category_layout, container, false);
    }

    @Override
    protected void onInitWidgets(View rootView, Bundle savedInstanceState) {
        mListData = new ArrayList<ContentBean>();
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView
                .setMode(mPullRefreshListView.getMode() == PullToRefreshBase.Mode.BOTH ? PullToRefreshBase.Mode.PULL_FROM_START
                        : PullToRefreshBase.Mode.BOTH);
        initIndicator();
        mAdapter = new ListViewAdapter(mListData);
        mPullRefreshListView.setAdapter(mAdapter);
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //这里写下拉刷新的任务
                String label = DateUtils.formatDateTime(
                        getActivity().getApplicationContext(),
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);

                mListData = new ArrayList<ContentBean>();
                //new GetDataTask().execute(FIRST_PAGE);
                NetSpirit.getInstance().httpGet(getRefreshUrl(10),req_type_refersh,requestReceiver);
                // 还原toPage初始值
                toPage = 1;
                // 还原上拉加载控制变量
                isMore = true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //这里写上拉加载更多的任务
                // 上拉刷新时，逐步加载新界面
                toPage++;
                if (!isMore) {
                    // 上一次请求已经没有数据了
                    mPullRefreshListView.getLoadingLayoutProxy()
                            .setPullLabel("没有更多了...");
                    mPullRefreshListView.getLoadingLayoutProxy()
                            .setRefreshingLabel("没有更多了...");
                    mPullRefreshListView.getLoadingLayoutProxy()
                            .setReleaseLabel("没有更多了...");
                }
                //new GetDataTask().execute(toPage);
                NetSpirit.getInstance().httpGet(getLoadMoreUrl(toPage, 10),req_type_refersh,requestReceiver);
            }
        });


        mPullRefreshListView
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        // listView最后一个item可见时触发
                        /*Toast.makeText(getActivity(),
                                "End of List!", Toast.LENGTH_SHORT).show();*/
                    }
                });

        // 获取首页数据并设置listView
        //new GetDataTask().execute(FIRST_PAGE);
        NetSpirit.getInstance().httpGet(getRefreshUrl(10),req_type_refersh,requestReceiver);
    }


    private void initIndicator() {
        ILoadingLayout startLabels = mPullRefreshListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("请稍等正在刷新...");// 刷新时
        startLabels.setReleaseLabel("松开自动刷新");// 下来达到一定距离时，显示的提示
        ILoadingLayout endLabels = mPullRefreshListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("加载更多数据...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在加载数据...");// 刷新时
        endLabels.setReleaseLabel("松开加载完成...");// 下来达到一定距离时，显示的提示
    }

    /**
     * 接收子线程传递出来的信息
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Toast.makeText(getActivity(), "没有更多了", Toast.LENGTH_SHORT).show();
        }
    };

    /*private class GetDataTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            // 本次请求的数据集合
            List<GetNetJsonData.ItemAttri> currData = new ArrayList<GetNetJsonData.ItemAttri>();
            currData = new GetNetJsonData().getDataFromJson(params[0]);
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
           // Log.i(TAG, "mListDataSize：" + mListData.size());
            mAdapter.notifyDataSetChanged(mListData);
            //Log.i(TAG, "page：" + toPage);
            mPullRefreshListView.onRefreshComplete();// 完成刷新动作
            super.onPostExecute(v);
        }
    }*/


    @Override
    public String getLoadMoreUrl(int pageNo, int perReqeustDataLenght) {
        String url= SystemConst.prefix_data+"GetContentList.do?order=4&pageSize="+perReqeustDataLenght+"&channelIds="+categoryCode+"&pageNo="+pageNo;
        return url;
    }

    @Override
    public String getRefreshUrl(int perReqeustDataLenght) {
        String url=SystemConst.prefix_data+"GetContentList.do?order=4&pageSize="+perReqeustDataLenght+"&channelIds="+categoryCode+"&pageNo=1";
        return url;
    }

    @Override
    public List<ItemAttri> handlerParseData(String rawResp) {
        return null;
    }

    @Override
    public String getMaxId(ItemAttri itemAttri) {
        return null;
    }


    private RequestReceiver requestReceiver = new RequestReceiver(){
        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            List<ContentBean> currData = new ArrayList<ContentBean>();
            if(resp!=null && !resp.equals("")){
                JSONObject map= (JSONObject)JSON.parse(resp);

                JSONArray array=map.getJSONArray("list");
                int maxPageNum=map.getInteger("totalPage");

                if(toPage<=maxPageNum){
                    for (int i=0;i<array.size();i++) {
                        JSONObject jsonObj = (JSONObject) array.getJSONObject(i);
                        currData.add(new ContentBean(jsonObj.getInteger("id"),
                                jsonObj.getString("title"),
                                jsonObj.getString("author"),
                                jsonObj.getString("publishDate"),
                                jsonObj.getString("content")
                        ));
                    }
                }

            }


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

            mAdapter.notifyDataSetChanged(mListData);
            //Log.i(TAG, "page：" + toPage);
            mPullRefreshListView.onRefreshComplete();// 完成刷新动作
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    };

}

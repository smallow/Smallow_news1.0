package com.smallow.community;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.smallow.common.DataLoadControler;
import com.smallow.common.network.NetSpirit;
import com.smallow.common.network.RequestReceiver;
import com.smallow.common.utils.StrUtils;
import com.smallow.community.adapter.ContentListViewAdapter2;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;
import com.smallow.community.ui.CommonTitleBar;
import com.smallow.community.ui.SubMenuWindow;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends Activity  implements DataLoadControler<Content>{
    private List<SubMenuWindow.SubMenu> subMenuData = new ArrayList<SubMenuWindow.SubMenu>();
    private ListView contentListView;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    private List<Content> mListData;// 存储网络数据
    private ContentListViewAdapter2 mAdapter;


    /**首次网络请求页码*/
    private static final int FIRST_PAGE = 1;
    /**数据请求页码*/
    private int toPage = 1;
    /*** 更多的网络数据 **/
    private boolean isMore = true;
    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private long lastRequestId;
    private static final int req_type_refersh = 0;
    private static final int req_type_load_more = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initCommonTitleBar();
        initContentListView();
        initPullToRefreshScrollView();
        lazyLoad();
    }



    /**
     * 初始化一般顶部导航栏
     */
    private void initCommonTitleBar(){
        initSubMenuData();
        final CommonTitleBar bar1= (CommonTitleBar) findViewById(R.id.titleBar1);
        bar1.setOnCommonTitleBarOnClickListener(new CommonTitleBarOnClickLinstener() {
            @Override
            public void leftOnclick() {
                Toast.makeText(MainActivity.this,"上-左边",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void rightOnclick() {
                SubMenuWindow subMenuWindow=new SubMenuWindow(MainActivity.this);
                subMenuWindow.setMenuItem(subMenuData);
                subMenuWindow.showBelowAndRight(bar1);
            }
        });
    }
    private void initContentListView() {
        contentListView= (ListView) findViewById(R.id.contentListView);
        mListData=new ArrayList<Content>();
        //contentListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getData()));
        mAdapter=new ContentListViewAdapter2(mListData,MainActivity.this);
        contentListView.setAdapter(mAdapter);
        contentListView.setOnItemClickListener(mContentClickListener);
        setListViewHeightBasedOnChildren(contentListView);
    }


    private void initPullToRefreshScrollView(){
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                /** 下拉刷新*/
                String label = DateUtils.formatDateTime(MainActivity.this,
                        System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);
                // 显示最后更新的时间
                refreshView.getLoadingLayoutProxy()
                        .setLastUpdatedLabel(label);
                mListData = new ArrayList<Content>();
                NetSpirit.getInstance().httpGet(getRefreshUrl(10), req_type_refersh,requestReceiver);
                // 还原toPage初始值
                toPage = 1;
                // 还原上拉加载控制变量
                isMore = true;
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                /** 上拉加载分页*/
                toPage++;
                if (!isMore) {
                    // 上一次请求已经没有数据了
                    mPullRefreshScrollView.getLoadingLayoutProxy()
                            .setPullLabel("没有更多了...");
                    mPullRefreshScrollView.getLoadingLayoutProxy()
                            .setRefreshingLabel("没有更多了...");
                    mPullRefreshScrollView.getLoadingLayoutProxy()
                            .setReleaseLabel("没有更多了...");
                }
                NetSpirit.getInstance().httpGet(getLoadMoreUrl(toPage, 10),req_type_load_more,requestReceiver);
            }
        });
        mScrollView = mPullRefreshScrollView.getRefreshableView();
        isPrepared = true;
    }

    private void lazyLoad() {
        if (!isPrepared  || mHasLoadedOnce) {
            return;
        }
        lastRequestId=NetSpirit.getInstance().httpGet(getRefreshUrl(10), req_type_refersh,requestReceiver);
        mHasLoadedOnce = true;
    }



    private RequestReceiver requestReceiver = new RequestReceiver(){
        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            Message m = mHandler.obtainMessage();
            List<Content> currData=new ArrayList<Content>();
            if(resp!=null && !"".equals(resp)){
                JSONArray array= JSON.parseArray(resp);
                for (int i=0;i<array.size();i++) {
                    JSONObject jsonObj = (JSONObject) array.getJSONObject(i);
                    JSONObject _jsonObj=jsonObj.getJSONObject("merchant");
                    currData.add(new Content(jsonObj.getInteger("id"),_jsonObj.getString("name"), StrUtils.textCut(jsonObj.getString("title"), 35, "..."),String.valueOf(jsonObj.getFloat("concessionalPrice")),jsonObj.getString("titleImg")));
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
                m.arg1=201;
                mHandler.sendEmptyMessage(0);
                // 没有数据toPage--
                toPage--;
            }
            mAdapter.notifyDataSetChanged(mListData);
            setListViewHeightBasedOnChildren(contentListView);
            mPullRefreshScrollView.onRefreshComplete();
            mHandler.sendMessage(m);
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    };





    @Override
    public String getRefreshUrl(int perReqeustDataLenght) {
        String url=Api.conParam+Api.getTop10MerchantContent+"?page="+FIRST_PAGE+"&pageSize="+perReqeustDataLenght;
        return url;
    }

    @Override
    public String getLoadMoreUrl(int pageNo, int perReqeustDataLenght) {
        String url=Api.conParam+Api.getTop10MerchantContent+"?page="+pageNo+"&pageSize="+perReqeustDataLenght;
        return url;
    }







    private void initSubMenuData(){
        subMenuData.add(new MainSubMenuData("登录"));
        subMenuData.add(new MainSubMenuData("注册"));
        subMenuData.add(new MainSubMenuData("查看订单"));
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





    private AdapterView.OnItemClickListener mContentClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try{
                Content itemBean=mListData.get(position);
               // Toast.makeText(MainActivity.this,itemBean.getTitle()+" "+itemBean.getId()+" ",Toast.LENGTH_SHORT).show();
                if(itemBean!=null){
                    Intent intent=new Intent(MainActivity.this,ContentDetailAty.class);
                    intent.putExtra("contentId",itemBean.getId());
                    startActivity(intent);
                }
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
    };



    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };
}

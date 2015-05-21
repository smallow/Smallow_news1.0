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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.smallow.common.DataLoadControler;
import com.smallow.common.network.NetSpirit;
import com.smallow.common.network.RequestReceiver;
import com.smallow.common.utils.StrUtils;
import com.smallow.community.adapter.ContentListViewAdapter;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;
import com.smallow.community.ui.CommonTitleBar;
import com.smallow.community.ui.SlideShowView;
import com.smallow.community.ui.SubMenuWindow;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends Activity  implements DataLoadControler<Content>{
    private List<SubMenuWindow.SubMenu> subMenuData = new ArrayList<SubMenuWindow.SubMenu>();
    private ListView contentListView;
    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    private List<Content> mListData;// 存储网络数据
    private ContentListViewAdapter mAdapter;
    ExecutorService executorService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        executorService = Executors.newFixedThreadPool(5);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initCommonTitleBar();
        initContentListView();
        initPullToRefreshScrollView();
    }


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


    private void initPullToRefreshScrollView(){
        mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                NetSpirit.getInstance().httpGet(getRefreshUrl(10), Api.getTop10MerchantContentRid,getTop10MerchantContentReceiver);
            }
        });
        mScrollView = mPullRefreshScrollView.getRefreshableView();
    }











    private RequestReceiver getTop10MerchantContentReceiver = new RequestReceiver(){
        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            List<Content> currData=new ArrayList<Content>();
            if(resp!=null && !"".equals(resp)){
                JSONArray array= JSON.parseArray(resp);
                for (int i=0;i<array.size();i++) {
                    JSONObject jsonObj = (JSONObject) array.getJSONObject(i);
                    JSONObject _jsonObj=jsonObj.getJSONObject("merchant");
                    currData.add(new Content(jsonObj.getInteger("id"),_jsonObj.getString("name"), StrUtils.textCut(jsonObj.getString("title"), 35, "..."),String.valueOf(jsonObj.getFloat("concessionalPrice")),jsonObj.getString("titleImg")));
                    if(jsonObj.getString("titleImg")!=null){
                        queueDownLoadPic(jsonObj.getString("titleImg"));
                    }


                }
            }
            if (!currData.isEmpty()) {
                // 有数据返回
                // 数据加入集合中
                mListData.addAll(currData);
            } else {
                // 没有数据
                //isMore = false;
                // 向主线程发送通知
               // m.arg1=201;
               // mHandler.sendEmptyMessage(0);
                // 没有数据toPage--
                //toPage--;
            }

            mAdapter.notifyDataSetChanged(mListData);
            setListViewHeightBasedOnChildren(contentListView);
            mPullRefreshScrollView.onRefreshComplete();
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    };


    @Override
    public String getRefreshUrl(int perReqeustDataLenght) {
        String url=Api.conParam+Api.getTop10MerchantContent;
        return url;
    }

    @Override
    public String getLoadMoreUrl(int pageNo, int perReqeustDataLenght) {
        return null;
    }



    private void initContentListView() {
        contentListView= (ListView) findViewById(R.id.contentListView);
        mListData=new ArrayList<Content>();
        //contentListView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getData()));
        mAdapter=new ContentListViewAdapter(mListData,MainActivity.this);
        contentListView.setAdapter(mAdapter);
        setListViewHeightBasedOnChildren(contentListView);
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



    private void queueDownLoadPic(String titleImg)
    {
        TitleImgToDown titleImgDownTask=new TitleImgToDown(titleImg);
        executorService.submit(new TitleImgDownLoader(titleImgDownTask));
    }

    private class TitleImgToDown{
        public String url;

        private TitleImgToDown(String url) {
            this.url = url;
        }
    }


    private class TitleImgDownLoader implements Runnable{
        TitleImgToDown titleImgDownTask;
        TitleImgDownLoader(TitleImgToDown titleImgDownTask) {
            this.titleImgDownTask=titleImgDownTask;
        }

        @Override
        public void run() {

        }
    }

    /*private class GetDataTask extends AsyncTask<Void, Void, String[]> {

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

            Content bean1=new Content();
            bean1.setTitle("宝贝计划孕婴生活馆");
            bean1.setDescription("3M贝亲150ML奶瓶98,送原装奶嘴2个");
            bean1.setMoney("97");
            mListData.add(bean1);
            mAdapter.notifyDataSetChanged(mListData);
            mPullRefreshScrollView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }*/

    /*private List<String> getData(){
        List<String> data = new ArrayList<String>();
        for(int i=1;i<100;i++){
            data.add("数据"+i);
        }
        return data;
    }*/
}

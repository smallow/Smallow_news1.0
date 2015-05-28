package com.smallow.community;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smallow.common.network.NetSpirit;
import com.smallow.common.network.RequestReceiver;
import com.smallow.community.api.Api;
import com.smallow.community.interfac.CommonTitleBarOnClickLinstener;
import com.smallow.community.ui.CommonTitleBar;
import com.smallow.community.ui.SubMenuWindow;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by smallow on 2015/5/25.
 */
public class ContentDetailAty extends Activity {
    private Integer contentId;
    private List<SubMenuWindow.SubMenu> subMenuData = new ArrayList<SubMenuWindow.SubMenu>();

    PullToRefreshScrollView mPullRefreshScrollView;
    ScrollView mScrollView;
    private ImageLoader imageLoader;

    /** 标志位，标志已经初始化完成 */
    private boolean isPrepared;
    /** 是否已被加载过一次，第二次就不再去请求数据了 */
    private boolean mHasLoadedOnce;
    private static final int req_type_refersh = 0;


    private TextView price;
    private TextView merchant_name;
    private TextView merchant_address;
    private TextView merchant_mobilephone;
    private ImageView call_merchant;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.aty_content_detail);
        Intent intent=getIntent();
        if(intent!=null){
              contentId=intent.getIntExtra("contentId",0);
        }
        initCommonTitleBar();
        initPullToRefreshScrollView();
        initUI();
        lazyLoad();
    }




    private void initCommonTitleBar() {
        initSubMenuData();
        final CommonTitleBar titleBar= (CommonTitleBar) findViewById(R.id.content_detail_titlebar);
        titleBar.setOnCommonTitleBarOnClickListener(new CommonTitleBarOnClickLinstener() {
            @Override
            public void leftOnclick() {
                //startActivity(new Intent(ContentDetailAty.this,MainActivity.class));
                ContentDetailAty.this.finish();
            }

            @Override
            public void rightOnclick() {
                SubMenuWindow subMenuWindow=new SubMenuWindow(ContentDetailAty.this);
                subMenuWindow.setMenuItem(subMenuData);
                subMenuWindow.showBelowAndRight(titleBar);
            }
        });
    }

    private void initPullToRefreshScrollView() {
        mPullRefreshScrollView= (PullToRefreshScrollView) findViewById(R.id.content_detail_ptr);
        mPullRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        mScrollView = mPullRefreshScrollView.getRefreshableView();

    }


    private void initUI(){
        price= (TextView) findViewById(R.id.price);
        merchant_name=(TextView)findViewById(R.id.merchant_name);
        merchant_address=(TextView)findViewById(R.id.merchant_address);
        merchant_mobilephone= (TextView) findViewById(R.id.merchant_mobilephone);
        call_merchant= (ImageView) findViewById(R.id.call_merchant);
        imageLoader=ImageLoader.getInstance();
        isPrepared = true;

    }
    private void lazyLoad() {
        if (!isPrepared  || mHasLoadedOnce) {
            return;
        }
        NetSpirit.getInstance().httpGet(Api.conParam+Api.getContentById+"?contentId="+contentId, req_type_refersh,requestReceiver);
        mHasLoadedOnce = true;
    }








    private RequestReceiver requestReceiver=new RequestReceiver() {
        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            if(resp!=null && !"".equals(resp)){
                JSONObject object= JSON.parseObject(resp);
                String _price=String.valueOf(object.getBigDecimal("concessionalPrice"));
                price.setText(_price+"￥");
                merchant_name.setText(object.getJSONObject("merchant").getString("name"));
                merchant_address.setText(object.getString("address")==null?"":object.getString("address"));
                merchant_mobilephone.setText(object.getString("mobilephone").equals("null")?object.getString("telphone"):object.getString("mobilephone"));
                String titleImg=object.getString("titleImg");
                if(titleImg!=null && !"".equals(titleImg)){
                    ImageView imageView= (ImageView) findViewById(R.id.content_detail_titleImg);
                    imageLoader.displayImage(Api.conParam+titleImg,imageView);
                }
            }
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
           /* Intent intent=new Intent();
            intent.setClass(ContentDetailAty.this, MainActivity.class);
            startActivity(intent);*/
            ContentDetailAty.this.finish();
        }
        return false;

    }

    class ContentDetailSubMenuData implements SubMenuWindow.SubMenu{
        private String name;

        public ContentDetailSubMenuData(String name){
            this.name=name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
    private void initSubMenuData() {
        subMenuData.add(new ContentDetailSubMenuData("分享到朋友圈"));
        subMenuData.add(new ContentDetailSubMenuData("分享给微信朋友"));
        subMenuData.add(new ContentDetailSubMenuData("分享到微博"));
    }
}

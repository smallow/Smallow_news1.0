package com.smallow.community.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.smallow.common.network.NetSpirit;
import com.smallow.common.network.RequestReceiver;
import com.smallow.common.utils.StrUtils;
import com.smallow.community.R;
import com.smallow.community.adapter.ViewPagerAdpter;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;


/**
 * Created by smallow on 2015/5/25.
 */
public class SlideShowView2 extends FrameLayout implements ViewPager.OnPageChangeListener{

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private  static boolean isAutoPlay = true;

    //定时任务
    private ScheduledExecutorService scheduledExecutorService;
    //当前轮播页
    private int currentItem  = 0;
    private String[] imageUrls;
    private List<ImageView> imageViewsList;
    private List<View> dotViewsList;


    private ViewPager viewPager;
    private ViewPagerAdpter adpter;
    private Context context;
    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };
    public SlideShowView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        if (isInEditMode()) { return; }
        initImageLoader(context);
        initDisplayOptions();
        initData();
        if(isAutoPlay){
            startPlay();
        }
    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }

    private void initData(){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();
        // 一步任务获取图片
        NetSpirit.getInstance().httpGet(Api.conParam+Api.getTop10MerchantContent+"?page=1&pageSize=5",0,slidShowViewRequestReceiver);
    }

    private void initUI(Context context) {
        if(imageUrls == null || imageUrls.length == 0)
            return;
        LayoutInflater.from(context).inflate(R.layout.slide_show_view,this,true);
        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view =  new ImageView(context);
            view.setTag(imageUrls[i]);
            if(i==0)//给一个默认图
                view.setBackgroundResource(R.drawable.pic1);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView =  new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);

            viewPager = (ViewPager) findViewById(R.id.id_viewPager);
            adpter = new ViewPagerAdpter(imageViewsList,context,imageLoader,options);
            viewPager.setFocusable(true);
            viewPager.setAdapter(adpter);
            viewPager.setOnPageChangeListener(this);
        }


    }
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) // default  线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                //.diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
                //.diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
                //.diskCacheFileCount(100)  // 可以缓存的文件数量
                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
                //.memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
                //.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                //.writeDebugLogs() //打印log信息
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void initDisplayOptions(){
        options= new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic1) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.pic2) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.pic3) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
                //.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
                .build(); // 构建完成
    }


    private RequestReceiver slidShowViewRequestReceiver=new RequestReceiver() {
        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            List<Content> currData=new ArrayList<Content>();
            if(resp!=null && !"".equals(resp)){
                imageUrls = new String[5];
                JSONArray array= JSON.parseArray(resp);
                for (int i=0;i<array.size();i++) {
                    JSONObject jsonObj = (JSONObject) array.getJSONObject(i);
                    JSONObject _jsonObj=jsonObj.getJSONObject("merchant");
                    currData.add(new Content(jsonObj.getInteger("id"),_jsonObj.getString("name"), StrUtils.textCut(jsonObj.getString("title"), 35, "..."),String.valueOf(jsonObj.getFloat("concessionalPrice")),jsonObj.getString("titleImg")));
                    imageUrls [i]=Api.conParam+jsonObj.getString("titleImg");
                }
            }
            initUI(context);
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    };


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int pos) {
        for(int i=0;i < dotViewsList.size();i++){
            if(i == pos){
                ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.login_point_selected);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.login_point);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        switch (arg0) {
            case 1:// 手势滑动，空闲中
                isAutoPlay = false;
                break;
            case 2:// 界面切换中
                isAutoPlay = true;
                break;
            case 0:// 滑动结束，即切换完毕或者加载完毕
                // 当前为最后一张，此时从右向左滑，则切换到第一张
                if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                    viewPager.setCurrentItem(0);
                }
                // 当前为第一张，此时从左向右滑，则切换到最后一张
                else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                    viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                }
                break;
        }
    }


    public void onClearMemoryClick(View view) {
        Toast.makeText(context, "清除内存缓存成功", Toast.LENGTH_SHORT).show();
        ImageLoader.getInstance().clearMemoryCache();  // 清除内存缓存
    }
    public void onClearDiskClick(View view) {
        Toast.makeText(context, "清除本地缓存成功", Toast.LENGTH_SHORT).show();
        ImageLoader.getInstance().clearDiskCache();  // 清除本地缓存
    }


    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }
}

package com.smallow.community.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.smallow.common.AsyncImageLoader;
import com.smallow.community.R;
import com.smallow.community.api.Api;
import com.smallow.community.bean.Content;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by smallow on 2015/5/20.
 */
public class ContentListViewAdapter2 extends InnerBaseAdapter<Content> {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    public ContentListViewAdapter2(List<Content> data, Context context) {
        setData(data, false);
        initImageLoader(context);
        initDisplayOptions();
    }

    public void notifyDataSetChanged(List<Content> data) {
        setData(data,true);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_listview_item,null);
            holder.tvTitle = (TextView) convertView
                    .findViewById(R.id.title);
            holder.tvDescription = (TextView) convertView
                    .findViewById(R.id.description);
            holder.tvMoney = (TextView) convertView
                    .findViewById(R.id.money);
            holder.titleImg= (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Content bean=getData(position);
        holder.tvTitle.setText(bean.getTitle());
        holder.tvDescription.setText(bean.getDescription());
        holder.tvMoney.setText("￥"+bean.getMoney());
        String titleImgUrl=bean.getTitleImg();
        ImageView imageView = holder.getTitleImg();
        imageLoader.displayImage(Api.conParam+titleImgUrl,imageView,options);
        return convertView;
    }




    private class ViewHolder {
        TextView tvTitle;
        TextView tvMoney;
        TextView tvDescription;
        ImageView titleImg;


        public ImageView getTitleImg(){
            return titleImg;
        }
    }






    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache2");
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
}


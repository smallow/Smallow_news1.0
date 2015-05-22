package com.smallow.common;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by smallow on 2015/5/22.
 */
public class AsyncImageLoader {

    private HashMap<String, SoftReference<Drawable>> imageCache;

    public AsyncImageLoader() {
        imageCache = new HashMap<String, SoftReference<Drawable>>();
    }


    public Drawable loadDrawable(final String imageUrl,final ImageView imageView, final ImageCallback imageCallback) {
        try {
            if (imageCache.containsKey(imageUrl)) {
                SoftReference<Drawable> softReference = imageCache.get(imageUrl);
                Drawable drawable = softReference.get();
                if (drawable != null) {
                    return drawable;
                }
            }
            final Handler handler = new Handler() {
                public void handleMessage(Message message) {
                    imageCallback.imageLoaded((Drawable) message.obj, imageView,imageUrl);
                }
            };
            new Thread() {
                @Override
                public void run() {
                    try {
                        Drawable drawable = loadImageFromUrl(imageUrl);
                        imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));
                        Message message = handler.obtainMessage(0, drawable);
                        handler.sendMessage(message);
                    }catch (Exception e2){
                        e2.printStackTrace();
                    }

                }
            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static Drawable loadImageFromUrl(String url) {
        URL m;
        InputStream i = null;
        try {
            m = new URL(url);
            i = (InputStream) m.getContent();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(i, "src");
        return d;
    }

    /*public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable, String imageUrl);
    }*/

    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable,ImageView imageView, String imageUrl);
    }
}

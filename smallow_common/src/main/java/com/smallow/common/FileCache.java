package com.smallow.common;

import android.content.Context;

import com.smallow.common.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by smallow on 2015/5/21.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //找一个用来缓存图片的路径
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }


    public File getFile(String url){
        if(null==url || "".equals(url))
            return null;
        String filename=String.valueOf(url.hashCode());
        try {
            FileInputStream fis = new FileInputStream(url);
            File f = new File(cacheDir, filename);
            if(!f.exists())
                f.createNewFile();
            FileOutputStream fos=new FileOutputStream(f);
            byte []files= Utils.readStream(fis);
            fos.write(files);
            fos.close();
            fos=null;
            return f;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }



}

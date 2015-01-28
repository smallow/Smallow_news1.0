package com.smallow.android.news;

/**
 * Created by smallow on 2015/1/28.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 获取模拟数据
 */
public class GetNetJsonData {

    /**
     * 获取网络json数据
     *
     * @return 返回数据的list形式
     * @throws Exception
     */
    public List<ItemAttri> getDataFromJson(int pageNum) {
        List<ItemAttri> dataList = new ArrayList<ItemAttri>();
        String path = "http://jp-testdata.qiniudn.com/%40%2FlistDataPage"
                + pageNum + ".json";
        URL url;
        try {
            url = new URL(path);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                InputStream instream = conn.getInputStream();
                dataList = parseJSON(instream);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < dataList.size(); i++) {
            Log.i("GetSimulationData", "" + dataList.get(i).title);
        }

        return dataList;
    }

    /**
     * 解析JSON
     */
    private List<ItemAttri> parseJSON(InputStream instream) {

        List<ItemAttri> lst = new ArrayList<ItemAttri>();
        byte[] data;
        try {
            data = IOUtils.read(instream);

            String jsonStr = new String(data);
            JSONArray array = new JSONArray(jsonStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObj = (JSONObject) array.getJSONObject(i);
                ItemAttri v = new ItemAttri(jsonObj.getString("content"),
                        jsonObj.getString("time"), jsonObj.getString("title"));
                lst.add(v);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return lst;
    }

    /**
     * 模拟实体类
     */
    public class ItemAttri {
        public String content;
        public String time;
        public String title;

        public ItemAttri(String content, String time, String title) {
            super();
            this.content = content;
            this.time = time;
            this.title = title;
        }
    }

}

class IOUtils {
    /**
     * 读取输入流为byte[]数组
     */
    static byte[] read(InputStream instream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = instream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        return bos.toByteArray();
    }
}

package com.smallow.android.news.common;

import android.content.Context;

import com.smallow.android.news.SystemConst;
import com.smallow.android.news.utils.DeviceUtility;

import java.net.URLEncoder;

/**
 * Created by smallow on 2015/2/9.
 */
public class URLHelper {
    private static String REQUEST_COMMON_PARAMS;// 请求公用携带参数

    public static final String DEMOURL = "http://jp-testdata.qiniudn.com/%40%2FlistDataPage.json?userId=%s&token=%s&startPosition=%s&length=%s";

    private static final String getPrefix() {
        return SystemConst.prefix_data;
    }





    private static String getUrl(Context context, String urlPrefix,
                                 String pathRefer, Object... formatValues) {
        if (formatValues != null) {
            int length = formatValues.length;
            for (int i = 0; i < length; i++) {
                Object obj = formatValues[i];
                if (obj == null) {
                    formatValues[i] = "";
                } else if (obj instanceof String) {
                    obj = URLEncoder.encode((String) obj);
                    formatValues[i] = obj;
                }
            }
        }
        StringBuffer strBuffer = new StringBuffer(urlPrefix);
        strBuffer.append(String.format(pathRefer, formatValues));
        if (strBuffer.lastIndexOf("?") != (strBuffer.length() - 1)) {
            strBuffer.append('&');
        }
        strBuffer.append(getDeviceParams(context));
        return strBuffer.toString();
    }

    private static String getDeviceParams(Context context) {
        if (REQUEST_COMMON_PARAMS == null) {
            StringBuffer strBuffer = new StringBuffer();
            strBuffer.append("ver=");// 版本号
            strBuffer.append(DeviceUtility.getAppVersionName(context));
            strBuffer.append("&dev=");// 设备类型(iphone, ipad, android)
            strBuffer.append(URLEncoder.encode("android"));
            try {
                strBuffer.append("&devId=");
                strBuffer.append(URLEncoder.encode(SimpleCrypto
                        .encrypt(DeviceUtility.getDeviceID(context))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            REQUEST_COMMON_PARAMS = strBuffer.toString();
        }
        return REQUEST_COMMON_PARAMS;
    }
}

package com.smallow.android.news.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class DeviceUtility {
    public static String getAppVersionName(Context context) {
        String versionname = "0";
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionname = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionname;
    }


    public static String getDeviceID(Context context) {
        DeviceUuidFactory df = new DeviceUuidFactory(context);
        return df.getDeviceUuid().toString();
    }
}
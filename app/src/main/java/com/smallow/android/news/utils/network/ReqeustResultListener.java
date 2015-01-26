package com.smallow.android.news.utils.network;

/**
 * Created by smallow on 2015/1/26.
 */
public interface ReqeustResultListener {
    public boolean onPerReqeustReturn(RequestEntity en, ConnectionHelper conn);
}

package com.smallow.android.news.utils.network.jiekou;

import com.smallow.android.news.utils.network.ConnectionUtil;
import com.smallow.android.news.utils.network.RequestEntity;

/**
 * Created by smallow on 2015/1/23.
 */
public interface ReqeustResultListener {
    public boolean onPerReqeustReturn(RequestEntity en, ConnectionUtil conn);
}

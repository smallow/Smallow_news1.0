package com.smallow.android.news.utils.network;

/**
 * Created by smallow on 2015/2/9.
 */
public interface RequestReceiver {

    public static final int RESULT_STATE_OK = 200;
    public static final int RESULT_STATE_SERVER_ERROR = 500;
    public static final int RESULT_STATE_NETWORK_ERROR = -1;
    public static final int RESULT_STATE_CANCLED = -2;
    public static final int RESULT_STATE_TIME_OUT = 408;

    public void onResult(int resultCode, int reqId, Object tag, String resp);

    public void onRequestCanceled(int reqId, Object tag);
}

package com.smallow.android.news.utils.network;

/**
 * Created by smallow on 2015/1/26.
 */

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.net.ParseException;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;

/**
 * 网络请求数据封装
 *
 * @author sky
 */
public class ConnectionHelper {
    public static final String TAG = "ConnectionHelper";
    public static final boolean DEBUG = true;
    public static final int CONNECTION_TIMEOUT = 20000;// ms
    public static final int CON_TIME_OUT_MS = CONNECTION_TIMEOUT;
    public static final int SO_TIME_OUT_MS = CONNECTION_TIMEOUT;
    public static final int MAX_CONNECTIONS_PER_HOST = 20;// SDK默认20
    public static final int MAX_TOTAL_CONNECTIONS = 10;// 同时允许的连接个数//SDK默认2
    public static final int CONNETIONS_MAX_IDLE_TIME = 60 * 1000;//
    // 线程池
    public static final int MAX_CORE_POOL_SIZE = 2;// 一直保留的线程数
    public static final int KEEP_ALIVE_TIME = 60;// s//允许空闲线程时间
    final static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            MAX_CORE_POOL_SIZE, Integer.MAX_VALUE, KEEP_ALIVE_TIME,
            TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    //
    private HttpClient httpClient;

    private ConnectionHelper() {
        HttpParams httpParams = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(httpParams,
                MAX_TOTAL_CONNECTIONS);
        ConnPerRouteBean connPerRoute = new ConnPerRouteBean(
                MAX_CONNECTIONS_PER_HOST);
        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);

        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        SchemeRegistry reg = new SchemeRegistry();
        reg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(),
                80));
        reg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),
                443));
        ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(
                httpParams, reg);


        HttpConnectionParams.setConnectionTimeout(httpParams, CON_TIME_OUT_MS);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIME_OUT_MS);

        HttpClientParams.setCookiePolicy(httpParams,
                CookiePolicy.BROWSER_COMPATIBILITY);
        httpClient = new DefaultHttpClient(connectionManager, httpParams);
    }

    private static ConnectionHelper instance;

    public static synchronized ConnectionHelper obtainInstance() {
            if (instance == null) {
                instance = new ConnectionHelper();
            }
        return instance;
    }

    public enum RequestMethod {
        GET, POST,
        /**
         * multipart form data
         */
        POST_WITH_FILE;
    }

    /**
     * 使用指定的Method发送请求
     *
     * @param url
     * @param requestId 在onFinish的会返回requestId，标示是哪个一个请求
     * @param rr
     */
    public long httpGet(String url, int requestId, RequestReceiver rr) {
        return httpGet(url, requestId, null, rr);
    }

    public long httpGet(String url, int requestId, String acceptEncoding,
                        RequestReceiver rr) {
        /*RequestEntity entity = RequestEntity.obtain();
        entity.setUrl(url);
        entity.setRequestReceiver(rr);
        entity.setAcceptEconding(acceptEncoding);
        entity.setMethod(RequestMethod.GET);
        entity.setRequestId(requestId);
        return httpExecute(entity);*/
        return 0l;
    }

    public long httpPost(String url, int requestId,
                         List<NameValuePair> postValues, RequestReceiver rr) {
        String n = null;
        return httpPost(url, requestId, postValues, n, rr);
    }

    /**
     * Post方式发送数据
     *
     * @param url
     * @param requestId
     * @param postValues
     * @param charset
     * @param rr
     */
    public long httpPost(String url, int requestId,
                         List<NameValuePair> postValues, String charset, RequestReceiver rr) {
       /* RequestEntity entity = RequestEntity.obtain();
        entity.setUrl(url);
        entity.setRequestReceiver(rr);
        entity.setPostEntitiy(postValues, charset);
        entity.setMethod(RequestMethod.POST);
        entity.setRequestId(requestId);
        return httpExecute(entity);*/
        return 0l;
    }

    public long httpPost(String url, int requestId, String queryString,
                         RequestReceiver rr) {
        return httpPost(url, requestId, queryString, null, rr);
    }

    /**
     * Post方式发送数据
     *
     * @param url
     * @param requestId
     * @param queryString
     * @param charset
     * @param rr
     */
    public long httpPost(String url, int requestId, String queryString,
                         String charset, RequestReceiver rr) {
       /* RequestEntity entity = RequestEntity.obtain();
        entity.setUrl(url);
        entity.setRequestReceiver(rr);
        entity.setPostEntitiy(queryString, charset);
        entity.setMethod(RequestMethod.POST);
        entity.setRequestId(requestId);
        return httpExecute(entity);*/
        return 0l;
    }

    public long httpPost(String url, int requestId,
                         List<NameValuePair> postValues, Map<String, File> files,
                         RequestReceiver rr) {
        return httpPost(url, requestId, postValues, null, files, rr);
    }

    /**
     * POST方式上传文件
     *
     * @param url
     * @param requestId
     * @param postValues
     * @param files
     * @param rr
     */
    public long httpPost(String url, int requestId,
                         List<NameValuePair> postValues, String charset,
                         Map<String, File> files, RequestReceiver rr) {
        /*RequestEntity entity = RequestEntity.obtain();
        entity.setUrl(url);
        entity.setRequestReceiver(rr);
        entity.setPostEntitiy(postValues, charset, files);
        entity.setMethod(RequestMethod.POST_WITH_FILE);
        entity.setRequestId(requestId);
        return httpExecute(entity);*/
        return 0l;
    }

    private Map<Long, RequestEntity> mRequestRecords = new HashMap<Long, RequestEntity>();

    public long httpExecute(RequestEntity entity) {
        ConnectionTask task = obtainConnectionTask(entity);
        entity.setRequestTaskFuture(executor.submit(task));
        synchronized (mRequestRecords) {
            mRequestRecords.put(entity.getRequestHandler(), entity);
        }
        // executor.execute(obtainConnectionTask(entity));
        return entity.getRequestHandler();
    }

    /**
     * 判断是否在运行
     *
     * @param reqFingerprint
     * @return
     */
    public boolean isReqeustRunning(long reqFingerprint) {
        synchronized (mRequestRecords) {
            RequestEntity re = mRequestRecords.get(reqFingerprint);
            if (re == null) {
                return false;
            }
            return !re.isCanceled();
        }
    }

    public class RequestEntityNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 3742290111879087686L;
        private long reqFingerprint;

        public RequestEntityNotFoundException(long reqFingerprint) {
            this.reqFingerprint = reqFingerprint;
        }

        @Override
        public String toString() {
            return "RequestEntityNotFoundException reqFingerprint="
                    + reqFingerprint + " reqeust entity not found";
        }

    }

    /**
     * 取消请求
     *
     * @param reqFingerprint
     * @return
     */
    public boolean cancleRequest(long reqFingerprint)
            throws RequestEntityNotFoundException {
        RequestEntity request = null;
        synchronized (mRequestRecords) {
            request = mRequestRecords.get(reqFingerprint);
            if (request == null) {
                throw new RequestEntityNotFoundException(reqFingerprint);
            }
            //
            synchronized (request) {
                if (request.isCanceled() && request.isCancelStateSend()) {
                    return true;
                }
                Future<?> future = request.getRequestTaskFuture();
                if (future == null) {
                    return true;
                }
                request.setCanceled(true);
                //
                try {
                    future.cancel(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    return isReqeustRunning(reqFingerprint);
                }
                tryNotifyCanceled(request);
            }
        }
        return true;
    }

    private ReqeustResultListener mReqeustResultListener;

    public synchronized void setRequestResultListener(ReqeustResultListener t) {
        mReqeustResultListener = t;
    }

    /**
     * 连接任务处理
     */
    class ConnectionTask implements Runnable {
        RequestEntity rEntity;
        boolean isInterrupted = false;

        public ConnectionTask(RequestEntity entity) {
            rEntity = entity;
        }

        public void setRequestEntity(RequestEntity entity) {
            rEntity = entity;
        }

        @Override
        public void run() {
            HttpRequestBase httpRequest = null;
            int customResultCode = RequestReceiver.RESULT_STATE_NETWORK_ERROR;
            int statusCode = -1;
            try {
                /*if (rEntity.getMethod() == RequestMethod.GET) {
                    httpRequest = new HttpGet(rEntity.getUrl());
                } else {
                    // POST/POST_WITH_FILE
                    HttpPost httpPost = new HttpPost(rEntity.getUrl());
                    // 设置请求的数据
                    httpPost.setEntity(rEntity.getPostEntitiy());
                    httpRequest = httpPost;
                }*/
                HttpConnectionParams.setSoTimeout(httpRequest.getParams(),
                        CONNECTION_TIMEOUT);
                String acceptEncoding = rEntity.getAcceptEconding();
                if (acceptEncoding != null && acceptEncoding.length() > 0) {
                    httpRequest.addHeader("Accept-Encoding", acceptEncoding);
                }
                //
                HttpResponse response = httpClient.execute(httpRequest);
                statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.SC_OK) {
                    rEntity.setRawResponse(fetchString(rEntity,
                            response.getEntity(), rEntity.getDefaultCharset()));
                    customResultCode = RequestReceiver.RESULT_STATE_OK;
                } else {
                    customResultCode = RequestReceiver.RESULT_STATE_SERVER_ERROR;
                }
            } catch (ConnectTimeoutException | SocketTimeoutException e) {
                e.printStackTrace();
                customResultCode = RequestReceiver.RESULT_STATE_TIME_OUT;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                customResultCode = RequestReceiver.RESULT_STATE_NETWORK_ERROR;
            } catch (Exception e) {
                e.printStackTrace();
                customResultCode = RequestReceiver.RESULT_STATE_NETWORK_ERROR;
            } finally {
                try {
                    httpRequest.abort();
                } catch (Exception e) {
                    // ignore
                }
            }
            rEntity.setResultCode(customResultCode);
            // Debug
            if (DEBUG) {
                reportRequestEntity(rEntity, statusCode);
            }
            /*RequestReceiver rr = rEntity.getRequestReceiver();
            if (rr instanceof ParasiticRequestReceiver) {
                ((ParasiticRequestReceiver) rr).parasiticHandler(
                        rEntity.getResultCode(), rEntity.getRequestId(),
                        rEntity.getTag(), rEntity.getRawResponse());
            }*/
            synchronized (this) {
                if (!isInterrupted) {
                    synchronized (rEntity) {
                        if (rEntity.isCanceled()) {
                            tryNotifyCanceled(rEntity);
                        } else {
                            // 发送到主线程
                            Message msg = httpHandler.obtainMessage();
                            msg.obj = rEntity;
                            httpHandler.sendMessage(msg);
                        }
                    }
                }
            }
            recycle();
        }

        public void recycle() {
            rEntity = null;
            isInterrupted = false;
            if (connectionTaskList.size() < 6) {
                connectionTaskList.add(this);
            }
        }
    }

    public static final String CONTENT_ENCODING_GZIP = "gzip";

    static String fetchString(RequestEntity en, final HttpEntity entity,
                              final String defaultCharset) throws IOException, ParseException,
            IllegalStateException {
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        if (entity.getContentLength() > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(
                    "HTTP entity too large to be buffered in memory");
        }
        Header header = entity.getContentEncoding();
        // long time = System.currentTimeMillis();
        String result = null;
        if (header != null
                && CONTENT_ENCODING_GZIP.equalsIgnoreCase(header.getValue())) {
            result = readStringFromGzipStream(entity, defaultCharset);
            // if (en.getUrl().contains("github")) {
            // Log.e("ttt",
            // "gzip fetch string use time:"
            // + (System.currentTimeMillis() - time));
            // }
        } else {
            result = readStringFromDefaultStream(entity, defaultCharset);
            // if (en.getUrl().contains("github")) {
            // Log.e("ttt",
            // "direct fetch string use time:"
            // + (System.currentTimeMillis() - time));
            // }
        }
        return result;
    }

    private static String readStringFromGzipStream(final HttpEntity entity,
                                                   final String defaultCharset) throws IllegalStateException,
            IOException {
        InputStream instream = new GZIPInputStream(new BufferedInputStream(
                entity.getContent()));
        int i = (int) entity.getContentLength();
        if (i < 0) {
            i = 4096;
        }
        String charset = getContentCharSet(entity);
        if (charset == null) {
            charset = defaultCharset;
        }
        if (charset == null) {
            charset = HTTP.DEFAULT_CONTENT_CHARSET;
        }
        Reader reader = new InputStreamReader(instream, charset);
        CharArrayBuffer buffer = new CharArrayBuffer(i);
        try {
            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                buffer.append(tmp, 0, l);
            }
        } finally {
            safeClose(reader);
            safeClose(instream);
        }
        return buffer.toString();
    }

    private static String readStringFromDefaultStream(final HttpEntity entity,
                                                      final String defaultCharset) throws ParseException, IOException {
        return EntityUtils.toString(entity, defaultCharset);
    }

    /**
     * Obtains character set of the entity, if known.
     *
     * @param entity must not be null
     * @return the character set, or null if not found
     * @throws ParseException           if header elements cannot be parsed
     * @throws IllegalArgumentException if entity is null
     */
    public static String getContentCharSet(final HttpEntity entity)
            throws ParseException {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        String charset = null;
        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;
    }

    private static void safeClose(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 检查是否是cancle的状态，如果是则分发状态
     *
     * @param re
     */
    private void tryNotifyCanceled(RequestEntity re) {
        if (re.isCanceled() && !re.isCancelStateSend()) {
            re.setCancelStateSend(true);
            // 发送到主线程
            Message msg = httpHandler.obtainMessage();
            msg.obj = re;
            httpHandler.sendMessage(msg);
        }
    }

    private Handler.Callback handlerCallback = new Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            // 分发结果给UI Thread
            if (msg != null && msg.obj instanceof RequestEntity) {
                RequestEntity e = (RequestEntity) msg.obj;
                /*if (mReqeustResultListener != null) {
                    mReqeustResultListener.onPerReqeustReturn(e,
                            ConnectionHelper.this);
                }
                //
                RequestReceiver rr = e.getRequestReceiver();
                if (rr != null) {
                    if (e.isCanceled()) {
                        rr.onRequestCanceled(e.getRequestId(), e.getTag());
                    } else if (rr != null) {
                        rr.onResult(e.getResultCode(), e.getRequestId(),
                                e.getTag(), e.getRawResponse());
                    }
                }*/
                //
                e.recycle();
            }
            return true;
        }
    };

    private Handler httpHandler = new Handler(handlerCallback);

    private final static Vector<ConnectionTask> connectionTaskList = new Vector<ConnectionTask>();

    /**
     * 获取实例
     *
     * @param entity
     * @return
     */
    private ConnectionTask obtainConnectionTask(RequestEntity entity) {
        if (connectionTaskList.size() <= 0) {
            return new ConnectionTask(entity);
        } else {
            ConnectionTask task = connectionTaskList.remove(0);
            task.setRequestEntity(entity);
            return task;
        }
    }

    /**
     * 断开HttpClient的连接
     */
    public void shutdownConnection() {
        try {
            httpClient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tryRelaseConnetions() {
        httpClient.getConnectionManager().closeIdleConnections(
                CONNETIONS_MAX_IDLE_TIME, TimeUnit.NANOSECONDS);
    }

    @Override
    protected void finalize() throws Throwable {
        shutdownConnection();
    }

    public interface RequestReceiver {
        public static final int RESULT_STATE_OK = 200;
        public static final int RESULT_STATE_SERVER_ERROR = 500;
        public static final int RESULT_STATE_NETWORK_ERROR = -1;
        public static final int RESULT_STATE_CANCLED = -2;
        public static final int RESULT_STATE_TIME_OUT = 408;

        public void onResult(int resultCode, int reqId, Object tag, String resp);

        public void onRequestCanceled(int reqId, Object tag);
    }

    public abstract static class ParasiticRequestReceiver implements
            RequestReceiver {
        protected abstract void parasiticHandler(int resultCode, int reqId,
                                                 Object tag, String resp);

        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {

        }

    }

    public abstract static class SimpleReqeustReceiver implements
            RequestReceiver {
        @Override
        public void onRequestCanceled(int reqId, Object tag) {

        }
    }

    public static abstract class RichRequestReceiver implements RequestReceiver {

        @Override
        public void onResult(int resultCode, int reqId, Object tag, String resp) {
            onBeforeDispatch(reqId, resultCode, resp);
            if (!doResult(reqId, resultCode, resp)) {
                if (resultCode == RequestReceiver.RESULT_STATE_OK) {
                    doSuccess(reqId, resultCode, resp);
                } else {
                    doFailed(reqId, resultCode, resp);
                }
            }
            onEndDispatch(reqId, resultCode, resp);
        }

        @Override
        public void onRequestCanceled(int reqId, Object tag) {
            onBeforeDispatch(reqId, RequestReceiver.RESULT_STATE_CANCLED, null);
            doCancale(reqId);
            onEndDispatch(reqId, RequestReceiver.RESULT_STATE_CANCLED, null);
        }

        /**
         * 在Result开始分发之前
         */
        public void onBeforeDispatch(int reqId, int resultCode, String resp) {

        }

        /**
         * 在Result开始分发之后
         */
        public void onEndDispatch(int reqId, int resultCode, String resp) {

        }

        public boolean doResult(int reqId, int resultCode, String resp) {
            return false;
        }

        public void doSuccess(int reqId, int resultCode, String resp) {
        }

        public void doFailed(int reqId, int resultCode, String errorResp) {
        }

        public void doCancale(int reqId) {
        }

    }

    // public interface HttpTask extends Runnable {
    // public void setTask(RequestEntity entity);
    // }

    // just debug
    public void reportRequestEntity(RequestEntity re, int netStateCode) {
        if (re == null) {
            Log.w(TAG, "------>Connection info RequestEntity is:" + re);
            Log.d(TAG,
                    "------>Connection Thread Pool curr size:"
                            + executor.getPoolSize());
        } else {
            Log.d(TAG, "------>Connection info start");
            Log.d(TAG,
                    "------>Connection Thread Pool curr size:"
                            + executor.getPoolSize());
            Log.d(TAG, "------>Url:" + re.getUrl());
            if (netStateCode < 0) {
                Log.w(TAG, "------>Connection thorws Exception");
            } else {
                Log.d(TAG, "------>Connection StatusCode:" + netStateCode
                        + "  custom ResultCode:" + re.getResultCode());
            }
            // if (re.getPostEntitiy() == null) {
            // Log.d(TAG, "------>PostValue if Exist:null");
            // } else {
            // String postContent = null;
            // HttpEntity entity = re.getPostEntitiy();
            // Header header = entity.getContentEncoding();
            // if (header != null && header.getElements() != null) {
            // postContent = header.getElements().toString();
            // }
            // Log.d(TAG, "------>PostValue if Exist:" + postContent);
            // }
            Log.v(TAG, "------>Raw Result:" + re.getRawResponse());
            Log.d(TAG, "------>Connection info end");
        }
    }
}
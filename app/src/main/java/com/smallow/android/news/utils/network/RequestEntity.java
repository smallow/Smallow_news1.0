package com.smallow.android.news.utils.network;

import com.smallow.android.news.utils.network.jiekou.RequestReceiver;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by smallow on 2015/1/23.
 */
public class RequestEntity {

    private String url;
    private HttpEntity postEntity;
    private RequestReceiver requestReceiver;
    private String rawResponse;
    private ConnectionUtil.RequestMethod requestMethod;
    private int resultCode;
    private int requestId;
    private String defCharset;
    private Object mTag;
    private String acceptEconding;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpEntity getPostEntity() {
        return postEntity;
    }

    public void setPostEntity(HttpEntity postEntity) {
        this.postEntity = postEntity;
    }

    public void setPostEntitiy(List<NameValuePair> postValues) {
        setPostEntitiy(postValues, defCharset);
    }

    public void setPostEntitiy(List<NameValuePair> postValues, String charset) {
        try {
            postEntity = new UrlEncodedFormEntity(postValues, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public void setPostEntitiy(List<NameValuePair> postValues,
                               Map<String, File> files) {
        setPostEntitiy(postValues, defCharset, files);
    }

    /**
     * 带文件上传的POST
     * <p/>
     * <pre>
     * RequestMethod必须是{@link com.smallow.android.news.utils.network.ConnectionUtil.RequestMethod#POST_WITH_FILE}}模式
     * </pre>
     *
     * @param postValues
     * @param files
     * @param charset
     */
    public void setPostEntitiy(List<NameValuePair> postValues, String charset,
                               Map<String, File> files) {
        Charset c = null;
        try {
            c = Charset.forName(charset);
        } catch (Exception e) {
            c = null;
        }
        MultipartEntity entity;
        HttpMultipartMode mode = HttpMultipartMode.BROWSER_COMPATIBLE;
        if (c == null) {
            entity = new MultipartEntity(mode);
        } else {
            entity = new MultipartEntity(mode, null, c);
        }
        postEntity = entity;
        if (postValues != null) {
            for (NameValuePair v : postValues) {
                try {
                    entity.addPart(v.getName(), new StringBody(v.getValue()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        if (files != null) {
            Iterator<Map.Entry<String, File>> iterator = files.entrySet()
                    .iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, File> entry = iterator.next();
                entity.addPart(entry.getKey(), new FileBody(entry.getValue()));
            }
        }
    }

    public void setPostEntitiy(String querryString, String charset) {
        try {
            postEntity = new StringEntity(querryString, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public RequestReceiver getRequestReceiver() {
        return requestReceiver;
    }

    public void setRequestReceiver(RequestReceiver requestReceiver) {
        this.requestReceiver = requestReceiver;
    }

    public String getRawResponse() {
        return rawResponse;
    }

    public void setRawResponse(String rawResponse) {
        this.rawResponse = rawResponse;
    }

    public ConnectionUtil.RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(ConnectionUtil.RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getDefCharset() {
        return defCharset;
    }

    public void setDefCharset(String defCharset) {
        this.defCharset = defCharset;
    }

    public Object getmTag() {
        return mTag;
    }

    public void setmTag(Object mTag) {
        this.mTag = mTag;
    }

    public String getAcceptEconding() {
        return acceptEconding;
    }

    public void setAcceptEconding(String acceptEconding) {
        this.acceptEconding = acceptEconding;
    }


    private static long last_reqeust_index;

    /**
     * 生成下一个ReqeustEntitiy对应的id
     *
     * @return
     */
    private static long makeNextRequestIndex() {
        synchronized (RequestEntity.class) {
            long id = System.currentTimeMillis();
            if (id == last_reqeust_index) {
                id++;
            }
            last_reqeust_index = id;
            return last_reqeust_index;
        }
    }


    private boolean isCanceled = false;
    private boolean isCancelStateSend = false;
    private long requestHandler = makeNextRequestIndex();

    /**
     * 获取请求句柄
     *
     * @return
     */
    public long getRequestHandler() {
        return requestHandler;
    }

    public boolean isCancelStateSend() {
        return isCancelStateSend;
    }

    protected void setCancelStateSend(boolean send) {
        isCancelStateSend = send;
    }

    protected void setCanceled(boolean canceled) {
        this.isCanceled = canceled;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    private Future<?> requestTaskFuture;

    public void setRequestTaskFuture(Future<?> future) {
        requestTaskFuture = future;
    }

    public Future<?> getRequestTaskFuture() {
        return requestTaskFuture;
    }


    private final static List<RequestEntity> recyleList = new ArrayList<RequestEntity>();

    public static RequestEntity obtain() {
        if (recyleList.size() <= 0) {
            return new RequestEntity();
        } else {
            return recyleList.remove(0);
        }
    }

    public synchronized void recycle() {
        url = null;
        postEntity = null;
        requestReceiver = null;
        rawResponse = null;
        requestMethod = null;
        resultCode = 0;
        defCharset = null;
        isCanceled = false;
        isCancelStateSend = false;
        requestHandler = makeNextRequestIndex();
        setRequestTaskFuture(null);
        if (recyleList.size() < 6) {
            recyleList.add(this);
        }
    }
}

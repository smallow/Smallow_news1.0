package com.smallow.android.news.common;

import java.util.List;

/**
 * Created by smallow on 2015/2/9.
 */
public interface DataLoadControler<T> {



    public String getLoadMoreUrl(int pageNo, int perReqeustDataLenght);

    public String getRefreshUrl(int perReqeustDataLenght);

    public List<T> handlerParseData(String rawResp) ;

    public String getMaxId(T t);

}

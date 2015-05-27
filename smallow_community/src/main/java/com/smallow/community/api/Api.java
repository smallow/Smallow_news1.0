package com.smallow.community.api;

/**
 * Created by smallow on 2015/5/21.
 */
public class Api {

    public static final String serverIp="122.0.114.65";
    public static final int serverPort=8080;
    public static final String conParam="http://"+serverIp+":"+serverPort+"/";


    public static final String getTop10MerchantContent="mobile/getMerchantContent.do";
    public static final int getTop10MerchantContentRid=1;

    public static final String getContentById="mobile/getContentById.do";
    public static final int getContentByIdRid=2;

}

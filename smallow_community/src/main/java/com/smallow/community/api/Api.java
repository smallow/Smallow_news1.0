package com.smallow.community.api;

/**
 * Created by smallow on 2015/5/21.
 */
public class Api {

    public static final String serverIp="172.16.18.147";
    public static final int serverPort=8080;
    public static final String conParam="http://"+serverIp+":"+serverPort+"/";


    public static final String getTop10MerchantContent="mobile/getMerchantContent.do?page=1&pageSize=10";
    public static final int getTop10MerchantContentRid=1;

}

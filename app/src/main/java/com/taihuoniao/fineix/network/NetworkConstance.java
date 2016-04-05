package com.taihuoniao.fineix.network;

/**
 * Created by android on 2015/12/27.
 * 网络请求url封装
 */
public class NetworkConstance {

    public static final String BASE_URL = "http://t.taihuoniao.com/app/api";

    //热门城市 暂用ESTORE的URL
    public static final String BASE_URL_ESTORE="http://t.taihuoniao.com/app/api/estore/";

    public static final String CHARSET = "utf-8";
    public static final int CONN_TIMEOUT = 30000;
    //获取京东商品信息
    public static final String urlString_JD_productsData = BASE_URL + "/scene_product/jd_view";
    //热门城市
    public static final String HOT_CITIES = BASE_URL_ESTORE + "/get_city_list";


}

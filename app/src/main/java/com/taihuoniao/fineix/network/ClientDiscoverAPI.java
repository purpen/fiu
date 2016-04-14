package com.taihuoniao.fineix.network;

import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.utils.MD5Utils;

/**
 * Created by android on 2015/12/27.
 * 参数设置
 */
public class ClientDiscoverAPI {
    //产品
    //列表
    public static void getProductList(String category, String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_productsList;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("category", category);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //获取京东商品信息
    public static void getJDProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_JD_productsData;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //产品
    //获取淘宝商品详情
    public static void getTBProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_TB_productsData;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //场景
    //新增场景
    public static void createScene(String id, String title, String des, String scene_id, String tags, String product_id,
                                   String product_title, String product_price, String product_x, String product_y,
                                   String address, String lat, String lng, RequestCallBack<String> callBack) {
        String url = NetworkConstance.create_scene;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("title", title);
        params.addQueryStringParameter("des", des);
        params.addQueryStringParameter("scene_id", scene_id);
        params.addQueryStringParameter("tags", tags);
        params.addQueryStringParameter("product_id", product_id);
        params.addQueryStringParameter("product_title", product_title);
        params.addQueryStringParameter("product_price", product_price);
        params.addQueryStringParameter("product_x", product_x);
        params.addQueryStringParameter("product_y", product_y);
        params.addQueryStringParameter("address", address);
        params.addQueryStringParameter("lat", lat);
        params.addQueryStringParameter("lng", lng);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //使用过的标签
    public static void usedLabelList(RequestCallBack<String> callBack) {
        String url = NetworkConstance.used_label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("type", 1 + "");
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //标签列表
    public static void labelList(String parent_id, int page, String size, RequestCallBack<String> callBack) {
        String url = NetworkConstance.label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("parent_id", parent_id);
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //标签
    //热门标签
    public static void hotLabelList(String page, RequestCallBack<String> callBack) {
        String url = NetworkConstance.hot_label_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }

    //公共
    //分类列表
    public static void categoryList(String page, String domin, RequestCallBack<String> callBack) {
        String url = NetworkConstance.category_list;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("page", page);
        params.addQueryStringParameter("domin", domin);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


}

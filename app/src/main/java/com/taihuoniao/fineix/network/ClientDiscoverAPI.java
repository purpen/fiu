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

    //获取京东商品信息
    public static void getJDProductsData(String ids, RequestCallBack<String> callBack) {
        String url = NetworkConstance.urlString_JD_productsData;
        RequestParams params = new RequestParams(NetworkConstance.CHARSET);
        params.addQueryStringParameter("ids", ids);
        HttpHandler<String> httpHandler = MD5Utils.sign(params, url, callBack);
    }


}

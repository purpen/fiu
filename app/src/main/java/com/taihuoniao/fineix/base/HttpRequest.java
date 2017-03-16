package com.taihuoniao.fineix.base;

import android.os.Message;
import android.text.TextUtils;

import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MD5Utils;
import com.taihuoniao.fineix.utils.OkHttpUtils;

import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Stephen on 2017/1/4 16:48
 * Email: 895745843@qq.com
 */

public class HttpRequest {
    public static Call post(Map<String, String> params, String requestUrl){
        List<NameValuePair> nameValuePairs = getSignedList(params);
        LogUtil.e("请求接口为" + requestUrl + "\n" + "请求参数为" + removedUnnecessaryNameValuePairs(nameValuePairs).toString());
        String requestUrlreal = requestUrl;
        if (!requestUrl.contains("http")) {
            requestUrlreal = URL.BASE_URL + requestUrl;
        }
        return OkHttpUtils.post(requestUrlreal, nameValuePairs);
    }

    public static Call post(String requestUrl, GlobalDataCallBack callBack) {
        if (callBack != null) {
            callBack.onStart();
        }
        return post(getSignedList(null), requestUrl, callBack, true);
    }

    public static Call post(Map<String, String> params, String requestUrl, GlobalDataCallBack callBack) {
        if (callBack != null) {
            callBack.onStart();
        }
        return post(getSignedList(params), requestUrl, callBack, true);
    }

    private static Call post(final List<NameValuePair> list, final String requestUrl, GlobalDataCallBack callBack, boolean isShowProgress) {
        final BaseHandler handler = new BaseHandler(null, callBack);
        LogUtil.e("请求接口为" + requestUrl + "\n" + "请求参数为" + list.toString());
        String requestUrlreal = requestUrl;
        if (!requestUrl.contains("http")) {
            requestUrlreal = URL.BASE_URL + requestUrl;
        }
        return OkHttpUtils.post(requestUrlreal, list, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = BaseHandler.CALLBACK_FAILURE;
                LogUtil.e("请求接口为" + requestUrl + "\n" + "请求失败");
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                msg.what = BaseHandler.CALLBACK_SUCCESS;
                msg.obj = response.body().string();
                LogUtil.e("请求接口为" + requestUrl + "\n" + "请求参数为" + removedUnnecessaryNameValuePairs(list).toString() + "\n" + "返回数据为" + msg.obj);
                handler.sendMessage(msg);
            }
        });
    }

    private static List<NameValuePair> getSignedList(Map<String, String> params){
        if (params == null) {
            params = new HashMap<>();
        }
        List<NameValuePair> signedNameValuePairs = MD5Utils.getSignedNameValuePairs(params);
        for(int i = signedNameValuePairs.size() - 1; i >= 0; i--) {
            NameValuePair nameValuePair = signedNameValuePairs.get(i);
            if (TextUtils.isEmpty(nameValuePair.getValue())) {
                signedNameValuePairs.remove(nameValuePair);
            }
        }
        return signedNameValuePairs;
    }

    /**
     * 移除不必打印的key-value
     */
    private static List<NameValuePair> removedUnnecessaryNameValuePairs(List<NameValuePair> nameValuePairs){
        if (nameValuePairs == null) {
            return new ArrayList<NameValuePair>();
        }
        for(int i = nameValuePairs.size() - 1; i >= 0; i--) {
            NameValuePair nameValuePair = nameValuePairs.get(i);
            String name = nameValuePair.getName();
            if ("app_type".equals(name) || "client_id".equals(name) || "uuid".equals(name) || "channel".equals(name) || "time".equals(name) || "sign".equals(name)) {
                nameValuePairs.remove(nameValuePair);
            }
        }
        return nameValuePairs;
    }

    /********************************************** 旧的网络请求******************************************************/
/*    public static HttpHandler<String> sign(HashMap<String, String> params, String url, RequestCallBack<String> callBack) {
        return sign(params, url, callBack, false);
    }

    //sign
    public static HttpHandler<String> sign(HashMap<String, String> params, String url, RequestCallBack<String> callBack, boolean isPay) {
        List<NameValuePair> list = MD5Utils.getSignedNameValuePairs(params);
        params.addBodyParameter(list);
        params.getQueryStringParams().clear();
        HttpUtils httpUtils = new HttpUtils(ConstantCfg.CONN_TIMEOUT);
        if (isPay) {
            httpUtils.configCurrentHttpCacheExpiry(1000);
        }
        return httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, url, params, callBack);
    }*/
}

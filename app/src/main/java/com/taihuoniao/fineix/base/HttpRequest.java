package com.taihuoniao.fineix.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MD5Utils;
import com.taihuoniao.fineix.utils.OkHttpUtils;
import com.taihuoniao.fineix.utils.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Stephen on 2017/1/4 16:48
 * Email: 895745843@qq.com
 */

public class HttpRequest {
    private static final int CALLBACK_SUCCESS = 200;
    private static final int CALLBACK_FAILURE = 201;

    private static class BaseHandler extends Handler {
        private GlobalDataCallBack callBack;

        private BaseHandler(Context context, GlobalDataCallBack callBack) {
            this.callBack = callBack;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CALLBACK_SUCCESS:
                    callBack.processData((String) msg.obj);
                    break;
                case CALLBACK_FAILURE:
                    callBack.responseError((String) msg.obj);
                    break;
            }
        }
    }

    public static void getDataFromServer(RequestParams params, final String requestUrl, GlobalDataCallBack callBack) throws IOException {
        getDataFromServer(getList(params), requestUrl, callBack, true);
    }

    public static void getDataFromServer(List<NameValuePair> list, final String requestUrl, GlobalDataCallBack callBack, boolean isShowProgress) throws IOException {
        final BaseHandler handler = new BaseHandler(null, callBack);
        LogUtil.e("请求接口为" + requestUrl + "\\n" + "请求参数为" + list.toString());
        String requestUrlreal = requestUrl;
        if (!requestUrl.contains("http")) {
            requestUrlreal = NetworkConstance.BASE_URL + requestUrl;
        }
        OkHttpUtils.post(requestUrlreal, list, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = Message.obtain();
                msg.what = CALLBACK_FAILURE;
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message msg = Message.obtain();
                msg.what = CALLBACK_SUCCESS;
                msg.obj = response.body().string();
                LogUtil.e("请求接口为" + requestUrl + "\\n" + "返回数据为" + msg.obj);
                handler.sendMessage(msg);
            }
        });
    }

    private static List<NameValuePair> getList(RequestParams params){
        List<NameValuePair> list = new ArrayList<>();
        if (params != null) {
            list = params.getQueryStringParams();
        }
        return list;
    }

    /********************************************** 旧的网络请求******************************************************/
    public static HttpHandler<String> sign(RequestParams params, String url, RequestCallBack<String> callBack) {
        return sign(params, url, callBack, false);
    }

    //sign
    public static HttpHandler<String> sign(RequestParams params, String url, RequestCallBack<String> callBack, boolean isPay) {
        List<NameValuePair> list = MD5Utils.getSignedNameValuePairs(params);
        params.addBodyParameter(list);
        params.getQueryStringParams().clear();
        HttpUtils httpUtils = new HttpUtils(ConstantCfg.CONN_TIMEOUT);
        if (isPay) {
            httpUtils.configCurrentHttpCacheExpiry(1000);
        }
        return httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, url, params, callBack);
    }
}

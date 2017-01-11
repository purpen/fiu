package com.taihuoniao.fineix.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Stephen on 2017/1/4 14:22
 * Email: 895745843@qq.com
 */

public class OkHttpUtils {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient();
    private static final String CHARSET_NAME = "UTF-8";
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");


    private static String formatParams(List<NameValuePair> params) {
        return URLEncodedUtils.format(params, CHARSET_NAME);
//        return JsonUtil.list2Json(params);
    }

    public static Call post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
//            return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return call;
    }

    public static Call post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = mOkHttpClient.newCall(request);
        if (callback != null) {
            call.enqueue(callback);
        }
        return call;
    }

    public static Call post(String url, List<NameValuePair> list) {
        return post(url, formatParams(list));
    }

    public static Call post(String url, List<NameValuePair> list, Callback callback) {
        return post(url, formatParams(list), callback);
    }
}


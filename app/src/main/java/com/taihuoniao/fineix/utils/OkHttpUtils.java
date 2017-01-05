package com.taihuoniao.fineix.utils;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
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
    }

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = mOkHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }

    public static void post(String url, String json, Callback callback) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        if (callback != null) {
            mOkHttpClient.newCall(request).enqueue(callback);
        }
    }

    public static String post(String url, List<NameValuePair> list) throws IOException {
        return post(url, formatParams(list));
    }

    public static void post(String url, List<NameValuePair> list, Callback callback) throws IOException {
        post(url, formatParams(list), callback);
    }
}


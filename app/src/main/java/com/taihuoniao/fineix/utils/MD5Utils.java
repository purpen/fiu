package com.taihuoniao.fineix.utils;

import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.NetworkConstance;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/28.
 */
public class MD5Utils {
    static String channel = "androidPhone";

    private static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static HttpHandler<String> sign(RequestParams params, String url, RequestCallBack<String> callBack) {
        return sign(params, url, callBack, false);
    }

    //sign
    public static HttpHandler<String> sign(RequestParams params, String url, RequestCallBack<String> callBack, boolean isPay) {
//        boolean firstIn = true;
//        params.addQueryStringParameter("client_id", "1415289600");
        params.addQueryStringParameter("uuid", MainApplication.uuid);
//        params.addQueryStringParameter("channel", channel);
//        params.addQueryStringParameter("time", NetworkConstance.CONN_TIMEOUT + "");
        List<NameValuePair> list = params.getQueryStringParams();
//        Collections.sort(list, new Comparator<NameValuePair>() {
//            @Override
//            public int compare(NameValuePair lhs, NameValuePair rhs) {
//                return lhs.getName().compareTo(rhs.getName());
//            }
//        });
//        StringBuilder sign = new StringBuilder();
//        for (int i = 0; i < list.size(); i++) {
//            NameValuePair nameValuePair = list.get(i);
//            String name = nameValuePair.getName();
//            String value = nameValuePair.getValue();
//            if (name.equals("tmp") || value == null) {
//                continue;
//            }
//            if (firstIn) {
//                sign.append(name).append("=").append(value);
//                firstIn = false;
//            } else {
//                sign.append("&").append(name).append("=").append(value);
//            }
//        }
//        String sign1 = null;
//
//        sign1 = getMD5(getMD5(sign.toString() + "545d9f8aac6b7a4d04abffe51415289600"));
//
//        params.addQueryStringParameter("sign", sign1);
        Log.e("<<<", params.getQueryStringParams().toString());
        params.addBodyParameter(list);
        params.getQueryStringParams().clear();
        HttpUtils httpUtils = new HttpUtils(NetworkConstance.CONN_TIMEOUT);
        if (isPay) {
            httpUtils.configCurrentHttpCacheExpiry(1000);
        }
        return httpUtils.send(HttpRequest.HttpMethod.POST, url, params, callBack);
    }


}

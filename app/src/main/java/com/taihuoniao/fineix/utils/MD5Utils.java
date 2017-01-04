package com.taihuoniao.fineix.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ConstantCfg;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by taihuoniao on 2016/3/28.
 */
public class MD5Utils {
    public static String getMD5(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();
            StringBuilder strBuf = new StringBuilder();
            for (byte anEncryption : encryption) {
                if (Integer.toHexString(0xff & anEncryption).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & anEncryption));
                } else {
                    strBuf.append(Integer.toHexString(0xff & anEncryption));
                }
            }
            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    @NonNull
    public static List<NameValuePair> getSignedNameValuePairs(RequestParams params) {
        boolean firstIn = true;
        params.addQueryStringParameter("client_id", "1415289600");
        params.addQueryStringParameter("uuid", MainApplication.uuid);
        params.addQueryStringParameter("app_type", "2");
        params.addQueryStringParameter("channel", Util.getAppMetaData(MainApplication.getContext().getResources().getString(R.string.channel_name)));
//        ToastUtils.showError(Util.getAppMetaData(MainApplication.getContext().getResources().getString(R.string.channel_name)));
        params.addQueryStringParameter("time", System.currentTimeMillis() / 1000 + "");
        List<NameValuePair> list = params.getQueryStringParams();
        Collections.sort(list, new Comparator<NameValuePair>() {
            @Override
            public int compare(NameValuePair lhs, NameValuePair rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            NameValuePair nameValuePair = list.get(i);
            String name = nameValuePair.getName();
            String value = nameValuePair.getValue();
            if (name.equals("tmp") || name.equals("id_card_a_tmp") || name.equals("business_card_tmp")
                    || name.equals("link") || name.equals("cover_url") || name.equals("banners_url") || value == null) {
                continue;
            }
            if (firstIn) {
                sign.append(name).append("=").append(value);
                firstIn = false;
            } else {
                sign.append("&").append(name).append("=").append(value);
            }
        }
        String sign1;

        sign1 = getMD5(getMD5(sign.toString() + "545d9f8aac6b7a4d04abffe51415289600"));

        params.addQueryStringParameter("sign", sign1);
        Log.e("<<<", params.getQueryStringParams().toString());
        return list;
    }


}

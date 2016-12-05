package com.taihuoniao.fineix.pay.jdpay;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.pay.bean.JdPayParams;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

/**
 * @author lilin
 *         created at 2016/8/1 14:18
 */
public class JdPay {
    private static Activity activity;

    public static void pay(String orderId, Activity activity) {
        JdPay.activity = activity;
        getPayParams(orderId);
    }

    private static void getPayParams(final String orderId) {
        ClientDiscoverAPI.getPayParams(orderId, ConstantCfg.JD_PAY, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("JD", responseInfo.result);
                HttpResponse<JdPayParams> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<JdPayParams>>() {
                });
                if (response.isSuccess()) {
                    JdPayParams data = response.getData();
                    data.rid = orderId;
                    jdPay(data);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast("网络异常");
            }
        });
    }


    private static void jdPay(JdPayParams params) {
        Intent intent = new Intent(activity, JDPayActivity.class);
        intent.putExtra(JDPayActivity.class.getSimpleName(), params);
        activity.startActivity(intent);
    }
}

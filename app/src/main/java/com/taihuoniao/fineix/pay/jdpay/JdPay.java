package com.taihuoniao.fineix.pay.jdpay;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.pay.bean.JdPayParams;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.HashMap;

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
        HashMap<String, String> params = ClientDiscoverAPI.getgetPayParamsRequestParams(orderId, ConstantCfg.JD_PAY);
        HttpRequest.post(params,  URL.PAY_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                LogUtil.e("JD", json);
                HttpResponse<JdPayParams> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<JdPayParams>>() {
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
            public void onFailure(String error) {
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

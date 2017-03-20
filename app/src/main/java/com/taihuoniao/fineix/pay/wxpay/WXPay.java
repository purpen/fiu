package com.taihuoniao.fineix.pay.wxpay;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.pay.bean.WXPayParams;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.HashMap;

/**
 * @author lilin
 *         created at 2016/5/13 15:20
 */
public class WXPay {
    private static Activity activity;

    public static void pay(String orderId, Activity activity) {
        WXPay.activity = activity;
        getPayParams(orderId);
    }

    private static void wxPay(WXPayParams params) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(activity, null);
        msgApi.registerApp(params.appid);
        PayReq req = new PayReq();
        req.appId = params.appid;
        req.partnerId = params.partner_id;
        req.prepayId = params.prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = params.nonce_str;
        req.timeStamp = params.time_stamp;
        req.sign = params.new_sign;
        msgApi.sendReq(req);
    }

    private static void getPayParams(String orderId) {
        HashMap<String, String> params = ClientDiscoverAPI.getgetPayParamsRequestParams(orderId, ConstantCfg.WX_PAY);
        HttpRequest.post(params,  URL.PAY_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<WXPayParams> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WXPayParams>>() {
                });

                if (response.isSuccess()) {
                    WXPayParams data = response.getData();
                    wxPay(data);
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
}

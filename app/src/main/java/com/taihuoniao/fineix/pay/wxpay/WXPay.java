package com.taihuoniao.fineix.pay.wxpay;

import android.app.Activity;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

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
        ClientDiscoverAPI.getPayParams(orderId, NetworkConstance.WX_PAY, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<WXPayParams> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<WXPayParams>>() {
                });

                if (response.isSuccess()) {
                    WXPayParams data = response.getData();
                    wxPay(data);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast("网络异常");
            }
        });
//        HashMap<String,String> hashMap=new HashMap<String,String>();
//        hashMap.put("orderid", order.order_sn);
//        LogUtil.e("orderid", order.order_sn);
//        order.total_price="0.01";//TODO 测试数据
//        order.goods_amount=order.order_amount;
//        hashMap.put("order_price", order.total_price);
//        LogUtil.e("order_price",order.total_price);
//        HttpRequestData.sendPostRequest(Constants.WX_PAY_URI, hashMap, new ICallback4Http() {
//            @Override
//            public void onResponse(String response) {
//                LogUtil.e("微信onResponse", response);
//                WXPayParams wxParams = JsonUtil.fromJson(response, new TypeToken<HttpResponse<WXPayParams>>() {
//                });
//                try {
//                    String packageValue = new JSONObject(response).getJSONObject("result").getString("package");
//                    wxParams.packageValue = packageValue;
//                    LogUtil.e("packageValue", wxParams.packageValue);
//                    wxPay(activity,wxParams);
//                    WXPayEntryActivity.setWXPayResultListener(new WXPayEntryActivity.WXPayResultListener() {
//                        @Override
//                        public void execute() {
//                            activity.startActivity(new Intent(activity, UserOrderListActivity.class));
//                            activity.finish();
//                        }
//                    });
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }

//            @Override
//            public void onFailure(String errorMessage) {
//                Util.makeToast(errorMessage);
//            }
//        });
    }
}

package com.taihuoniao.fineix.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.pay.bean.ALIPayParams;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.util.HashMap;

/**
 * @author lilin
 * created at 2016/5/13 15:54
 */
public class AliPay{
	public interface AlipayListener{
		void onSuccess();
		void onFailure();
	}
	private static AlipayListener listener;
	private static Activity activity;

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private static Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					if (listener!=null){
						listener.onSuccess();
//						Util.makeToast("支付成功");
					}
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Util.makeToast("支付结果确认中");
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						if (listener!=null){
							listener.onFailure();
						}
//						Util.makeToast("可能支付失败");
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Util.makeToast("检查结果为：" + msg.obj);
				break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public static void pay( String orderId,Activity activity,AlipayListener listener) {
		AliPay.activity = activity;
		AliPay.listener = listener;
		getPayParams(orderId);
	}
	private static void getPayParams(String orderId){
		HashMap<String, String> params = ClientDiscoverAPI.getgetPayParamsRequestParams(orderId, ConstantCfg.ALI_PAY);
		HttpRequest.post(params,  URL.PAY_URL, new GlobalDataCallBack(){
			@Override
			public void onSuccess(String json) {
				if (TextUtils.isEmpty(json)) return;
				HttpResponse<ALIPayParams> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ALIPayParams>>() {
				});
				if (response.isSuccess()){
					ALIPayParams data = response.getData();
					aliPay(data.str);
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

	private static void aliPay(final String payInfo){
		LogUtil.e("payInfo", payInfo + "==");
		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(activity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo,true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
}

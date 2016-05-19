package com.taihuoniao.fineix.pay.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.ALIPayParams;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
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
	// 商户PID
	public static final String PARTNER = "2088021152594917";
	// 商户收款账号
	public static final String SELLER = "2088021152594917";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL0cenFc0hPLx1qB2pyxMD6Zf0/eV3Yi/tbP014AGV2t5sg8KZLa1gTsxDS5wVXMAcbluinWr2Sx0gdT0n7RKVd9qd0lnlEqXJOLJu/Bf5inqI6lvU9YX5ZAoEXxvE2MxCVwWJgBuzQ35jhpWsNbz1ZV0YPhN4xb6J3Oeak/9PYBAgMBAAECgYBAQXjpI9zNlSP7gLvDGgGGg6laffXB0ko8uwjam7YCup/70VVe7LRjn/9a2vLyMAs6hbwnkyatVC5FBxoytOcSmXaGjB4xBk4FEw/bhMbhIl76yWc1YUglpRfdhA2kEm2cQQ9rOO+T3Coqh4N+g9sF9PML29EUb3zeXsrdcY+fQQJBAOnm7r5jQrhCmefk6/EKJvvIPUc5r3lK0Bv+gNfCwBmin/zHPb8Yceg2zVWJxDRIZBO+6X7l4KYqK717KlkKkiUCQQDO+kBdVdpK7tkdG63z1wC3meat0NkGhNo5ciUqZrhaauII+kmn0THrpNtO4lsWIIQrb7bFIW3mMol6DmdmDXetAkA5Hk/G5m5wmLME0f5cCmKisa9lKU0UjZRsgaXtCn3mxLPVAsKtW8bVMizKaq4jJlpqCAD1ICXP7hRoXR9mRxKRAkEAlrB33/w7e8a9Z4XZdegY65Mu8WlWOHrM7nn+OQqkOaALhQHEUlvp/mf+C0adjlSKJZ2l8YvPGYO9t5F5EkHH2QJBAKaWrVN5bqRHB7PAD+naN9OFYcFwVnh50+debhGXAtdBDrIp/+4W+bgqtl9gDXK4fbj8Bxu6c3kw7ca+1cWd+HU=";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "";
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
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public static void pay( String orderId,Activity activity,AlipayListener listener) {
		AliPay.activity=activity;
		AliPay.listener=listener;
		getPayParams(orderId);
//		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
//				|| TextUtils.isEmpty(SELLER)) {
//			new AlertDialog.Builder(activity)
//					.setTitle("警告")
//					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(
//										DialogInterface dialoginterface, int i) {
//									//
////									activity.finish();
//								}
//							}).show();
//			return;
//		}

		// 订单
//		String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
//		String orderInfo = getOrderInfo(order);
		// 对订单做RSA 签名
//		String sign = sign(orderInfo);
//		try {
//			// 仅需对sign 做URL编码
//			sign = URLEncoder.encode(sign, "UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}

		// 完整的符合支付宝参数规范的订单信息
//		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
//				+ getSignType();

//		Runnable payRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask alipay = new PayTask(activity);
//				// 调用支付接口，获取支付结果
//				String result = alipay.pay(payInfo,true);
//
//				Message msg = new Message();
//				msg.what = SDK_PAY_FLAG;
//				msg.obj = result;
//				mHandler.sendMessage(msg);
//			}
//		};
//
//		// 必须异步调用
//		Thread payThread = new Thread(payRunnable);
//		payThread.start();
//		LogUtil.e("payInfo",payInfo);
	}

	private static void getPayParams(String orderId){
		ClientDiscoverAPI.getPayParams(orderId, NetworkConstance.ALI_PAY, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (responseInfo==null) return;
				if (TextUtils.isEmpty(responseInfo.result)) return;
				HttpResponse<ALIPayParams> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ALIPayParams>>() {
				});
				if (response.isSuccess()){
					ALIPayParams data = response.getData();
					aliPay(data.str);
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

	private static void aliPay(final String payInfo){
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

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
//	public void check(View v) {
//		Runnable checkRunnable = new Runnable() {
//
//			@Override
//			public void run() {
//				// 构造PayTask 对象
//				PayTask payTask = new PayTask(activity);
//				// 调用查询接口，获取查询结果
//				boolean isExist = payTask.checkAccountIfExist();
//
//				Message msg = new Message();
//				msg.what = SDK_CHECK_FLAG;
//				msg.obj = isExist;
//				mHandler.sendMessage(msg);
//			}
//		};
//
//		Thread checkThread = new Thread(checkRunnable);
//		checkThread.start();
//
//	}

//	/**
//	 * get the sdk version. 获取SDK版本号
//	 *
//	 */
//	public static void getSDKVersion() {
//		PayTask payTask = new PayTask(activity);
//		String version = payTask.getVersion();
//		Toast.makeText(activity, version, Toast.LENGTH_SHORT).show();
//	}

	/**
	 * create the order info. 创建订单信息
	 * getOrderInfo(String subject, String body, String price)
	 */
//	private static String getOrderInfo(Order order) {
//
//		// 签约合作者身份ID
//		String orderInfo = "partner=" + "\"" + PARTNER + "\"";
//
//		// 签约卖家支付宝账号
//		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
//
//		// 商户网站唯一订单号
////		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
//		orderInfo += "&out_trade_no=" + "\"" + order.order_sn + "\"";
//		// 商品名称
//		orderInfo += "&subject=" + "\"" + "众筹界支付"+ "\"";
//
//		// 商品详情
//		orderInfo += "&body=" + "\"" + "众筹界支付" + "\"";
//
//		// 商品金额 //TODO 测试数据0.01
//		orderInfo += "&total_fee=" + "\"" + order.total_price + "\"";
//
//		// 服务器异步通知页面路径
//		orderInfo += "&notify_url=" + "\"" + "http://api2.dmore.com.cn/mobile/api/client/payment.php"
//				+ "\"";
//		// 服务接口名称， 固定值
//		orderInfo += "&service=\"mobile.securitypay.pay\"";
//
//		// 支付类型， 固定值
//		orderInfo += "&payment_type=\"1\"";
//
//		// 参数编码， 固定值
//		orderInfo += "&_input_charset=\"utf-8\"";
//
//		// 设置未付款交易的超时时间
//		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//		// 取值范围：1m～15d。
//		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//		// 该参数数值不接受小数点，如1.5h，可转换为90m。
//		orderInfo += "&it_b_pay=\"30m\"";
//
//		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//		orderInfo += "&return_url=\"m.alipay.com\"";
//
//		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//		// orderInfo += "&paymethod=\"expressGateway\"";
//		LogUtil.e("orderInfo",orderInfo);
//		return orderInfo;
//	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	private static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private static String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	private static String getSignType() {
		return "sign_type=\"RSA\"";
	}

}

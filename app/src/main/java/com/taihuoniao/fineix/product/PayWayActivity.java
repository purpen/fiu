package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.pay.alipay.AliPay;
import com.taihuoniao.fineix.pay.jdpay.JdPay;
import com.taihuoniao.fineix.pay.wxpay.WXPay;
import com.taihuoniao.fineix.user.PayDetailsActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.dialog.CustomDialogForPay;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.wxapi.WXPayEntryActivity;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;


//支付方式
public class PayWayActivity extends Activity implements View.OnClickListener {
    //微信支付
//    public static final String APP_ID = "wx08a55a284c50442e";
    // IWXAPI 是第三方app和微信通信的openapi接口
//    private IWXAPI api;

    //支付宝
//    private static final int SDK_PAY_FLAG = 1;

    private ImageView mImageAlipay;
    private ImageView mImageWechat;
    private ImageView mImageJd;
    private String mTotalMoney, mRid;
    private String mPayway = ConstantCfg.ALI_PAY;
    //    private WaittingDialog mWaittingDialog = null;
    private CustomDialogForPay mDialog;
    private boolean mBack = true;//判断是否让返回键生效
    private String orderId;
    private Handler mHandler = new Handler();


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

    private PopupWindow popupWindow;

    @Override
    public void onBackPressed() {
        if (mBack) {
            showPopup();
        }
    }

    private void initPop() {
        View view = View.inflate(this, R.layout.dialog_cancel_pay, null);
        Button cancelBtn = (Button) view.findViewById(R.id.cancel_btn);
        Button confirmBtn = (Button) view.findViewById(R.id.confirm_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.alpha);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = PayWayActivity.this.getWindow().getAttributes();
                params.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(params);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(this,
                R.color.nothing));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private void showPopup() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.4f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(params);
        popupWindow.showAtLocation(activity_view, Gravity.CENTER, 0, 0);
    }

    private View activity_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        activity_view = View.inflate(this, R.layout.activity_pay_way, null);
        setContentView(activity_view);
        mDialog = new CustomDialogForPay(this);
        getIntentData();
        initData();
        initView();
        WindowUtils.chenjin(this);
        initPop();
    }


    private void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("orderId")) {
            orderId = intent.getStringExtra("orderId");
        }
    }

//    private void regToWx() {
//// 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(PayWayActivity.this, null);
//        // 将该app注册到微信
//        api.registerApp(APP_ID);
//
//    }

    private void initData() {
        mTotalMoney = getIntent().getStringExtra("paymoney");
        mRid = getIntent().getStringExtra("orderId");

    }

    private void initView() {
        GlobalTitleLayout title = (GlobalTitleLayout) findViewById(R.id.title_payway);
        title.setTitle("订单支付");
        title.setContinueTvVisible(false);
        LinearLayout mLinearAlipay = (LinearLayout) findViewById(R.id.linear_alipay);
        mLinearAlipay.setOnClickListener(this);
        LinearLayout mLinearWechat = (LinearLayout) findViewById(R.id.linear_wechat);
        mLinearWechat.setOnClickListener(this);
        LinearLayout mLinearJd = (LinearLayout) findViewById(R.id.linear_jd);
        mLinearJd.setOnClickListener(this);
        mImageAlipay = (ImageView) findViewById(R.id.image_alipay);
        mImageWechat = (ImageView) findViewById(R.id.image_wechat);
        mImageJd = (ImageView) findViewById(R.id.image_jd);
        TextView mPayMoney = (TextView) findViewById(R.id.tv_paymoney_payway);
        DecimalFormat df = new DecimalFormat("######0.00");
        mPayMoney.setText("¥" + df.format(Double.valueOf(mTotalMoney)));
        Button mPayNow = (Button) findViewById(R.id.bt_paynow_payway);
        mPayNow.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xf.taihuoniao.app.mytaihuoniao.wxapi.wxpayentryactivity");
//        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.linear_alipay:
                mImageAlipay.setImageResource(R.mipmap.checked);
                mImageWechat.setImageResource(R.mipmap.check);
                mImageJd.setImageResource(R.mipmap.check);
                mPayway = ConstantCfg.ALI_PAY;
                break;
            case R.id.linear_wechat:
                mImageWechat.setImageResource(R.mipmap.checked);
                mImageAlipay.setImageResource(R.mipmap.check);
                mImageJd.setImageResource(R.mipmap.check);
                mPayway = ConstantCfg.WX_PAY;
                break;
            case R.id.linear_jd:
                mImageJd.setImageResource(R.mipmap.checked);
                mImageAlipay.setImageResource(R.mipmap.check);
                mImageWechat.setImageResource(R.mipmap.check);
                mPayway = ConstantCfg.JD_PAY;
                break;
            case R.id.bt_paynow_payway:
//                Toast.makeText(PayWayActivity.this, "支付", Toast.LENGTH_SHORT).show();
//                if (!mWaittingDialog.isShowing()) {
//                    mWaittingDialog.show();
//                }
//                DataParser.payParser(THNApplication.uuid, mRid, mPayway, mHandler);
                if (TextUtils.equals(ConstantCfg.ALI_PAY, mPayway)) {
                    AliPay.pay(orderId, PayWayActivity.this, new AliPay.AlipayListener() {
                        @Override
                        public void onSuccess() {
                            delayThreeSeconds();
                        }

                        @Override
                        public void onFailure() {//可能支付失败
                            delayThreeSeconds();
                        }
                    });
                } else if (TextUtils.equals(ConstantCfg.WX_PAY, mPayway)) {
                    WXPay.pay(orderId, PayWayActivity.this);
                    WXPayEntryActivity.setWXPayResultListener(new WXPayEntryActivity.WXPayResultListener() {
                        @Override
                        public void onSuccess() {
                            LogUtil.e("onSuccess", "支付成功");
                            delayThreeSeconds();
                        }

                        @Override
                        public void onCancel() {
                            LogUtil.e("onCancel", "取消支付");
                            delayThreeSeconds();
                        }

                        @Override
                        public void onFailure() {
                            LogUtil.e("onFailure", "支付失败");
                            delayThreeSeconds();
                        }
                    });
                } else if (TextUtils.equals(ConstantCfg.JD_PAY, mPayway)) {
                    JdPay.pay(orderId, PayWayActivity.this);
                }
                break;
        }
    }

    //延时三秒再跳转去订单支付详情界面是为给服务器留时间以确保其及时更新数据
    private void delayThreeSeconds() {
        mBack = false;
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                toPayDetailsActivity();
            }
        }, 2000);
    }

    //跳到订单支付详情界面
    private void toPayDetailsActivity() {
        Intent intent = new Intent(PayWayActivity.this, PayDetailsActivity.class);
        intent.putExtra("rid", mRid);
        intent.putExtra("payway", mPayway);
        startActivity(intent);
    }


    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(View v, String payNowStr) {
//        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE) || TextUtils.isEmpty(SELLER)) {
//            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialoginterface, int i) {
//                            //
//                            finish();
//                        }
//                    }).show();
//            return;
//        }
//        String orderInfo = getOrderInfo(payNowStr);

//        /**
//         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
//         */
//        String sign = sign(orderInfo);
//        try {
//            /**
//             * 仅需对sign 做URL编码
//             */
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = payNowStr;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayWayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
//                msg.what = SDK_PAY_FLAG;
                msg.obj = result;

//                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }


//    /**
//     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
//     *
//     * @param v
//     */
//    public void h5Pay(View v) {
//        Intent intent = new Intent(this, H5PayDemoActivity.class);
//        Bundle extras = new Bundle();
//        /**
//         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
//         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
//         * 商户可以根据自己的需求来实现
//         */
//        String url = "http://m.meituan.com";
//        // url可以是一号店或者美团等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
//        extras.putString("url", url);
//        intent.putExtras(extras);
//        startActivity(intent);
//
//    }

//    /**
//     * create the order info. 创建订单信息
//     */
//    private String getOrderInfo(String paynowStr) {
//
//        // 签约合作者身份ID
//        String orderInfo = "partner=" + "\"" + PARTNER + "\"";
//
//        // 签约卖家支付宝账号
//        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";
//
//        // 商户网站唯一订单号
//        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";
//        String orderInfo = paynowStr.toString().trim();
//
//        // 商品名称
//        orderInfo += "&subject=" + "\"" + subject + "\"";
//
//        // 商品详情
//        orderInfo += "&body=" + "\"" + body + "\"";
//
//        // 商品金额
//        orderInfo += "&total_fee=" + "\"" + price + "\"";

//        // 服务器异步通知页面路径
//        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
//
//        // 服务接口名称， 固定值
//        orderInfo += "&service=\"mobile.securitypay.pay\"";
//
//        // 支付类型， 固定值
//        orderInfo += "&payment_type=\"1\"";
//
//        // 参数编码， 固定值
//        orderInfo += "&_input_charset=\"utf-8\"";
//
//        // 设置未付款交易的超时时间
//        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
//        // 取值范围：1m～15d。
//        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
//        // 该参数数值不接受小数点，如1.5h，可转换为90m。
//        orderInfo += "&it_b_pay=\"30m\"";
//
//        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
//        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
//
//        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//        orderInfo += "&return_url=\"m.alipay.com\"";
//
//        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//        // orderInfo += "&paymethod=\"expressGateway\"";
//
//        return orderInfo;
//    }
//
//    /**
//     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
//     */
//    private String getOutTradeNo() {
//        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
//        Date date = new Date();
//        String key = format.format(date);
//
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
//        return key;
//    }

//    /**
//     * sign the order info. 对订单信息进行签名
//     *
//     * @param content
//     *            待签名订单信息
//     */
//    private String sign(String content) {
//        return SignUtils.sign(content, RSA_PRIVATE);
//    }

//    /**
//     * get the sign type we use. 获取签名方式
//     */
//    private String getSignType() {
//        return "sign_type=\"RSA\"";
//    }


    public static byte[] streamToByte(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c;
        byte[] buffer = new byte[8 * 1024];
        try {
            while ((c = is.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] loadByteFromURL(String url) {
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.connect();

            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                bis.toString();

                return streamToByte(bis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                if (httpConn != null)
                    httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

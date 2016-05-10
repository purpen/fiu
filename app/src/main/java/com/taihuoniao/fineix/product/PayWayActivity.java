package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.CustomDialogForPay;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

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

    private LinearLayout mLinearAlipay;
    private LinearLayout mLinearWechat;
    private ImageView mImageAlipay;
    private ImageView mImageWechat;
    private String mTotalMoney, mRid;
    private String mPayway = "alipay";
    private TextView mPayMoney;
    private Button mPayNow;
    private WaittingDialog mWaittingDialog = null;
    private CustomDialogForPay mDialog;
    private boolean mBack = true;//判断是否让返回键生效
    private DecimalFormat df = null;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DataConstants.PARSER_PAY_ALIPAY:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof PayNow) {
//                            PayNow payNow = null;
//                            payNow = (PayNow) msg.obj;
//                            if (mWaittingDialog.isShowing()) {
//                                mWaittingDialog.dismiss();
//                            }
//                            pay(mPayNow, payNow.getStr());
//                        }
//                    }
//                    break;
//                case DataConstants.PARSER_PAY_WECHAT:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof PayNowWeChat) {
//                            PayNowWeChat payNowWeChat = null;
//                            payNowWeChat = (PayNowWeChat) msg.obj;
//                            if (mWaittingDialog.isShowing()) {
//                                mWaittingDialog.dismiss();
//                            }
//                            PayReq req = new PayReq();
//                            req.appId = payNowWeChat.getAppid();
//                            req.partnerId = payNowWeChat.getPartner_id();
//                            req.prepayId = payNowWeChat.getPrepay_id();
//                            req.nonceStr = payNowWeChat.getNonce_str();
//                            req.timeStamp = payNowWeChat.getTime_stamp();
//                            req.packageValue = "Sign=WXPay";
//                            req.sign = payNowWeChat.getNew_sign();
//                            Log.e(">>>", ">>>req.partnerId>>>" +req.partnerId);
//                            Log.e(">>>", ">>>req.prepayId>>>" + req.prepayId);
//                            Log.e(">>>", ">>>req.nonceStr>>>" +req.nonceStr);
//                            Log.e(">>>", ">>>req.timeStamp>>>" +req.timeStamp);
//                            Log.e(">>>", ">>>req.packageValue>>>" +req.packageValue);
//                            Log.e(">>>", ">>>req.sign>>>" +req.sign);
//                            Log.e(">>>", ">>>req.appId>>>" +req.appId);
//
//                            // 将该app注册到微信
//                            api.registerApp(APP_ID);
//                            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                            api.sendReq(req);
//                        }
//                    }
//                    break;
//                case SDK_PAY_FLAG: {
//                    PayResult payResult = new PayResult((String) msg.obj);
//                    /**
//                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
//                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
//                     * docType=1) 建议商户依赖异步通知
//                     */
//                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
//
//                    String resultStatus = payResult.getResultStatus();
//                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        delayThreeSeconds();
//                    } else {
//                        // 判断resultStatus 为非"9000"则代表可能支付失败
//                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            delayThreeSeconds();
//                        } else {
//                            delayThreeSeconds();
//                        }
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//        }
//    };

    //跳到订单支付详情界面
//    private void toPayDetailsActivity() {
//        Intent intent = new Intent(PayWayActivity.this, PayDetailsActivity.class);
//        intent.putExtra("rid", mRid);
//        intent.putExtra("payway", mPayway);
//        startActivity(intent);
//    }

    //延时三秒再跳转去订单支付详情界面是为给服务器留时间以确保其及时更新数据
//    private void delayThreeSeconds() {
//        mBack = false;
//        mDialog.show();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mDialog.dismiss();
//                toPayDetailsActivity();
//            }
//        }, 3000);
//    }

//    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int respondCode = Integer.valueOf(intent.getExtras().getString("respondcode"));
//            if (respondCode == 0) {
//                //支付成功
//                delayThreeSeconds();
//            } else if (respondCode == -1) {
//                //支付异常
//                delayThreeSeconds();
//            } else {
//                delayThreeSeconds();
//            }
//        }
//    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if (mBack) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(PayWayActivity.this);
            dialog.setTitle("您真的要放弃订单吗？");
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialog.setPositiveButton("放弃", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(PayWayActivity.this, MainActivity.class));
                    finish();
                }
            });
            dialog.show();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_pay_way);
        mDialog = new CustomDialogForPay(this);
        ActivityUtil.getInstance().addActivity(this);
        mWaittingDialog = new WaittingDialog(this);
//        regToWx();
        initData();
        initView();

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
        MyGlobalTitleLayout title = (MyGlobalTitleLayout) findViewById(R.id.title_payway);
        title.setTitle("支付方式");
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        title.setBackButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mLinearAlipay = (LinearLayout) findViewById(R.id.linear_alipay);
        mLinearAlipay.setOnClickListener(this);
        mLinearWechat = (LinearLayout) findViewById(R.id.linear_wechat);
        mLinearWechat.setOnClickListener(this);
        mImageAlipay = (ImageView) findViewById(R.id.image_alipay);
        mImageWechat = (ImageView) findViewById(R.id.image_wechat);
        mPayMoney = (TextView) findViewById(R.id.tv_paymoney_payway);
        df = new DecimalFormat("######0.00");
        mPayMoney.setText("¥" + df.format(Double.valueOf(mTotalMoney)));
        mPayNow = (Button) findViewById(R.id.bt_paynow_payway);
        mPayNow.setOnClickListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.xf.taihuoniao.app.mytaihuoniao.wxapi.wxpayentryactivity");
//        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.linear_alipay:
                mImageAlipay.setImageResource(R.mipmap.check_red);
                mImageWechat.setImageResource(R.mipmap.circle_payway);
                mPayway = "alipay";
                break;
            case R.id.linear_wechat:

                mImageWechat.setImageResource(R.mipmap.check_red);
                mImageAlipay.setImageResource(R.mipmap.circle_payway);
                mPayway = "weichat";
                break;
            case R.id.bt_paynow_payway:
                Toast.makeText(PayWayActivity.this, "支付", Toast.LENGTH_SHORT).show();
//                if (!mWaittingDialog.isShowing()) {
//                    mWaittingDialog.show();
//                }
//                DataParser.payParser(THNApplication.uuid, mRid, mPayway, mHandler);
                break;
        }
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
        int c = 0;
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
                if (baos != null) {
                    baos.close();
                }
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
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}

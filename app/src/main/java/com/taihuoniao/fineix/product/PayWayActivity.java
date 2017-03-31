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

import com.alipay.sdk.app.PayTask;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.pay.alipay.AliPay;
import com.taihuoniao.fineix.pay.jdpay.JdPay;
import com.taihuoniao.fineix.pay.wxpay.WXPay;
import com.taihuoniao.fineix.user.PayDetailsActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.CustomDialogForPay;
import com.taihuoniao.fineix.wxapi.WXPayEntryActivity;

import java.text.DecimalFormat;


// 支付方式
public class PayWayActivity extends Activity implements View.OnClickListener {
    private ImageView mImageAlipay;
    private ImageView mImageWechat;
    private ImageView mImageJd;
    private String mTotalMoney, mRid;
    private String mPayway = ConstantCfg.ALI_PAY;
    private CustomDialogForPay mDialog;
    private boolean mBack = true;//判断是否让返回键生效
    private String orderId;
    private Handler mHandler = new Handler();
    private PopupWindow popupWindow;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

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
                            delayThreeSeconds();
                        }

                        @Override
                        public void onCancel() {
                            delayThreeSeconds();
                        }

                        @Override
                        public void onFailure() {
                            delayThreeSeconds();
                        }
                    });
                } else if (TextUtils.equals(ConstantCfg.JD_PAY, mPayway)) {
                    JdPay.pay(orderId, PayWayActivity.this);
                }
                break;
        }
    }

    /**
     * 延时三秒再跳转去订单支付详情界面是为给服务器留时间以确保其及时更新数据
     */
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
        final String payInfo = payNowStr; //完整的符合支付宝参数规范的订单信息
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayWayActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.obj = result;
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}

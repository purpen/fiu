package com.taihuoniao.fineix.pay.jdpay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.pay.bean.JdPayParams;
import com.taihuoniao.fineix.user.PayDetailsActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;

public class JDPayActivity extends BaseActivity {
    private WaittingDialog dialog;
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.webView)
    WebView webView;
    private boolean flag = false;
    private JdPayParams params;

    public JDPayActivity() {
        super(R.layout.activity_jdpay);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(JDPayActivity.class.getSimpleName())) {
            params = intent.getParcelableExtra(JDPayActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, R.string.jd_pay);
        dialog = new WaittingDialog(this);
        webView.setWebViewClient(webViewClient);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);
        StringBuilder builder = new StringBuilder();
        try {
            builder.append("version=").append(Util.getEncodeStr(params.params.version));
            builder.append("&sign=").append(Util.getEncodeStr(params.params.sign));
            builder.append("&merchant=").append(Util.getEncodeStr(params.params.merchant));
            builder.append("&device=").append(Util.getEncodeStr(params.params.device));
            builder.append("&tradeNum=").append(Util.getEncodeStr(params.params.tradeNum));
            builder.append("&tradeName=").append(Util.getEncodeStr(params.params.tradeName));
            builder.append("&tradeDesc=").append(Util.getEncodeStr(params.params.tradeDesc));
            builder.append("&tradeTime=").append(Util.getEncodeStr(params.params.tradeTime));
            builder.append("&amount=").append(Util.getEncodeStr(params.params.amount));
            builder.append("&orderType=").append(Util.getEncodeStr(params.params.orderType));
            builder.append("&industryCategoryCode=").append(Util.getEncodeStr(params.params.industryCategoryCode));
            builder.append("&currency=").append(Util.getEncodeStr(params.params.currency));
            builder.append("&note=").append(Util.getEncodeStr(params.params.note));
            builder.append("&callbackUrl=").append(Util.getEncodeStr(params.params.callbackUrl));
            builder.append("&notifyUrl=").append(Util.getEncodeStr(params.params.notifyUrl));
            builder.append("&ip=").append(Util.getEncodeStr(params.params.ip));
            builder.append("&specCardNo=").append(Util.getEncodeStr(params.params.specCardNo));
            builder.append("&specId=").append(Util.getEncodeStr(params.params.specId));
            builder.append("&specName=").append(Util.getEncodeStr(params.params.specName));
            builder.append("&userType=").append(Util.getEncodeStr(params.params.userType));
            builder.append("&userId=").append(Util.getEncodeStr(params.params.userId));
            builder.append("&expireTime=").append(Util.getEncodeStr(params.params.expireTime));
            byte[] bytes = builder.toString().getBytes(ConstantCfg.CHARSET);
            webView.postUrl(params.url, bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (TextUtils.isEmpty(url)) return;
            LogUtil.e(TAG, url);
            if (!flag && url.contains("taihuoniao.com")) {
                flag = true;
                delayJump();
            }
            if (dialog != null) dialog.show();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (dialog != null) dialog.dismiss();
        }

    };

    private void delayJump() {
        if (dialog != null) dialog.show();
        new MyHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                toPayDetailsActivity();
            }
        }, 2000);
    }

    private void toPayDetailsActivity() {
        Intent intent = new Intent(activity, PayDetailsActivity.class);
        intent.putExtra("rid", params.rid);
        intent.putExtra("payway", ConstantCfg.JD_PAY);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewClient = null;
        webView.removeAllViews();
        webView.destroy();
    }

    private static class MyHandler extends Handler {
    }
}

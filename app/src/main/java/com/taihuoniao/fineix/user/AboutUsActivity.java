package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import butterknife.Bind;

public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    private String url;
    private String title;
    private WebView webView;

    public AboutUsActivity(){
        super(R.layout.activity_about_us);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(AboutUsActivity.class.getSimpleName())){
            url=intent.getStringExtra(AboutUsActivity.class.getSimpleName());
        }

        if (intent.hasExtra(AboutUsActivity.class.getName())){
            title=intent.getStringExtra(AboutUsActivity.class.getName());
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,title);
        WindowUtils.chenjin(this);
        webView = (WebView) findViewById(R.id.webView_about);
        webView.setWebViewClient(new MyWebViewClient(activity));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);
        webView.loadUrl(url);
    }



    static class MyWebViewClient extends WebViewClient{
        private WaittingDialog dialog;
        private boolean isFirstLoad = true;
        private Activity activity;
        public MyWebViewClient(Activity activity){
            this.activity = activity;
            dialog=new WaittingDialog(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!isFirstLoad){
                LogUtil.e(url.toString());
                Intent intent = new Intent(activity, UsableRedPacketActivity.class);
                activity.startActivity(intent);
                return true;
            }
            isFirstLoad = false;
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (dialog != null) dialog.show();

        }
        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            if (dialog != null) dialog.dismiss();
        }
    }
    @Override
    protected void onDestroy() {
        if (webView!=null){
            ViewGroup parent = (ViewGroup) webView.getParent();
            if (null!=parent) parent.removeView(webView);
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }
}

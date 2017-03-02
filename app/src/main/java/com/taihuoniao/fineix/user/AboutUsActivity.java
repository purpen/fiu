package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import butterknife.Bind;

public class AboutUsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
//    private WaittingDialog dialog;
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
//        dialog = new WaittingDialog(this);
        webView = (WebView) findViewById(R.id.webView_about);
        webView.setWebViewClient(new MyWebViewClient(activity));
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);

        // 为了让javascript中的alert()执行，必须设置以下语句
//        mWebAbout.setWebChromeClient(new WebChromeClient() {
//            @Override
//            public boolean onJsAlert(WebView view, String url, String message,
//                                     final JsResult result) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(
//                        activity);
//                builder.setIcon(R.mipmap.ic_launcher);
//                builder.setTitle("提示：");
//                builder.setMessage(message);
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                });
//                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                });
//                builder.show();
//                return true;
//            }
//        });
//        "http://m.taihuoniao.com/app/api/view/about"
        webView.loadUrl(url);
    }



    static class MyWebViewClient extends WebViewClient{
        private WaittingDialog dialog;
        public MyWebViewClient(Activity activity){
            dialog=new WaittingDialog(activity);
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
        super.onDestroy();
        webView.removeAllViews();
        webView.destroy();
    }
}

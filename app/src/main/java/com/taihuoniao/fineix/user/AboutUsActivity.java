package com.taihuoniao.fineix.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

public class AboutUsActivity extends BaseActivity {
    private WebView mWebAbout;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    private ProgressDialog mDialog;

    public AboutUsActivity(){
        super(R.layout.activity_about_us);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"关于我们");
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正为您拼命加载...");
        mDialog.show();

        mWebAbout = (WebView) findViewById(R.id.webView_about);
        mWebAbout.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if(!mDialog.isShowing()){
                    mDialog.show();
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(mDialog.isShowing()){
                    mDialog.dismiss();
                }
            }
        });
        WebSettings webSettings = mWebAbout.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        // 为了让javascript中的alert()执行，必须设置以下语句
        mWebAbout.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message,
                                     final JsResult result) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        activity);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("提示：");
                builder.setMessage(message);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.cancel();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                builder.show();
                return true;
            }
        });

        mWebAbout.loadUrl("http://m.taihuoniao.com/guide/about");
    }

}

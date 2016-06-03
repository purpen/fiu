package com.taihuoniao.fineix.product;

import android.webkit.WebView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.ToastUtils;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class WebActivity extends BaseActivity {
    //上个界面传递过来的购买链接
    private String url;
    //界面下的控件
    @Bind(R.id.activity_web_webview)
    WebView webView;

    @Override
    protected void getIntentData() {
        url = getIntent().getStringExtra("url");
        if (url == null) {
            ToastUtils.showError("购买链接已失效");
//            new SVProgressHUD(this).showErrorWithStatus("购买链接已失效");
//            Toast.makeText(WebActivity.this, "购买链接已失效", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public WebActivity() {
        super(R.layout.activity_web);
    }

    @Override
    protected void initView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}

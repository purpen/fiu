package com.taihuoniao.fineix.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubjectActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.webView_about)
    WebView webViewAbout;
    private WaittingDialog dialog;
    private String url;
    private String title;
    private SubjectData data;

    public SubjectActivity() {
        super(R.layout.activity_subject);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(SubjectActivity.class.getSimpleName())) {
            url = intent.getStringExtra(SubjectActivity.class.getSimpleName());
        }

        if (intent.hasExtra(SubjectActivity.class.getName())) {
            title = intent.getStringExtra(SubjectActivity.class.getName());
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, title);
        dialog = new WaittingDialog(this);
        webViewAbout.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
            }
        });
        WebSettings webSettings = webViewAbout.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);

        // 为了让javascript中的alert()执行，必须设置以下语句
        webViewAbout.setWebChromeClient(new WebChromeClient() {
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
    }

    @OnClick({R.id.ibtn_favorite, R.id.ibtn_share, R.id.ibtn_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_favorite: //收藏

                break;
            case R.id.ibtn_share: //分享

                break;
            case R.id.ibtn_comment: //去评论

                break;
        }
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getSubjectData(url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SubjectData>>() {
                });

                if (response.isSuccess()) {
                    data = response.getData();
                    refreshUI();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showError("网络异常，请确认网络畅通");
            }
        });
    }

    @Override
    protected void refreshUI() {
        webViewAbout.loadUrl(data.content_view_url);
    }

    @Override
    protected void onDestroy() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

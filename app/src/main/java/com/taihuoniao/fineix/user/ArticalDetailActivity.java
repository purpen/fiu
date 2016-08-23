package com.taihuoniao.fineix.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomShareView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin  文章和新品是H5，每个item去购买是促销
 *         created at 2016/8/23 10:28
 */
public class ArticalDetailActivity extends BaseActivity {
    public static final int REQUEST_COMMENT = 100;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.webView_about)
    WebView webViewAbout;
    @Bind(R.id.ibtn_favorite)
    TextView ibtnFavorite;
    @Bind(R.id.ibtn_comment)
    TextView ibtnComment;
    @Bind(R.id.ibtn_share)
    TextView ibtnShare;
    private WaittingDialog dialog;
    private String id;
    private SubjectData data;

    public ArticalDetailActivity() {
        super(R.layout.activity_artical_detail);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            id = intent.getStringExtra(TAG);
        }
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "文章详情");
        dialog = new WaittingDialog(this);
        WebSettings webSettings = webViewAbout.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);
        webViewAbout.setWebViewClient(webViewClient);
    }

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageFinished(view, url);
            if (!activity.isFinishing() && dialog != null) dialog.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!activity.isFinishing() && dialog != null) dialog.show();
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (!activity.isFinishing() && dialog != null) dialog.dismiss();
        }
    };

    @OnClick({R.id.ibtn_share, R.id.ibtn_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_share: //分享
                ShareContent content = new ShareContent();
                content.shareTxt = data.title;
                content.url = data.content_view_url;
                content.titleUrl = data.share_view_url;
                content.site = getResources().getString(R.string.app_name);
                content.siteUrl = "http://www.taihuoniao.com/";
                CustomShareView customShareView = new CustomShareView(activity, content);
                customShareView.setOnShareSuccessListener(new CustomShareView.OnShareSuccessListener() {
                    @Override
                    public void onSuccess() {
                        ibtnShare.setText(String.valueOf(data.share_count + 1));
                    }
                });
                PopupWindowUtil.show(activity, customShareView);
                break;
            case R.id.ibtn_comment: //去评论
                if (data == null) return;
                Intent intent = new Intent(activity, CommentListActivity.class);
                intent.putExtra("target_id", String.valueOf(data._id)); //专题id
                intent.putExtra("type", String.valueOf(13));
                intent.putExtra("target_user_id", String.valueOf(data.user_id));
                startActivityForResult(intent, REQUEST_COMMENT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_COMMENT:
                    int count = data.getIntExtra(CommentListActivity.class.getSimpleName(), this.data.comment_count);
                    ibtnComment.setText(String.valueOf(count));
                    break;
            }
        }
    }


    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getSubjectData(id, new RequestCallBack<String>() {
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
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        webViewAbout.loadUrl(data.content_view_url);
        ibtnFavorite.setText(String.valueOf(data.view_count));
        ibtnComment.setText(String.valueOf(data.comment_count));
        ibtnShare.setText(String.valueOf(data.share_count));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewAbout.removeAllViews();
        webViewClient = null;
        webViewAbout.destroy();
    }

}

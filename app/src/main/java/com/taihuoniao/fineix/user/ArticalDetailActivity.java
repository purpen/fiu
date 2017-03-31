package com.taihuoniao.fineix.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomShareView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author lilin  文章和新品是H5，每个item去购买是促销
 *         created at 2016/8/23 10:28
 */
public class ArticalDetailActivity extends BaseActivity {
    private static final String INFO_TYPE_URL = "1";
    private static final String INFO_TYPE_QJ = "11";
    private static final String INFO_TYPE_CP = "12";
    private static final String INFO_TYPE_USER = "13";
    private static final String INFO_TYPE_JXZT = "14";  //精选主题
    private static final String INFO_TYPE_PP = "15";  //品牌
    private static final String INFO_TYPE_SEARCH = "20";
    private static final String INFO_TYPE_RED_PACKAGE = "16";
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
        WebSettings webSettings = webViewAbout.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setAppCacheEnabled(true);
        webViewAbout.setWebViewClient(new MyWebViewClient(activity));
        WindowUtils.chenjin(this);
    }

    static class MyWebViewClient extends WebViewClient {
        private WaittingDialog dialog;
        private Activity activity;

        public MyWebViewClient(Activity activity) {
            this.activity = activity;
            dialog = new WaittingDialog(activity);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("infoType") && url.contains("infoId")) {
                Intent intent;
                url = url.substring(url.indexOf("?") + 1, url.length());
                String[] args = url.split("&");
                String infoType = args[0].split("=")[1];
                String infoId = args[1].split("=")[1];
                LogUtil.e("text", String.format("infoType=%s;infoId=%s", infoType, infoId));
                if (TextUtils.isEmpty(infoType) || TextUtils.isEmpty(infoId)) {
                    LogUtil.e("TextUtils.isEmpty(infoType) || TextUtils.isEmpty(infoId)", "参数为空");
                    return true;
                }
                if (TextUtils.equals(INFO_TYPE_USER, infoType)) {//跳转个人中心
                    intent = new Intent(activity, UserCenterActivity.class);
                    intent.putExtra(FocusActivity.USER_ID_EXTRA, infoId);
                    activity.startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_QJ, infoType)) {//跳转情境详情
                    intent = new Intent(activity, QJDetailActivity.class);
                    intent.putExtra("id", infoId);
                    activity.startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_JXZT, infoType)) {//精选主题
                    GoToNextUtils.jump2ThemeDetail(activity, infoId, false);
                } else if (TextUtils.equals(INFO_TYPE_CP, infoType)) {//转产品详情
                    intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", infoId);
                    activity.startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_PP, infoType)) {//品牌详情
                    intent = new Intent(activity, BrandDetailActivity.class);
                    intent.putExtra("id", infoId);
                    activity.startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_SEARCH, infoType)) { //搜索界面
                    if (url.contains("infoTag")) {
                        String infoTag = args[2].split("=")[1];
                        if (!TextUtils.isEmpty(infoTag)) {
                            intent = new Intent(activity, SearchActivity.class);
                            intent.putExtra("q", infoTag);
                            intent.putExtra("t", Integer.parseInt(infoId));
                            activity.startActivity(intent);
                        }
                    }
                } else if (TextUtils.equals(INFO_TYPE_URL, infoType)) { //用浏览器打开
                    Uri uri = Uri.parse(infoId);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    activity.startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_RED_PACKAGE, infoType)) {
                    if (LoginInfo.isUserLogin()) {
                        intent = new Intent(activity, UsableRedPacketActivity.class);
                        activity.startActivity(intent);
                    } else {
                        activity.startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    }
                }
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
            super.onReceivedSslError(view, handler, error);
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
    }

    @OnClick({R.id.ibtn_share, R.id.ibtn_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_share: //分享
                ShareContent content = new ShareContent();
                content.title = data.title;
                content.shareTxt = data.summary;
                content.url = data.content_view_url;
                content.titleUrl = data.share_view_url;
                content.imageUrl = data.banner_url;
                content.site = getResources().getString(R.string.app_name);
                content.siteUrl = data.share_view_url;
                content.shareType = Platform.SHARE_WEBPAGE;
                CustomShareView customShareView = new CustomShareView(activity, content);
                customShareView.setOnShareSuccessListener(new CustomShareView.OnShareSuccessListener() {
                    @Override
                    public void onSuccess() {
                        HashMap<String, String> params = ClientDiscoverAPI.getupdateShareCountRequestParams(id);
                        HttpRequest.post(params, URL.SCENE_SUBJECT_RECORD_SHARE_COUNT, new GlobalDataCallBack(){
                            @Override
                            public void onSuccess(String json) {
                                if (TextUtils.isEmpty(json)) return;
                                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                                if (response.isSuccess()) {
                                    return;
                                }
                                LogUtil.e(TAG, "更新分享数量失败");
                            }

                            @Override
                            public void onFailure(String error) {
                                LogUtil.e(TAG, "更新分享数量失败：" + error);
                            }
                        });
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
        HashMap<String, String> params = ClientDiscoverAPI.getgetSubjectDataRequestParams(id);
        HttpRequest.post(params,                                    URL.SCENE_SUBJECT_VIEW, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectData>>() {
                });

                if (response.isSuccess()) {
                    data = response.getData();
                    refreshUI();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        custom_head.setHeadCenterTxtShow(true, data.title);
        webViewAbout.loadUrl(data.content_view_url);
        ibtnFavorite.setText(String.valueOf(data.view_count));
        ibtnComment.setText(String.valueOf(data.comment_count));
        ibtnShare.setText(String.valueOf(data.share_count));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewAbout.removeAllViews();
//        webViewClient = null;
        webViewAbout.destroy();
    }

}

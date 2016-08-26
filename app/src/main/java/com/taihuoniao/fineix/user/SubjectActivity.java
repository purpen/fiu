package com.taihuoniao.fineix.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.beans.SupportData;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.MyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.CommentListActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomShareView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class SubjectActivity extends BaseActivity {
    private static final String INFO_TYPE_URL = "1";
    private static final String INFO_TYPE_QJ = "11";
    private static final String INFO_TYPE_CP = "12";
    private static final String INFO_TYPE_USER = "13";
    private static final String INFO_TYPE_JXZT = "14";  //精选主题
    private static final String INFO_TYPE_PP = "15";  //品牌
    private static final String INFO_TYPE_SEARCH = "20";
    public static final int REQUEST_COMMENT = 100;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.webView_about)
    WebView webViewAbout;
    @Bind(R.id.ibtn_favorite)
    TextView ibtnFavorite;
    @Bind(R.id.ibtn_comment)
    TextView ibtnComment;
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

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, title);
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
            LogUtil.e("shouldOverrideUrlLoading", url);
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
                    startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_QJ, infoType)) {//跳转情境详情
                    intent = new Intent(activity, QingjingDetailActivity.class);
                    intent.putExtra("id", infoId);
                    startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_JXZT, infoType)) {//精选主题
                    jump2ThemeDetail(infoId);
                } else if (TextUtils.equals(INFO_TYPE_CP, infoType)) {//转产品详情
                    intent = new Intent(activity, MyGoodsDetailsActivity.class);
                    intent.putExtra("id", infoId);
                    startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_PP, infoType)) {//品牌详情
                    intent = new Intent(activity, BrandDetailActivity.class);
                    intent.putExtra("id", infoId);
                    startActivity(intent);
                } else if (TextUtils.equals(INFO_TYPE_SEARCH, infoType)) { //搜索界面
                    if (url.contains("infoTag")) {
                        String infoTag = args[2].split("=")[1];
                        if (!TextUtils.isEmpty(infoTag)) {
                            intent = new Intent(activity, SearchActivity.class);
                            intent.putExtra("q", infoTag);
                            intent.putExtra("t", Integer.parseInt(infoId));
                            startActivity(intent);
                        }
                    }
                } else if (TextUtils.equals(INFO_TYPE_URL, infoType)) { //用浏览器打开
                    Uri uri = Uri.parse(url);
                    intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
                return true;
            }
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

    private void jump2ThemeDetail(final String id) {
        if (TextUtils.isEmpty(id)) return;
        ClientDiscoverAPI.getSubjectData(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SubjectData>>() {
                });

                if (response.isSuccess()) {
                    SubjectData data = response.getData();
                    Intent intent;
                    switch (data.type) {
                        case 1: //文章详情
                            intent = new Intent(activity, ArticalDetailActivity.class);
                            intent.putExtra(ArticalDetailActivity.class.getSimpleName(), id);
                            activity.startActivity(intent);
                            break;
                        case 2: //活动详情
                            intent = new Intent(activity, ActivityDetailActivity.class);
                            intent.putExtra(ActivityDetailActivity.class.getSimpleName(), id);
                            activity.startActivity(intent);
                            break;
                        case 3: //新品
                            intent = new Intent(activity, NewProductDetailActivity.class);
                            intent.putExtra(NewProductDetailActivity.class.getSimpleName(), id);
                            activity.startActivity(intent);
                            break;
                        case 4: //促销
                            intent = new Intent(activity, SalePromotionDetailActivity.class);
                            intent.putExtra(SalePromotionDetailActivity.class.getSimpleName(), id);
                            activity.startActivity(intent);
                            break;
                    }
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

    @OnClick({R.id.ibtn_favorite, R.id.ibtn_share, R.id.ibtn_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_favorite: //点赞
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(activity, OptRegisterLoginActivity.class));
                    return;
                }
                doFavorite((TextView) v);
                break;
            case R.id.ibtn_share: //分享
                ShareContent content = new ShareContent();
                content.shareTxt = data.title;
                content.url = data.content_view_url;
                content.titleUrl = data.share_view_url;
                content.site = getResources().getString(R.string.app_name);
                content.siteUrl = "http://www.taihuoniao.com/";
                PopupWindowUtil.show(activity, new CustomShareView(activity, content));
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

    /**
     * 点赞
     */
    private void doFavorite(final TextView v) {
        if (data == null) return;
        switch (data.is_love) {
            case 0:
                ClientDiscoverAPI.loveNet(String.valueOf(data._id), String.valueOf(13), new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        v.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        v.setEnabled(true);
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse<SupportData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SupportData>>() {
                        });
                        if (response.isSuccess()) {
                            data.is_love = 1;
                            v.setText(response.getData().love_count);
                            v.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.collect, 0, 0, 0);
                            ToastUtils.showSuccess("已赞");
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        v.setEnabled(true);
                        ToastUtils.showError("网络异常，请确保网络畅通");
                    }
                });
                break;
            case 1:
                ClientDiscoverAPI.cancelLoveNet(String.valueOf(data._id), String.valueOf(13), new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        v.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        v.setEnabled(true);
                        if (TextUtils.isEmpty(responseInfo.result)) return;
                        HttpResponse<SupportData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SupportData>>() {
                        });
                        if (response.isSuccess()) {
                            data.is_love = 0;
                            v.setText(response.getData().love_count);
                            v.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_collect, 0, 0, 0);
                            ToastUtils.showSuccess("已取消赞");
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        v.setEnabled(true);
                        ToastUtils.showError("网络异常，请确保网络畅通");
                    }
                });
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
        ibtnFavorite.setText(String.valueOf(data.love_count));
        if (data.is_love == 1) {
            ibtnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.collect, 0, 0, 0);
        } else {
            ibtnFavorite.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.not_collect, 0, 0, 0);
        }
        ibtnComment.setText(String.valueOf(data.comment_count));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewAbout.removeAllViews();
        webViewClient = null;
        webViewAbout.destroy();
    }

}

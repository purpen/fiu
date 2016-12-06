package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.NetWorkUtils;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.utils.DataCleanUtil;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.CustomShareView;
import com.umeng.message.IUmengCallback;
import com.umeng.message.MessageSharedPrefs;
import com.umeng.message.PushAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author lilin
 *         created at 2016/4/25 13:33
 */
public class SystemSettingsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.item_push_setting)
    CustomItemLayout item_push_setting;
    @Bind(R.id.item_update_psd)
    CustomItemLayout item_update_psd;
    @Bind(R.id.item_clear_cache)
    CustomItemLayout item_clear_cache;
    @Bind(R.id.item_to_comment)
    CustomItemLayout item_to_comment;
    @Bind(R.id.item_welcome_page)
    CustomItemLayout item_welcome_page;
    @Bind(R.id.item_service)
    CustomItemLayout item_service;
    @Bind(R.id.item_feedback)
    CustomItemLayout item_feedback;
    @Bind(R.id.item_about_us)
    CustomItemLayout item_about_us;
    @Bind(R.id.item_share)
    CustomItemLayout item_share;
    @Bind(R.id.item_check_update)
    CustomItemLayout itemCheckUpdate;

    private PushAgent mPushAgent;

    public SystemSettingsActivity() {
        super(R.layout.activity_system_settings);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "系统设置");
        item_update_psd.setTVStyle(0, "修改密码", R.color.color_333);
        item_push_setting.setTVStyle(0, "推送设置", R.color.color_333);
        item_push_setting.setRightMoreImgStyle(false);
        updateStatus();
        item_clear_cache.setTVStyle(0, "清空缓存", R.color.color_333);
        item_to_comment.setTVStyle(0, "去评价", R.color.color_333);
        item_welcome_page.setTVStyle(0, "欢迎页", R.color.color_333);
        item_service.setTVStyle(0, "服务条款", R.color.color_333);
        item_about_us.setTVStyle(0, R.string.about_us, R.color.color_333);
        item_feedback.setTVStyle(0, R.string.feed_back, R.color.color_333);
        item_share.setTVStyle(0, "分享给好友", R.color.color_333);
        itemCheckUpdate.setTVStyle(0, "检查更新", R.color.color_333);
        setCacheSize();
//        LogUtil.e("getCacheDir",getCacheDir().getAbsolutePath());
//        LogUtil.e("getCacheDirLen",getCacheDir().length()+"");
//        LogUtil.e("getExternalCacheDir",getExternalCacheDir().getAbsolutePath());
//        LogUtil.e("getExternalCacheDirLen",getExternalCacheDir().length()+"");
//        LogUtil.e("ImageLoaderCache",ImageLoader.getInstance().getDiskCache().getDirectory().getAbsolutePath());
        WindowUtils.chenjin(this);
        mPushAgent = PushAgent.getInstance(this);
    }

    @OnClick({R.id.item_update_psd, R.id.item_push_setting, R.id.btn_logout, R.id.item_clear_cache, R.id.item_to_comment, R.id.item_welcome_page, R.id.item_service, R.id.item_about_us, R.id.item_feedback, R.id.item_share, R.id.item_exit, R.id.item_check_update})
    void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.item_update_psd:
                startActivity(new Intent(activity, UpdatePasswordActivity.class));
                break;
            case R.id.item_push_setting:
                switchPush();
                break;
            case R.id.item_clear_cache:
                new MyAsyncTask().execute();
                break;
            case R.id.item_to_comment:
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.item_welcome_page:
                intent = new Intent(activity, UserGuideActivity.class);
                intent.putExtra(getClass().getSimpleName(), getClass().getSimpleName());
                startActivity(intent);
                break;
            case R.id.item_service:
                String url1 = NetworkConstance.BASE_URL + "/view/fiu_service_term?uuid=" + MainApplication.uuid + "&from_to=2&app_type=2";
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), url1);
                intent.putExtra(AboutUsActivity.class.getName(), "服务条款");
                startActivity(intent);
                break;
            case R.id.item_about_us:
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(), NetworkConstance.SETTINGSS_ABOUTUS);
                intent.putExtra(AboutUsActivity.class.getName(), "关于我们");
                startActivity(intent);
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity, FeedbackActivity.class));
                break;
            case R.id.item_share:
                ShareContent content = new ShareContent();
                content.shareType = Platform.SHARE_TEXT;
                content.shareTxt = getResources().getString(R.string.share_txt);
                content.title = getResources().getString(R.string.app_name);
                content.titleUrl = getResources().getString(R.string.title_url);
                content.site = getResources().getString(R.string.app_name);
                content.imageUrl = getResources().getString(R.string.title_url);
                content.siteUrl = getResources().getString(R.string.title_url);
                content.url = getResources().getString(R.string.title_url);
                PopupWindowUtil.show(activity, new CustomShareView(activity, content));
                break;
            case R.id.item_exit:
                logout();
                break;
            case R.id.item_check_update:
                NetWorkUtils netWorkUtils = new NetWorkUtils(this);
                netWorkUtils.updateToLatestVersion();
                break;
        }
    }

    private void logout() {
        ClientDiscoverAPI.logout(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {//   退出成功跳转首页
                    ToastUtils.showSuccess("退出成功");
                }
                SPUtil.remove(DataConstants.LOGIN_INFO);
                ((MainApplication) getApplication()).removeUserId();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra(IndexFragment.class.getSimpleName(), IndexFragment.class.getSimpleName());
                intent.putExtra("exit", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG, s);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                DataCleanUtil.cleanAppData(activity, FileUtils.getSavePath(getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setCacheSize();
            ToastUtils.showSuccess("清理完成");
        }
    }

    private void setCacheSize() {
        try {
            item_clear_cache.setTvArrowLeftStyle(true, DataCleanUtil.getTotalCacheSize(activity), R.color.color_af8323);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //切换推送开关
    private void switchPush() {
        item_push_setting.setClickable(false);
        if (((MainApplication) this.getApplication()).isPush_status()) {
            //关闭推送并设置关闭的回调处理
            mPushAgent.disable(mDisableCallback);
        } else {
            //开启推送并设置开启的回调处理
            mPushAgent.enable(mEnableCallback);
        }
    }

    private void updateStatus() {
        item_push_setting.setTVRightTxt(((MainApplication) this.getApplication()).isPush_status() ? "已开启" : "已关闭", R.color.color_333);
    }

    //此处是开启的回调处理
    public IUmengCallback mEnableCallback = new IUmengCallback() {

        @Override
        public void onSuccess() {
            MessageSharedPrefs.getInstance(SystemSettingsActivity.this).setIsEnabled(true);
            ((MainApplication) SystemSettingsActivity.this.getApplication()).setPush_status(true);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
            item_push_setting.setClickable(true);
        }

        @Override
        public void onFailure(String s, String s1) {
            item_push_setting.setClickable(true);
        }
    };

    //此处是关闭的回调处理
    public IUmengCallback mDisableCallback = new IUmengCallback() {

        @Override
        public void onSuccess() {
            ((MainApplication) SystemSettingsActivity.this.getApplication()).setPush_status(false);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateStatus();
                }
            });
            item_push_setting.setClickable(true);
        }

        @Override
        public void onFailure(String s, String s1) {
            item_push_setting.setClickable(true);
        }
    };
}

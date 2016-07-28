package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.ShareContent;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.utils.DataCleanUtil;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.CustomShareView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/25 13:33
 */
public class SystemSettingsActivity extends BaseActivity{
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
    @Bind(R.id.item_feedback)
    CustomItemLayout item_feedback;
    @Bind(R.id.item_about_us)
    CustomItemLayout item_about_us;
    @Bind(R.id.item_share)
    CustomItemLayout item_share;
    public SystemSettingsActivity(){
        super(R.layout.activity_system_settings);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"系统设置");
        item_update_psd.setTVStyle(0,"修改密码", R.color.color_333);
        item_push_setting.setTVStyle(0,"推送设置", R.color.color_333);
        item_clear_cache.setTVStyle(0, "清空缓存", R.color.color_333);
        item_to_comment.setTVStyle(0,"去评价", R.color.color_333);
        item_welcome_page.setTVStyle(0, "欢迎页", R.color.color_333);
        item_about_us.setTVStyle(0, R.string.about_us, R.color.color_333);
        item_feedback.setTVStyle(0, R.string.feed_back, R.color.color_333);
        item_share.setTVStyle(0,"分享给好友", R.color.color_333);
        setCacheSize();
//        LogUtil.e("getCacheDir",getCacheDir().getAbsolutePath());
//        LogUtil.e("getCacheDirLen",getCacheDir().length()+"");
//        LogUtil.e("getExternalCacheDir",getExternalCacheDir().getAbsolutePath());
//        LogUtil.e("getExternalCacheDirLen",getExternalCacheDir().length()+"");
//        LogUtil.e("ImageLoaderCache",ImageLoader.getInstance().getDiskCache().getDirectory().getAbsolutePath());
    }

    @OnClick({R.id.item_update_psd, R.id.btn_logout, R.id.item_clear_cache, R.id.item_to_comment, R.id.item_welcome_page, R.id.item_about_us, R.id.item_feedback, R.id.item_share, R.id.item_exit})
    void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.item_update_psd:
                startActivity(new Intent(activity,UpdatePasswordActivity.class));
                break;
            case R.id.item_clear_cache:
                new MyAsyncTask().execute();
                break;
            case R.id.item_to_comment:
                Uri uri = Uri.parse("market://details?id="+getPackageName());
                intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.item_welcome_page:
                intent=new Intent(activity,UserGuideActivity.class);
                intent.putExtra(getClass().getSimpleName(),getClass().getSimpleName());
                startActivity(intent);
                break;
            case R.id.item_about_us:
                String url= NetworkConstance.BASE_URL+"/view/about";
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(),url);
                intent.putExtra(AboutUsActivity.class.getName(),"关于我们");
                startActivity(intent);
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity,FeedbackActivity.class));
                break;
            case R.id.item_share:
                ShareContent content = new ShareContent();
                content.shareTxt = getResources().getString(R.string.share_txt);
                content.titleUrl = getResources().getString(R.string.title_url);
                content.site = getResources().getString(R.string.app_name);
                content.siteUrl = "http://www.taihuoniao.com/";
                PopupWindowUtil.show(activity, new CustomShareView(activity, content));
                break;
            case R.id.item_exit:
                logout();
                break;
        }
    }

    private void logout() {
        ClientDiscoverAPI.logout(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()){//   退出成功跳转首页
                    ToastUtils.showSuccess("退出成功");
                }
                SPUtil.remove(DataConstants.LOGIN_INFO);
                Intent intent=new Intent(activity,MainActivity.class);
                intent.putExtra(IndexFragment.class.getSimpleName(),IndexFragment.class.getSimpleName());
                intent.putExtra("exit", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG,s);
            }
        });
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

    private void setCacheSize(){
        try {
            item_clear_cache.setTvArrowLeftStyle(true, DataCleanUtil.getTotalCacheSize(activity), R.color.color_af8323);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

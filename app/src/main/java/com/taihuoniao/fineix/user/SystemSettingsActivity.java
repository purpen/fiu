package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.DataCleanUtil;
import com.taihuoniao.fineix.utils.FileUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PopupWindowUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.CustomShareView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

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
    private SVProgressHUD svProgressHUD;
    public SystemSettingsActivity(){
        super(R.layout.activity_system_settings);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"系统设置");
        svProgressHUD=new SVProgressHUD(this);
        item_update_psd.setTVStyle(0,"修改密码", R.color.color_333);
        item_push_setting.setTVStyle(0,"推送设置", R.color.color_333);
        item_clear_cache.setTVStyle(0,"清空缓存", R.color.color_333);
        item_to_comment.setTVStyle(0,"去评价", R.color.color_333);
        item_welcome_page.setTVStyle(0,"欢迎界面", R.color.color_333);
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

    @OnClick({R.id.item_update_psd,R.id.btn_logout,R.id.item_clear_cache,R.id.item_to_comment,R.id.item_welcome_page,R.id.item_about_us,R.id.item_feedback,R.id.item_share})
    void onClick(View view){
        Intent intent=null;
        switch (view.getId()){
            case R.id.item_update_psd:
                startActivity(new Intent(activity,UpdatePasswordActivity.class));
                break;
            case R.id.btn_logout:
                logout();
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
                String url="http://m.taihuoniao.com/app/api/view/about";
                intent = new Intent(activity, AboutUsActivity.class);
                intent.putExtra(AboutUsActivity.class.getSimpleName(),url);
                intent.putExtra(AboutUsActivity.class.getName(),"关于我们");
                startActivity(intent);
                break;
            case R.id.item_feedback:
                startActivity(new Intent(activity,FeedbackActivity.class));
                break;
            case R.id.item_share:
                PopupWindowUtil.show(activity,new CustomShareView(activity));
                //TODO 分享
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
                    svProgressHUD.showSuccessWithStatus("退出成功");
                }
                SPUtil.remove(activity,DataConstants.LOGIN_INFO);
                Intent intent=new Intent(activity,MainActivity.class);
                intent.putExtra(IndexFragment.class.getSimpleName(),IndexFragment.class.getSimpleName());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG,s);
            }
        });
    }

    private class MyAsyncTask extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                DataCleanUtil.cleanAppData(activity, FileUtils.getSavePath(getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            setCacheSize();
            svProgressHUD.showSuccessWithStatus("清理完成");
        }
    }

    private void setCacheSize(){
        try {
            item_clear_cache.setTvArrowLeftStyle(true,DataCleanUtil.getTotalCacheSize(activity),R.color.color_333);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

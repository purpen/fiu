package com.taihuoniao.fineix.network;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.BuildConfig;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.bean.CheckVersionBean;
import com.taihuoniao.fineix.network.bean.UpdateInfoBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;
import com.taihuoniao.fineix.zxing.activity.CaptureActivityZxing;
import com.yanzhenjie.permission.AndPermission;

import java.io.File;
import java.util.HashMap;

import static com.taihuoniao.fineix.utils.Constants.REQUEST_PHONE_STATE_CODE;

/**
 * Created by Stephen on 2016/12/6 15:05
 * Email: 895745843@qq.com
 *  code 状态：0.正常；1.提醒升级；2.强制升级；
 */

public class NetWorkUtils {
    private static final int INSTALL_APK = 10001;
    private static final int UPDATE_APK  = 10002;

    private Context mContext;
    private String downloadUrl;
    private String versionName;

    public NetWorkUtils(Context context) {
        this.mContext = context;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    break;
                case 1:
                    hintUpdate(null);
                    break;
                case 2:
                    if (isWifi(mContext)) {
                        downloadApkAndUpdate(false);
                    }
                    break;
                case UPDATE_APK:
                    break;
                case INSTALL_APK:
                    hintUpdate(versionName);
                    break;
            }
            return false;
        }
    });

    private void hintUpdate(String versionName) {
        String title = App.getString(R.string.hint_dialog_new_version_title);
        if (!TextUtils.isEmpty(versionName)) {
            title = TextUtils.replace(App.getString(R.string.hint_dialog_new_version_title2), new String[] {"$"}, new CharSequence[]{versionName}).toString();
        }
        new DefaultDialog(mContext, title, App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
            @Override
            public void clickRight() {
                if (AndPermission.hasPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    downloadApkAndUpdate(true);
                } else {
                    AndPermission.with((Activity)mContext).requestCode(REQUEST_PHONE_STATE_CODE).permission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE).send();
                }

            }
        });
    }

    /**
     * 下载更新
     * @param showProgressDialog 是否显示进度条
     */
    private void downloadApkAndUpdate(boolean showProgressDialog) {
        if (TextUtils.isEmpty(downloadUrl)) {
            if (showProgressDialog) {
                ToastUtils.showInfo("下载链接无效！");
            }
            return;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() +  "/downLoad";
        File folder = new File(path);
        if (!folder.isFile()) {
            folder.mkdir();
        }
        String fileName = "D3IN_" + System.currentTimeMillis() + ".apk";
        File apkFile = new File(path, fileName);
        new DownLoadTask(mContext, apkFile, mHandler, showProgressDialog).execute(downloadUrl + "?timestamp=" + System.currentTimeMillis());
    }

    static void installApk(File apkFile, Context context) {
        try {
            if (!apkFile.exists()) {
                return;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回当前程序版本名
     */
    private String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public void updateToLatestVersion() {
        HashMap<String, String> params = ClientDiscoverAPI.getupdateToLatestVersionRequestParams();
        HttpRequest.post(params,URL.FETCH_LATEST_VERSION, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                try {
                    UpdateInfoBean updateVersionInfo = JsonUtil.fromJson(json, new TypeToken<HttpResponse<UpdateInfoBean>>() {});
                    if (getAppVersionName(mContext).equals(updateVersionInfo.getVersion())) {
                        ToastUtils.showInfo("您当前已经是最新版本");
                    } else {
                        downloadUrl = updateVersionInfo.getDownload();
                        versionName = updateVersionInfo.getVersion();
                        mHandler.sendEmptyMessage(INSTALL_APK);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    public void checkVersionInfo() {
        String appVersionName = getAppVersionName(mContext);
        HashMap<String, String> params = ClientDiscoverAPI.getcheckVersionInfoRequestParams(appVersionName);
        HttpRequest.post(params, URL.CHECK_VERSION_INFO, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                try {
                    CheckVersionBean checkVersionBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<CheckVersionBean>>() {});
                    if (checkVersionBean != null) {
                        downloadUrl = checkVersionBean.getDownload();
                        mHandler.sendEmptyMessage(checkVersionBean.getCode());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    /**
     * make true current connect service is wifi
     * @param mContext context`
     * @return
     */
    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}

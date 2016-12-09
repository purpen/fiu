package com.taihuoniao.fineix.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.common.EnvironmentUtilities;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.bean.CheckVersionBean;
import com.taihuoniao.fineix.network.bean.UpdateInfoBean;
import com.taihuoniao.fineix.utils.DateUtils;
import com.taihuoniao.fineix.utils.DialogHelp;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Stephen on 2016/12/6 15:05
 * Email: 895745843@qq.com
 *  code 状态：0.正常；1.提醒升级；2.强制升级；
 */

public class NetWorkUtils {
    public static final int INSTALL_APK = 10001;
    public static final int UPDATE_APK  = 10002;

    private Context mContext;

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
                    hintUpdate();
                    break;
                case 2:
                    if (isWifi(mContext)) {
                        downloadApkAndUpdate(false);
                    }
                    break;
                case UPDATE_APK:
                    installApk();
                    break;
                case INSTALL_APK:
                    hintUpdate();
                    break;
            }
            return false;
        }
    });
    private File apkFile;

    private void hintUpdate() {
        AlertDialog.Builder dialog = DialogHelp.getSelectDialog(mContext, "版本更新(" + getAppVersionName(mContext) + ")", new String[]{"确定"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    downloadApkAndUpdate(true);
                }
            }
        });
        dialog.create().show();
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
        if (!folder.exists()) {
            folder.mkdir();//If there is no folder it will be created.
        }
        String fileName = "fiu_" + System.currentTimeMillis() + ".apk";
        apkFile = new File(path, fileName);
        Log.e("TAG", "-------------> apkFile.getAbsolutePath(): " + apkFile.getAbsolutePath());
        new DownLoadTask(mContext,apkFile.getAbsolutePath(), mHandler, showProgressDialog).execute(downloadUrl + "?timestamp=" + System.currentTimeMillis());
    }

    private void installApk() {
        try {
            if (!apkFile.exists()) {
                return;
            }
            // 通过Intent安装APK文件
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setDataAndType(Uri.parse("file://" + apkFile.toString()), "application/vnd.android.package-archive");
            mContext.startActivity(i);
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
        ClientDiscoverAPI.updateToLatestVersion(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    if (responseInfo == null) {
                        return;
                    }
                    UpdateInfoBean updateVersionInfo = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<UpdateInfoBean>>() {
                    });
                    if (getAppVersionName(mContext).equals(updateVersionInfo.getVersion())) {
                        ToastUtils.showInfo("您当前已经是最新版本");
                    } else {
                        downloadUrl = updateVersionInfo.getDownload();
                        mHandler.sendEmptyMessage(INSTALL_APK);
                    }
                } catch (Exception e) {
                    LogUtil.e(this.getClass().getSimpleName(), "<<<<< " + responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private String downloadUrl;

    public void checkVersionInfo() {
        String appVersionName = getAppVersionName(mContext);
        ClientDiscoverAPI.checkVersionInfo(appVersionName, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                try {
                    if (responseInfo == null) {
                        return;
                    }
                    CheckVersionBean
                            checkVersionBean = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<CheckVersionBean>>() {});
                    if (checkVersionBean != null) {
                        downloadUrl = checkVersionBean.getDownload();
                        mHandler.sendEmptyMessage(checkVersionBean.getCode());
                    }
                } catch (Exception e) {
                    LogUtil.e(this.getClass().getSimpleName(), "<<<<< " + responseInfo.result);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    /**
     * make true current connect service is wifi
     * @param mContext
     * @return
     */
    private static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }
}

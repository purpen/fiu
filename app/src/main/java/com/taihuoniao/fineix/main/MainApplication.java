package com.taihuoniao.fineix.main;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Vibrator;
import android.support.multidex.MultiDex;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.taihuoniao.fineix.BuildConfig;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.FileCameraUtil;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.PushUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.io.File;
import java.util.List;


/**
 * Created by taihuoniao on 2016/3/14.
 * ¥
 * 上线之前检查WriteJSONToSD
 * 在客户端scene是场景，qingjing是情景，而在服务器端sight是场景，scene是情景
 * 检查在DataConstants.NETWORK_FAILURE情况下dialog是否隐藏
 * 检查所有的Toast，删除没用的提示
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    public Vibrator mVibrator;
    public static int which_activity;//判断是从哪个界面跳转到登录界面,0是默认从主页面跳
    private DisplayMetrics displayMetrics = null;
    public static String systemPhotoPath = null;//系统相册路径
    public static boolean hasUser;
    //编辑好的图片标签的list
    public static List<TagItem> tagInfoList;
    //上传图片的时候的最大限制
    public static final long MAXPIC = 1024 * 1024;
    public static Bitmap cropBitmap = null;//前切好的图片
    public static Bitmap editBitmap = null;//编辑好的图片
    public static Bitmap blurBitmap = null;//模糊的图片
    public static String subjectId = null;//活动id
    public static String zoneId = null; //地盘id
    public static List<SceneListBean2.RowsEntity> sceneList;//情景小图跳转大图列表 情景
    public static List<String> picList;//跳转PictureActivity
    private PushAgent mPushAgent;
    private SharedPreferences tempSharedPreference;//检查本地存储是否设置推送的开关

    public static MainApplication getContext() {
        return instance;
    }


    @Override
    public void onCreate() {
        LogUtil.e("taihuoniao", "---------->onCreate()" + getClass().getSimpleName());
        super.onCreate();

        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        MobclickAgent.setDebugMode(BuildConfig.LOG_DEBUG);
        instance = this;
        FileCameraUtil.init();
        JsonUtil.init();
        systemPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
        initPush();
    }

    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    private void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }

    public String getFilesDirPath() {
        return getFilesDir().getAbsolutePath();
    }

    //获取应用的缓存目录
    public String getCacheDirPath() {
        return getCacheDisk().getAbsolutePath();
    }

    public boolean isSD() {
        if (Environment.MEDIA_MOUNTED.equals(Environment .getExternalStorageState())) {
            File file = getExternalCacheDir();
            if (file != null && file.canWrite()) {
                return true;
            }
        }
        return false;
    }

    //获取缓存文件
    public File getCacheDisk() {
        if (isSD()) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null) {
                if (cacheDir.canWrite()) {
                    return cacheDir;
                }
            }
        }
        return getCacheDir();
    }

    public void initPush() {
        tempSharedPreference = getSharedPreferences(DataConstants.PUSH_STATUS, Context.MODE_PRIVATE);
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setNotificationClickHandler(notificationClickHandler);
        //关闭打印log，提高代码效率
        mPushAgent.setDebugMode(false);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.e("<<<注册成功", "deviceToken=" + deviceToken);
                addUserId();
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.e("<<<注册失败", "s=" + s + ",s1=" + s1);
            }
        });
    }

    //绑定用户，方便友盟对指定用户id推送
    public void addUserId() {
        mPushAgent.addAlias(LoginInfo.getUserId() + "", DataConstants.FIU, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                LogUtil.e("<<<绑定用户", "userId=" + LoginInfo.getUserId());
            }
        });
    }

    //移除用户
    public void removeUserId() {
        mPushAgent.addAlias(LoginInfo.getUserId() + "", DataConstants.FIU, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {

            }
        });
    }

    //自定义点击通知栏动作
    UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
        @Override
        public void dealWithCustomAction(Context context, UMessage msg) {
            if (msg.extra.containsKey(DataConstants.TYPE) && msg.extra.containsKey(DataConstants.TARGET_ID)) {
                String type = msg.extra.get(DataConstants.TYPE);
                String target_id = msg.extra.get(DataConstants.TARGET_ID);
                PushUtils.switchActivity(type, target_id, MainApplication.this);
            }
        }
    };

    public boolean isPush_status() {
        return tempSharedPreference.getBoolean(DataConstants.STATUS, true);
    }

    public boolean setPush_status(boolean push_status) {
        return tempSharedPreference.edit().putBoolean(DataConstants.STATUS, push_status).commit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}

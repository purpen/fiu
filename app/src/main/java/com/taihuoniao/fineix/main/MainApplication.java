package com.taihuoniao.fineix.main;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.QingjingDetailBean;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.beans.UsedLabelBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.service.LocationService;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by taihuoniao on 2016/3/14.
 * ¥
 * 上线之前检查Log.e("<<<") 和 WriteJSONToSD
 * 在客户端scene是场景，qingjing是情景，而在服务器端sight是场景，scene是情景
 * 检查在DataConstants.NETWORK_FAILURE情况下dialog是否隐藏
 * 检查所有的Log和Toast，删除没用的提示,并改成DialogUtils
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    public LocationService locationService;
    public Vibrator mVibrator;
    public static int which_activity;//0是默认从主页面跳
    private DisplayMetrics displayMetrics = null;
    public static String systemPhotoPath = null;//系统相册路径
    //剪切好的图片存储路径
    public static String cropPicPath = null;
    public static String editPicPath = null;//编辑好的图片存储路径
    public static String filterPicPath = null;//加完滤镜的图片
    public static String uuid = null;
    //编辑好的图片标签的list
    public static List<TagItem> tagInfoList;
    //用户选择的标签列表
    public static List<UsedLabelBean> selectList;
    //创建场景或情景的标识
    public static int tag;//1,场景 2,情景
    //在哪个情景下创建场景
    public static QingjingDetailBean whichQingjing = null;
    //上传图片的时候的最大限制
    public static final int MAXPIC = 1024 * 1024;
    //分享场景时编辑的图片Bitmap
    public static Bitmap shareBitmap = null;
    // SharedPreference 中保存的手机号
    public static final String THN_MOBILE = "mobile";
    // SharedPreference 中保存的密码
    public static final String THN_PASSWORD = "password";

    public static MainApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
        instance = this;
        initImageLoader();
        JsonUtil.init();
        uuid = getMyUUID();
        systemPhotoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera";
        cropPicPath = getCacheDirPath() + "/crop";
        editPicPath = getCacheDirPath() + "/edit";
        filterPicPath = getCacheDirPath() + "/filter";
    }

    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        Log.e("<<<", "屏幕高度=" + this.displayMetrics.heightPixels);
        return this.displayMetrics.heightPixels;
    }

    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(getResources().getDisplayMetrics());
        }
        Log.e("<<<", "屏幕宽度=" + this.displayMetrics.widthPixels);
        return this.displayMetrics.widthPixels;
    }

    private void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.mipmap.ic_launcher)
//                .showImageOnLoading(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .considerExifParams(true)
//                .displayer(new FadeInBitmapDisplayer(500))
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(defaultOptions)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(StorageUtils.getCacheDirectory(this)))
                .diskCacheSize(100 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache()).memoryCacheSize(4 * 1024 * 1024)
                .threadPoolSize(5)
                .build();
//        ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(
//                this)
//                .memoryCacheExtraOptions(480, 800)
//                // max width, max height，即保存的每个缓存文件的最大长宽
//                .discCacheExtraOptions(480, 800, Bitmap.CompressFormat.PNG, 100, null)
//                // 设置缓存的详细信息，最好不要设置这个
//                .threadPoolSize(5)
//                        // 线程池内加载的数量
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
//                        // 你可以通过自己的内存缓存实现
//                .memoryCacheSize(4 * 1024 * 1024)
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                        // 将保存的时候的URI名称用MD5 加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .diskCacheFileCount(100)
//                        // 缓存的文件数量
//                .diskCache(new UnlimitedDiskCache(MainApplication.this.getCacheDir()))
//                        // 自定义缓存路径
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                .imageDownloader(
//                        new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
//                .build();// 开始构建
        ImageLoader.getInstance().init(config);
    }

    //获取应用的data/data/....File目录
    public String getFilesDirPath() {
        return getFilesDir().getAbsolutePath();
    }

    //获取应用的缓存目录
    public String getCacheDirPath() {
        return getCacheDisk().getAbsolutePath();
    }

    //获取缓存文件
    public File getCacheDisk() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            File cacheDir = getExternalCacheDir();
            if (cacheDir != null) {
                if (cacheDir.canWrite()) {
                    return cacheDir;
                }
            }
        }
        return getCacheDir();
    }

    //UUID+设备号序列号 唯一识别码（不可变）
    private String getMyUUID() {
        final TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }


    public static boolean isloginValid(String json, Class clazz) {
        if (TextUtils.isEmpty(json)) return false;
        if (clazz.equals(HttpResponse.class)) {
            HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
            if (TextUtils.equals(NetworkConstance.STATUS_NEED_LOGIN, response.getStatus())) {//需要登录
                SPUtil.remove(getContext(), DataConstants.LOGIN_INFO);
                getContext().startActivity(new Intent(getContext(), OptRegisterLoginActivity.class));
                return false;
            }
        } else {
            NetBean netBean = JsonUtil.fromJson(json, NetBean.class);
            if (TextUtils.equals(NetworkConstance.STATUS_NEED_LOGIN, netBean.getStatus())) {//需要登录
                SPUtil.remove(getContext(), DataConstants.LOGIN_INFO);
                getContext().startActivity(new Intent(getContext(), OptRegisterLoginActivity.class));
                return false;
            }
        }
        return true;
    }
}

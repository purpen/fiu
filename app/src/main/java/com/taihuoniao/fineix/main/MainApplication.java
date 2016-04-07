package com.taihuoniao.fineix.main;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.service.LocationService;
import com.taihuoniao.fineix.utils.JsonUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * Created by taihuoniao on 2016/3/14.
 * ¥
 */
public class MainApplication extends Application {
    private static MainApplication instance;
    public LocationService locationService;
    public Vibrator mVibrator;

    private DisplayMetrics displayMetrics = null;
    public static String systemPhotoPath = null;//系统相册路径
    //剪切好的图片存储路径
    public static String cropPicPath = null;
    public static String editPicPath = null;//编辑好的图片存储路径
    public static String uuid = null;
    //编辑好的图片标签的list
    public static List<TagItem> tagInfoList;

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


    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.EXACTLY)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCache(new UnlimitedDiskCache(StorageUtils.getOwnCacheDirectory(this, "/fiu/image")))
                .diskCacheSize(100 * 1024 * 1024).tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).memoryCacheSize(2 * 1024 * 1024)
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
}

package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.network.BufferedByteArrayOutputStream;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ConstantCfg;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.taihuoniao.fineix.utils.ImageUtils.getSmallBitmap;

public class Util {

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }

    public static String getImei(Context context) {
        TelephonyManager phoneMgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return phoneMgr.getDeviceId();
    }

    public static String getChannelID(Context context) {

        Bundle metaData = null;
        String metaValue = null;

        if (context == null) {
            return null;
        }
        try {

            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);

            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                metaValue = metaData.getString("UMENG_CHANNEL");
            }

        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return metaValue;// xxx

    }

    public static void makeToast(String content) {
        Toast result = new Toast(MainApplication.getContext());
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(Toast.LENGTH_SHORT);
        View v = inflateView(R.layout.transient_notification, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(content);
        result.setView(v);
        result.show();
    }

    public static void makeToast(Context context, String content) {
        Toast result = new Toast(context);
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(Toast.LENGTH_SHORT);
        View v = inflateView(R.layout.transient_notification, null);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(content);
        result.setView(v);
        result.show();
    }

    public static void makeToast(Context context, int resId) {
        makeToast(context, context.getString(resId));
    }

    public static float getScreenHeightDPI() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.ydpi;
    }

    public static float getScreenWidthDPI() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.xdpi;
    }

    public static int getScreenWidth() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }


    public static int getScreenHeight() {
        WindowManager wm = (WindowManager) MainApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static View inflateView(int resourceId, ViewGroup root) {
        LayoutInflater inflater = (LayoutInflater) MainApplication.getContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resourceId, root);
    }

    public static View inflateView(Activity activity, int resourceId, ViewGroup root) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resourceId, root);
    }

    public static int getScaleHeight(Activity activity, int w, int h) {

        return getScreenWidth() * h / w;
    }

    public static void traverseFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                LogUtil.e("traverseFolder", "文件夹是空的");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        LogUtil.e("文件夹:", file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        LogUtil.e("文件：", file2.getAbsolutePath());
                    }
                }
            }
        } else {
            LogUtil.e("文件：", "文件不存在");
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length == 0) {
                LogUtil.e("traverseFolder", "文件夹是空的");
            } else {
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        LogUtil.e("文件夹:", file2.getAbsolutePath());
                        traverseFolder(file2.getAbsolutePath());
                    } else {
                        LogUtil.e("删除文件：", file2.getAbsolutePath());
                        file2.delete();
                    }
                }
            }
            file.delete();
        } else {
            LogUtil.e("文件：", "文件不存在");
        }
    }


    public static String replaceChinese2UTF8(String url) {
        Pattern pattern = Pattern.compile("[\u3400-\u9FFF]+");
        Matcher matcher = pattern.matcher(url);
        while (matcher.find()) {
            try {
                String str = matcher.group();
                url = url.replace(str, URLEncoder.encode(str, Constants.CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    public static File saveBitmapToFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(),
                "tmp_avatar" + ".png");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fops = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fops);
            fops.flush();
            fops.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap.recycle();
        }
        return file;
    }

    public static String saveBitmap2Base64Str(Bitmap bitmap, int maxSize) {
        if (bitmap == null) return null;
        String imgStr = null;
        try {
            Matrix matrix = new Matrix();
            float scale = getScaleSize(bitmap,maxSize);
            matrix.setScale(scale,scale);
            bitmap=bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            ByteArrayOutputStream baos = new BufferedByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
            byte[] byteArr=baos.toByteArray();
            imgStr = Base64.encodeToString(byteArr, Base64.DEFAULT);
            baos.flush();
            baos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgStr;
    }

    private static float getScaleSize(Bitmap bitmap,int maxSize){
        float width = bitmap.getWidth();
        float height = bitmap.getHeight();
        if (width<maxSize || height<maxSize){
            return 1;
        }else {
            float scaleX;
            float scaleY;
            scaleX = maxSize/width;
            scaleY = maxSize/height;
            return scaleX>scaleY?scaleY:scaleX;
        }
    }

    public static String formatDouble(String price) throws NumberFormatException {
        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        return decimalFormat.format(Double.parseDouble(price));
    }

    public static String formatFloat(float price) throws NumberFormatException {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(price);
    }

    /**
     * @param key
     * @return
     */
    public static String getAppMetaData(String key) {
        Context context = MainApplication.getContext();
        String defaultValue = "10"; //默认官方渠道下载
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo.metaData != null) {
                if (applicationInfo.metaData.containsKey(key)) {
                    resultData = applicationInfo.metaData.getInt(key) + "";
                } else {
                    resultData = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(resultData)) {
            return defaultValue;
        }
        return resultData;
    }

    public static String getEncodeStr(String string) throws UnsupportedEncodingException {
        return URLEncoder.encode(string, ConstantCfg.CHARSET);
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersionName() throws NameNotFoundException {
        PackageManager manager = MainApplication.getContext().getPackageManager();
        PackageInfo info = manager.getPackageInfo(MainApplication.getContext().getPackageName(), 0);
        return info.versionName;
    }

    public static final String getUUID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static final List<String> getPackageNames(Context context) {
        ArrayList<String> list = new ArrayList<>();
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(PackageManager.GET_ACTIVITIES);
        for (PackageInfo packageInfo : packageInfos) {
            list.add(packageInfo.packageName);
        }
        return list;
    }

    public static boolean isExternalStorageStateMounted() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    //把bitmap转换成String
     public static String bitmap2String(Bitmap bitmap) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
         byte[] b = baos.toByteArray();
         return Base64.encodeToString(b, Base64.DEFAULT);
     }
}

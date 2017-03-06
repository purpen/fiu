package com.taihuoniao.fineix.utils;

import android.util.Log;

/**
 * @author lilin
 *         created at 2016/3/24 14:58
 */
public class LogUtil {
    private static final String TAG = "com.taihuoniao.fineix";
    private static final boolean DEV_MODE = false;

    public static void i(String tag, String msg) {
        if (DEV_MODE) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEV_MODE) {
            Log.d(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEV_MODE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEV_MODE) {
            Log.e(tag, msg);
        }
    }
     public static void e(String msg) {
            if (DEV_MODE) {
                Log.e(TAG, msg);
            }
        }
}

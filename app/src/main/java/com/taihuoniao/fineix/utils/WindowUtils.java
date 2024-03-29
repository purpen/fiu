package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taihuoniao.fineix.R;

/**
 * Created by taihuoniao on 2016/4/14.
 */
public class WindowUtils {
    //状态栏变为黑色
    public static void chenjin(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.black));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    //透明状态栏
    public static void showStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    //隐藏状态栏
    public static void hide(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
    }

    //显示状态栏
    public static void show(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
    }
}

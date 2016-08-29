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

    public static void showStatusBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏

//            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }


}

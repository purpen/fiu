package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Intent;

import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class LoginCompleteUtils {
    public static String id;//用来记录登录之前界面的id

    public static void goFrom(Activity activity) {
        switch (MainApplication.which_activity) {
            case DataConstants.QingjingDetailActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadQingjingDetail));
                break;
            case DataConstants.SceneDetailActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadSceneDetail));
                break;
            case DataConstants.ElseActivity:
                //其他不需要做任何活动的界面的activity，只需要正常返回就行
                break;
            default:
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}

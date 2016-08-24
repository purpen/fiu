package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Intent;

import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;

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
            case DataConstants.FindFragment:
                activity.sendBroadcast(new Intent(DataConstants.BroadFind));
                Intent intent2 = new Intent(activity, MainActivity.class);
                intent2.putExtra(FindFragment.class.getSimpleName(), false);
                activity.startActivity(intent2);
                break;
            case DataConstants.IndexFragment:
                activity.sendBroadcast(new Intent(DataConstants.BroadIndex));
                Intent intent1 = new Intent(activity, MainActivity.class);
                intent1.putExtra(IndexFragment.class.getSimpleName(), false);
                activity.startActivity(intent1);
                break;
            case DataConstants.SearchActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadSearch));
                Intent intent3 = new Intent(activity, SearchActivity.class);
                activity.startActivity(intent3);
                break;
            default:
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}

package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Intent;

import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.FindActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJCategoryActivity;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class LoginCompleteUtils {

    public static void goFrom(Activity activity) {
        switch (MainApplication.which_activity) {
            case DataConstants.GoodDetailsActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadGoodDetails));
                break;
            case DataConstants.QJCategoryActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadQJCategory));
                Intent intent3 = new Intent(activity, QJCategoryActivity.class);
                activity.startActivity(intent3);
                break;
            case DataConstants.BuyGoodDetailsActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                break;
            case DataConstants.ActivityDetail:
                activity.sendBroadcast(new Intent(DataConstants.BroadSceneActivityDetail));
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
                break;
            case DataConstants.FindActivity:
                Intent intent4 = new Intent(activity, FindActivity.class);
                activity.startActivity(intent4);
                break;
            case DataConstants.QJDetailActivity:
                activity.sendBroadcast(new Intent(DataConstants.BroadQJDetail));
                break;
            default:
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                break;
        }
    }
}

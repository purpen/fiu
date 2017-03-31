package com.taihuoniao.fineix.utils;

import android.content.Context;
import android.content.Intent;

import com.taihuoniao.fineix.home.GoToNextUtils;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;

/**
 * Created by taihuoniao on 2016/10/14.
 */

public class PushUtils {


    //选择跳转界面
    public static void switchActivity(String type, String target_id, Context context) {
        Intent intent = new Intent();
        switch (type) {
            case "1":
                intent.setClass(context, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", target_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "2":
                GoToNextUtils.jump2ThemeDetail(context, target_id, true);
                break;
            case "11":
                intent.setClass(context, QJDetailActivity.class);
                intent.putExtra("id", target_id);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
            case "13":
                intent.setClass(context, UserCenterActivity.class);
                long userId = Long.parseLong(target_id);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, userId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                break;
        }
    }


}

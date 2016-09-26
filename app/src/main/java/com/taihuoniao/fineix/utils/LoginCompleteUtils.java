package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ThirdLogin;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.FindActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJCategoryActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.user.ToLoginActivity;
import com.taihuoniao.fineix.user.ToRegisterActivity;

import static android.R.attr.button;

/**
 * Created by taihuoniao on 2016/5/25.
 */
public class LoginCompleteUtils {
    private static PopupWindow popupWindow;

    public static void goFrom(Activity activity, LoginInfo loginInfo, ThirdLogin thirdLogin) {
        int is_bonus = 0;
        if (loginInfo != null) {
            is_bonus = loginInfo.getIs_bonus();
        } else if (thirdLogin != null) {
            is_bonus = thirdLogin.user.is_bonus;
        } else {
            is_bonus = 0;
        }
        if (is_bonus == 1) {
            if (popupWindow == null) {
                initPop(activity);
            }
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.alpha = 0.4f;
            activity.getWindow().setAttributes(params);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            popupWindow.showAtLocation(popupWindow.getContentView(), Gravity.CENTER, 0, 0);
        } else {
            finishActivity(activity);
        }
    }

    private static void initPop(final Activity activity) {
        View popup_view = View.inflate(activity, R.layout.pop_login_bonus, null);
        ImageView cancelImg = (ImageView) popup_view.findViewById(R.id.cancel);
        popupWindow = new PopupWindow(popup_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.popup_style);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(params);
                finishActivity(activity);
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.color.nothing));
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    private static void finishActivity(Activity activity) {
        switch (MainApplication.which_activity) {
            case DataConstants.WellGoodsFragment:
                activity.sendBroadcast(new Intent(DataConstants.BroadWellGoods));
                Intent intent5 = new Intent(activity, MainActivity.class);
                intent5.putExtra(WellGoodsFragment.class.getSimpleName(), false);
                activity.startActivity(intent5);
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
        popupWindow = null;
        if (ToRegisterActivity.instance != null) {
            ToRegisterActivity.instance.finish();
        }
        if (OptRegisterLoginActivity.instance != null) {
            OptRegisterLoginActivity.instance.finish();
        }
        if (ToLoginActivity.instance != null) {
            ToLoginActivity.instance.finish();
        }
        activity.finish();
        activity = null;
    }
}

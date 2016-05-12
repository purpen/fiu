package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;

/**
 * Created by taihuoniao on 2016/5/11.
 */
public class FirstInAppUtils {
    public static final int QING = 11111;
    public static final int JING = 22222;
    public static final int FIU = 33333;
    public static final int PIN = 44444;
    public static final int WO = 55555;
    private static int type = 0;//判断第一次进入的哪个界面
    private static Activity activity;
    private static PopupWindow popupWindow;

    public static void showPop(Activity activity1, int type1, View activity_view) {
        activity = activity1;
        type = type1;
        initPop();
        WindowManager.LayoutParams params = activity1.getWindow().getAttributes();
        params.alpha = 0.4f;
        activity1.getWindow().setAttributes(params);
        activity1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//这行代码可以使window后的所有东西边暗淡
        popupWindow.showAtLocation(activity_view, Gravity.BOTTOM, 0, DensityUtils.dp2px(activity1, 80));
    }

    private static void initPop() {
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        final View popView = View.inflate(activity, R.layout.pop_first_in, null);
        TextView titleTv = (TextView) popView.findViewById(R.id.pop_first_in_title);
        TextView desTv = (TextView) popView.findViewById(R.id.pop_first_in_des);
        Button btn = (Button) popView.findViewById(R.id.pop_first_in_btn);
        switch (type) {
            case QING:
                titleTv.setText(R.string.first_in_qing_title);
                desTv.setText(R.string.first_in_qing_des);
                break;
            case JING:
                titleTv.setText(R.string.first_in_jing_title);
                desTv.setText(R.string.first_in_jing_des);
                break;
            case FIU:
                titleTv.setText(R.string.first_in_fiu_title);
                desTv.setText(R.string.first_in_fiu_des);
                break;
            case PIN:
                titleTv.setText(R.string.first_in_pin_title);
                desTv.setText(R.string.first_in_pin_des);
                break;
            case WO:
                titleTv.setText(R.string.first_in_wo_title);
                desTv.setText(R.string.first_in_wo_des);
                break;
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow = new PopupWindow(popView, MainApplication.getContext().getScreenWidth()-DensityUtils.dp2px(activity,50), ViewGroup.MarginLayoutParams.WRAP_CONTENT, true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = activity.getWindow().getAttributes();
                params.alpha = 1f;
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                activity.getWindow().setAttributes(params);

            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(activity,
                R.color.nothing));
//        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//                // 这里如果返回true的话，touch事件将被拦截
//                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
//            }
//        });
    }


}

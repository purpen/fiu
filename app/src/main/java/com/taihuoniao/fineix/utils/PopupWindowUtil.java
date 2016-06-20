package com.taihuoniao.fineix.utils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
/**
 * @author lilin
 * created at 2016/4/26 18:39
 */
public class PopupWindowUtil {
    private static PopupWindow popupWindow;
    private static Activity activity;
    private static OnDismissListener windowListener;
    public interface OnDismissListener {
        void onDismiss();
    }

    public static void setListener(OnDismissListener listener){
        windowListener=listener;
    }
    public static void show(Activity activity,View view) {
        PopupWindowUtil.activity=activity;
        popupWindow= new PopupWindow(view,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(0.5f);
    }

    public static void show(Activity activity,View view, int gravity) {
        PopupWindowUtil.activity=activity;
        popupWindow= new PopupWindow(view,Util.getScreenWidth()*4/5,Util.getScreenHeight()/4);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view,gravity, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(0.5f);
    }

    private static void setWindowAlpha(float f){
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        window.setAttributes(lp);
    }


    private static PopupWindow.OnDismissListener dismissListener=new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setWindowAlpha(1f);
            if (windowListener!=null){
                windowListener.onDismiss();
                activity = null;
                popupWindow = null;
                windowListener = null;
            }
        }
    };

    public static void dismiss(){
        if (popupWindow!=null)
            popupWindow.dismiss();
    }
}

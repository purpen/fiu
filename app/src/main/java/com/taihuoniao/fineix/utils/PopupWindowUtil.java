package com.taihuoniao.fineix.utils;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;


/**
 * @author lilin
 *         created at 2016/4/26 18:39
 */
public class PopupWindowUtil {
    private static OnDismissListener windowListener;
    private static PopupWindow popupWindow;
    public interface OnDismissListener {
        void onDismiss();
    }

    public static void setListener(OnDismissListener listener) {
        windowListener = listener;
    }

    public static void show(Activity activity, View view) {
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(activity, 0.5f);
    }

    public static void show(Activity activity, View view, int gravity) {
        PopupWindow popupWindow = new PopupWindow(view, Util.getScreenWidth() * 4 / 5, Util.getScreenHeight() / 4);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, gravity, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(activity, 0.5f);
    }

    private static void setWindowAlpha(Activity activity, float f) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        window.setAttributes(lp);
    }

    private static PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            if (windowListener != null) {
                windowListener.onDismiss();
                PopupWindowUtil.windowListener = null;
            }
        }
    };


    public static void dismiss(Activity activity) {
        setWindowAlpha(activity, 1f);
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
}

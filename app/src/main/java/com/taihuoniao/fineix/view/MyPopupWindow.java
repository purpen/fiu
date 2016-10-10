package com.taihuoniao.fineix.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.taihuoniao.fineix.utils.Util;


/**
 * @author lilin
 *         created at 2016/4/26 18:39
 */
public class MyPopupWindow {
    private OnDismissListener windowListener;
    private Activity activity;
    private PopupWindow popupWindow;
    private View view;

    public MyPopupWindow(Activity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setListener(OnDismissListener listener) {
        this.windowListener = listener;
    }

    public void show() {
        if (popupWindow!=null) popupWindow.dismiss();
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(0.5f);
    }

    public void show(int gravity) {
        if (popupWindow!=null) popupWindow.dismiss();
        popupWindow = new PopupWindow(view, Util.getScreenWidth() * 4 / 5, Util.getScreenHeight() / 4);
        popupWindow.setAnimationStyle(android.R.style.Widget_PopupWindow);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.showAtLocation(view, gravity, 0, 0);
        popupWindow.setOnDismissListener(dismissListener);
        setWindowAlpha(0.5f);
    }

    private void setWindowAlpha(float f) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = f;
        if (f == 1) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }
        window.setAttributes(lp);
    }

    private PopupWindow.OnDismissListener dismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            setWindowAlpha(1f);
            if (windowListener != null) {
                windowListener.onDismiss();
            }
        }
    };


    public void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
            activity = null;
        }
    }
}

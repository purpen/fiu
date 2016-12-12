package com.taihuoniao.fineix.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.MainApplication;

/**
 * Created by taihuoniao on 2016/6/3.
 */
public class ToastUtils {
    private static int resInfo = R.drawable.ic_svstatus_info;
    private static int resSuccess = R.drawable.ic_svstatus_success;
    private static int resError = R.drawable.ic_svstatus_error;

    private static Toast toast;
    private static ImageView smallImg;
    private static TextView textView;

    private static void initToast() {
        if (toast == null) {
            toast = new Toast(MainApplication.getContext());
        }
        View view = View.inflate(MainApplication.getContext(), R.layout.view_svprogressdefault, null);
        toast.setView(view);
        ImageView imageView = (ImageView) view.findViewById(R.id.ivBigLoading);
        imageView.setVisibility(View.GONE);
        smallImg = (ImageView) view.findViewById(R.id.ivSmallLoading);
        smallImg.setVisibility(View.VISIBLE);
        textView = (TextView) view.findViewById(R.id.tvMsg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        textView.setVisibility(View.VISIBLE);
    }

    public static void showSuccess(String message) {
        showSuccess(message, Toast.LENGTH_SHORT);
    }

    public static void showSuccess(int resid) {
        showSuccess(resid, Toast.LENGTH_SHORT);
    }

    public static void showSuccess(String message, int duration) {
        initToast();
        smallImg.setImageResource(resSuccess);
        textView.setText(message);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showSuccess(int resid, int duration) {
        initToast();
        smallImg.setImageResource(resSuccess);
        textView.setText(resid);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showError(String message) {
        showError(message, Toast.LENGTH_SHORT);
    }

    public static void showError(int resid) {
        showError(resid, Toast.LENGTH_SHORT);
    }

    public static void showError(String message, int duration) {
        initToast();
        smallImg.setImageResource(resError);
        textView.setText(message);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showError(int resid, int duration) {
        initToast();
        smallImg.setImageResource(resError);
        textView.setText(resid);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showInfo(String message) {
        showInfo(message, Toast.LENGTH_SHORT);
    }

    public static void showInfo(int resid) {
        showInfo(resid, Toast.LENGTH_SHORT);
    }

    public static void showInfo(String message, int duration) {
        initToast();
        smallImg.setImageResource(resInfo);
        textView.setText(message);
        toast.setDuration(duration);
        toast.show();
    }

    public static void showInfo(int resid, int duration) {
        initToast();
        smallImg.setImageResource(resInfo);
        textView.setText(resid);
        toast.setDuration(duration);
        toast.show();
    }
}

package com.taihuoniao.fineix.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.taihuoniao.fineix.R;

/**
 * Created by taihuoniao on 2016/1/21.
 */
public class WaittingDialog extends Dialog {
    private int resBigLoading = R.drawable.ic_sv_loading;
    private int resInfo = R.drawable.ic_svstatus_info;
    private int resSuccess = R.drawable.ic_svstatus_success;
    private int resError = R.drawable.ic_svstatus_error;
    private ImageView ivBigLoading;/*, ivSmallLoading;
    private SVCircleProgressBar circleProgressBar;
    private TextView tvMsg;*/

    private RotateAnimation mRotateAnimation;
    public WaittingDialog(Context context) {
        this(context, R.style.custom_progress_dialog);
    }

    private WaittingDialog(Context context, int theme) {
        super(context, R.style.custom_progress_dialog);
        this.setContentView(R.layout.view_svprogressdefault);
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
//        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        initViews();
        init();
    }
    private void initViews() {
        ivBigLoading = (ImageView) findViewById(R.id.ivBigLoading);
//        ivSmallLoading = (ImageView) findViewById(R.id.ivSmallLoading);
//        circleProgressBar = (SVCircleProgressBar) findViewById(R.id.circleProgressBar);
//        tvMsg = (TextView) findViewById(R.id.tvMsg);
    }

    private void init() {
        mRotateAnimation = new RotateAnimation(0f, 359f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(1000L);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setRepeatMode(Animation.RESTART);
    }

    @Override
    public void show() {
        clearAnimations();
        super.show();
        ivBigLoading.startAnimation(mRotateAnimation);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        clearAnimations();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            dismiss();
        }
    }

    private void clearAnimations() {
        ivBigLoading.clearAnimation();
    }

}

package com.taihuoniao.fineix.view.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taihuoniao.fineix.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class LoadingLayout extends FrameLayout {

    static final int DEFAULT_ROTATION_ANIMATION_DURATION = 150;
    private final FrameLayout normalLayout;
    private final ImageView animImg;
    private final ImageView headerImage;
    private final ProgressBar headerProgress;
    private final TextView headerText;
    private final TextView headerTime;

    private String pullLabel;//下拉刷新
    private String refreshingLabel;//正在加载
    private String releaseLabel;//释放刷新

    private final Animation rotateAnimation, resetRotateAnimation;
    private final AnimationDrawable animationDrawable;

    public LoadingLayout(Context context, final int mode, String releaseLabel,
                         String pullLabel, String refreshingLabel) {
        super(context);
        ViewGroup header = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.pull_to_refresh_header, this);
        normalLayout = (FrameLayout) header.findViewById(R.id.pull_to_refresh_normal);
        animImg = (ImageView) header.findViewById(R.id.pull_to_refresh_anim);
        headerText = (TextView) header.findViewById(R.id.pull_to_refresh_text);
        headerTime = (TextView) header.findViewById(R.id.pull_to_refresh_time);
        headerImage = (ImageView) header
                .findViewById(R.id.pull_to_refresh_image);
        headerProgress = (ProgressBar) header
                .findViewById(R.id.pull_to_refresh_progress);

        final Interpolator interpolator = new LinearInterpolator();
        rotateAnimation = new RotateAnimation(0, -180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setInterpolator(interpolator);
        rotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        rotateAnimation.setFillAfter(true);

        resetRotateAnimation = new RotateAnimation(-180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        resetRotateAnimation.setInterpolator(interpolator);
        resetRotateAnimation.setDuration(DEFAULT_ROTATION_ANIMATION_DURATION);
        resetRotateAnimation.setFillAfter(true);

        this.releaseLabel = releaseLabel;
        this.pullLabel = pullLabel;
        this.refreshingLabel = refreshingLabel;

        switch (mode) {
            case PullToRefreshBase.MODE_PULL_UP_TO_REFRESH:
                headerImage.setRotation(180f);
                break;
            case PullToRefreshBase.MODE_PULL_DOWN_TO_REFRESH:
            default:
                headerImage.setImageResource(R.mipmap.goicon);
                headerImage.setRotation(0f);
                break;
        }
        animImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.pull_to_refresh_animation));
        animationDrawable = (AnimationDrawable) animImg.getDrawable();
    }

    //刷新完毕后显示刷新时间
    public void setLoadingTime() {
        headerTime.setText("上一次刷新：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis()));
    }

    //首页下拉动画的改变
    public void animLayout() {
        normalLayout.setVisibility(GONE);
        animImg.setVisibility(VISIBLE);
    }

    public void reset() {
        headerText.setText(pullLabel);
        headerImage.setVisibility(View.VISIBLE);
        headerProgress.setVisibility(View.GONE);
        if (animationDrawable.isRunning()) {
            animationDrawable.stop();
        }
    }

    public void releaseToRefresh() {
        headerText.setText(releaseLabel);
        headerImage.clearAnimation();
        headerImage.startAnimation(rotateAnimation);
    }

    public void setPullLabel(String pullLabel) {
        this.pullLabel = pullLabel;
    }

    public void refreshing() {
        headerText.setText(refreshingLabel);
        headerImage.clearAnimation();
        headerImage.setVisibility(View.INVISIBLE);
        headerProgress.setVisibility(View.VISIBLE);
        animImg.setImageDrawable(animationDrawable);
        animationDrawable.start();
    }

    public void setAnimImg(int resid) {
        animImg.setImageResource(resid);
    }

    public void setAnimImg(Drawable drawable) {
        animImg.setImageDrawable(drawable);
    }

    public void setRefreshingLabel(String refreshingLabel) {
        this.refreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(String releaseLabel) {
        this.releaseLabel = releaseLabel;
    }

    public void pullToRefresh() {
        headerText.setText(pullLabel);
        headerImage.clearAnimation();
        headerImage.startAnimation(resetRotateAnimation);
    }

    public void setTextColor(int color) {
        headerText.setTextColor(color);
    }

}

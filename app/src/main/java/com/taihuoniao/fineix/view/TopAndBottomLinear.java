package com.taihuoniao.fineix.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.main.MainApplication;

/**
 * Created by taihuoniao on 2016/3/15.
 */
public class TopAndBottomLinear extends LinearLayout {
    private Context context;
    private LinearLayout topLinear;
    private LinearLayout bottomLinear;
    private PointF startP;
    private ValueAnimator upAnimator;

    public TopAndBottomLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();

    }

    private void initView() {
        this.setOrientation(VERTICAL);
        topLinear = new LinearLayout(context);
        topLinear.setOrientation(VERTICAL);
        topLinear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(topLinear);
        bottomLinear = new LinearLayout(context);
        bottomLinear.setOrientation(VERTICAL);
        bottomLinear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(bottomLinear);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startP = new PointF(ev.getX(), ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                PointF nowP = new PointF(ev.getX(), ev.getY());
                if (startP.y >= topLinear.getHeight() && nowP.y < startP.y && nowP.y <= topLinear.getHeight() && this.getPaddingTop() != -MainApplication.getContext().getScreenWidth()) {
                    this.setPadding(0, (int) (nowP.y - MainApplication.getContext().getScreenWidth()), 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
                    return super.dispatchTouchEvent(ev);
                }
                nowP = new PointF(ev.getX(), ev.getY());
                if (nowP.y / MainApplication.getContext().getScreenWidth() <= 0.8 && this.getPaddingTop() < 0) {
                    upAnimator = ValueAnimator.ofFloat(getPaddingTop(), -MainApplication.getContext().getScreenWidth());
                    upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float f = (float) animation.getAnimatedValue();
                            TopAndBottomLinear.this.setPadding(0, (int) f, 0, 0);
                        }
                    });
                    upAnimator.setDuration(300);
                    upAnimator.start();
//                    this.setPadding(0, -MainApplication.getContext().getScreenWidth(), 0, 0);
                    return true;
                } else if (nowP.y / MainApplication.getContext().getScreenWidth() <= 1 && this.getPaddingTop() < 0) {
//                    this.setPadding(0, 0, 0, 0);
                    upAnimator = ValueAnimator.ofFloat(getPaddingTop(), 0);
                    upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float f = (float) animation.getAnimatedValue();
                            TopAndBottomLinear.this.setPadding(0, (int) f, 0, 0);
                        }
                    });
                    upAnimator.setDuration(300);
                    upAnimator.start();
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    public void addToToplinear(View view) {
        topLinear.addView(view);
    }

    public void addToBottomlinear(View view) {
        bottomLinear.addView(view);
    }

    //外界用来调用将toplinear展示出来的回调
    public void showTop() {
        if (this.getPaddingTop() == -MainApplication.getContext().getScreenWidth()) {
            upAnimator = ValueAnimator.ofFloat(getPaddingTop(), 0);
            upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float f = (float) animation.getAnimatedValue();
                    TopAndBottomLinear.this.setPadding(0, (int) f, 0, 0);
                }
            });
            upAnimator.setDuration(300);
            upAnimator.start();
        }
//            this.setPadding(0, 0, 0, 0);
    }
}

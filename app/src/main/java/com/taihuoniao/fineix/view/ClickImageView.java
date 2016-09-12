package com.taihuoniao.fineix.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by taihuoniao on 2016/9/12.
 */
public class ClickImageView extends ImageView {
    private OnClickListener onClickListener;
    private ValueAnimator smallAnimator, bigAnimator;

    public ClickImageView(Context context) {
        super(context);
    }

    public ClickImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClickImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void initAnim() {
        smallAnimator = ObjectAnimator.ofFloat(1f, 0.8f);
        smallAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ClickImageView.this.setScaleX((Float) animation.getAnimatedValue());
                ClickImageView.this.setScaleY((Float) animation.getAnimatedValue());
            }
        });
        bigAnimator = ObjectAnimator.ofFloat(0.8f, 1f);
        bigAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ClickImageView.this.setScaleX((Float) animation.getAnimatedValue());
                ClickImageView.this.setScaleY((Float) animation.getAnimatedValue());
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("<<<clickimageview", "x=" + event.getX() + ",y=" + event.getY());
        if (smallAnimator == null || bigAnimator == null) {
            initAnim();
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                smallAnimator.start();
                return true;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                bigAnimator.start();
                if (onClickListener != null) {
                    onClickListener.onClick(ClickImageView.this);
                }
                return true;
            case MotionEvent.ACTION_CANCEL:
                if (ClickImageView.this.getScaleX() < 1f) {
                    bigAnimator.start();
                }
                break;
        }
        return true;
    }


}

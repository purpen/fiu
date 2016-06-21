package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.taihuoniao.fineix.view.appleWatchView.AppleWatchView;

/**
 * Created by taihuoniao on 2016/6/21.
 */
public class TouchView extends LinearLayout {
    public TouchView(Context context) {
        super(context);
    }

//    GestureDetector gestureDetector;

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public TouchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

//    public void setGestureDetector(GestureDetector gestureDetector) {
//        this.gestureDetector = gestureDetector;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
//        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    private boolean isDis() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof AppleWatchView) {
                viewIndex = i;
                return true;
            }
        }
        return false;
    }

    private int viewIndex = -1;
}


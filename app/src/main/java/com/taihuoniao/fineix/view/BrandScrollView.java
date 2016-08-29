package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by taihuoniao on 2016/8/27.
 */
public class BrandScrollView extends ScrollView {
    private OnScrollListener onScrollListener;
    private boolean isStop = false;//拦截手势

    public boolean isStop() {
        return isStop;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public BrandScrollView(Context context) {
        super(context);
    }

    public BrandScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrandScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null) {
            onScrollListener.scroll(this, l, t, oldl, oldt);
        }
    }

    public interface OnScrollListener {
        void scroll(ScrollView scrollView, int l, int t, int oldl, int oldt);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isStop) {
                    Log.e("<<<Touch事件", "拦截");
                    return true;
                }
        }
        return super.dispatchTouchEvent(ev);
    }

}

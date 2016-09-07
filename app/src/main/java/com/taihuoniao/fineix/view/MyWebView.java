package com.taihuoniao.fineix.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * @author lilin
 *         created at 2016/9/7 13:35
 */
public class MyWebView extends WebView {
    public Context context;

    public interface PlayFinish {
        void After();
    }

    PlayFinish df;

    public void setDf(PlayFinish playFinish) {
        this.df = playFinish;
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setVerticalScrollBarEnabled(false);

    }

    public MyWebView(Context context) {
        super(context);
        this.context = context;
    }

    //onDraw表示显示完毕
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        df.After();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    /**
//     * 设置不能点击
//     */
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
//    }


}

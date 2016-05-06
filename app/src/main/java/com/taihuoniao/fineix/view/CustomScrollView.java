package com.taihuoniao.fineix.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by taihuoniao on 2016/2/23.
 * 商品详情页面上下分页的scrollview
 */
public class CustomScrollView extends ScrollView {
    private LinearLayout topLinear;
    private LinearLayout bottomLinear;

    private PointF startP;


    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        addView(linearLayout);
        topLinear = new LinearLayout(context);
        topLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        topLinear.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(topLinear);
        bottomLinear = new LinearLayout(context);
        bottomLinear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        bottomLinear.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(bottomLinear);
        bottomLinear.setVisibility(GONE);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startP = new PointF(ev.getX(), ev.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                PointF nowP = new PointF(ev.getX(), ev.getY());
                if (getScrollY() + getHeight() >= topLinear.getMeasuredHeight() && bottomLinear.getVisibility() == GONE) {
                    bottomLinear.setVisibility(VISIBLE);
                    setVerticalScrollBarEnabled(false);
                    return true;
                } else if (getScrollY() + getHeight() < topLinear.getMeasuredHeight() && bottomLinear.getVisibility() == VISIBLE) {
                    bottomLinear.setVisibility(GONE);
                    setVerticalScrollBarEnabled(true);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                nowP = new PointF(ev.getX(), ev.getY());
                if (nowP.y < startP.y && getScrollY() + getHeight() > topLinear.getMeasuredHeight() + getHeight() / 4 && getScrollY() < topLinear.getMeasuredHeight()) {
                    smoothScrollTo(0, topLinear.getMeasuredHeight());
                    return true;
                } else if (nowP.y < startP.y && getScrollY() + getHeight() < topLinear.getMeasuredHeight() + getHeight() / 4 && getScrollY() + getHeight() > topLinear.getMeasuredHeight()) {
                    smoothScrollTo(0, topLinear.getMeasuredHeight() - getHeight());
                    return true;
                } else if (nowP.y > startP.y && getScrollY() + getHeight() / 4 < topLinear.getMeasuredHeight() && getScrollY() + getHeight() > topLinear.getMeasuredHeight()) {
                    smoothScrollTo(0, topLinear.getMeasuredHeight() - getHeight());
                    return true;
                } else if (nowP.y > startP.y && getScrollY() + getHeight() / 4 > topLinear.getMeasuredHeight() && getScrollY() < topLinear.getMeasuredHeight()) {
                    smoothScrollTo(0, topLinear.getMeasuredHeight());
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    //往toplinear中添加界面
    public void addToToplinear(View view) {
        topLinear.addView(view);
    }

    //  往bottomlinear中添加界面
    public void addToBottomlinear(View view) {
        bottomLinear.addView(view);
    }

    //外部点击跳转到bottomview的回调
    public void goToBottom() {
        if (bottomLinear.getVisibility() == GONE)
            bottomLinear.setVisibility(VISIBLE);
        smoothScrollTo(0, topLinear.getMeasuredHeight());
    }

}

package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;

import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

/**
 * Created by taihuoniao on 2016/4/15.
 */
public class PullToRefreshListViewForScrollView extends PullToRefreshListView {
    public PullToRefreshListViewForScrollView(Context context) {
        super(context);
    }

    public PullToRefreshListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshListViewForScrollView(Context context, int mode) {
        super(context, mode);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

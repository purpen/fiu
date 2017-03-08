package com.taihuoniao.fineix.view.pulltorefresh;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.handmark.pulltorefresh.library.*;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by Stephen on 2017/3/7 11:18
 * Email: 895745843@qq.com
 */

public class PullToRefreshGridView2 extends PullToRefreshAdapterViewBase<HeaderGridView> {

    public PullToRefreshGridView2(Context context) {
        super(context);
    }

    public PullToRefreshGridView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshGridView2(Context context, PullToRefreshBase.Mode mode) {
        super(context, mode);
    }

    public PullToRefreshGridView2(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.AnimationStyle style) {
        super(context, mode, style);
    }

    @Override
    public final PullToRefreshBase.Orientation getPullToRefreshScrollDirection() {
        return PullToRefreshBase.Orientation.VERTICAL;
    }

    @Override
    protected final HeaderGridView createRefreshableView(Context context, AttributeSet attrs) {
        final HeaderGridView gv;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            gv = new PullToRefreshGridView2.InternalGridViewSDK9(context, attrs);
        } else {
            gv = new PullToRefreshGridView2.InternalGridView(context, attrs);
        }

        // Use Generated ID (from res/values/ids.xml)
        gv.setId(R.id.gridview);
        return gv;
    }

    class InternalGridView extends HeaderGridView implements EmptyViewMethodAccessor {

        public InternalGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshGridView2.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }
    }

    @TargetApi(9)
    final class InternalGridViewSDK9 extends PullToRefreshGridView2.InternalGridView {

        public InternalGridViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshGridView2.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

            return returnValue;
        }
    }
}

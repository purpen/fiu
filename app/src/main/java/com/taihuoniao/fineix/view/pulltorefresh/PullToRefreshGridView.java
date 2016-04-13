package com.taihuoniao.fineix.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;
import android.widget.GridView;

/**
 * Created by taihuoniao on 2016/1/27.
 */
public class PullToRefreshGridView extends PullToRefreshAdapterViewBase<GridView> {
    class InternalGridView extends GridView implements EmptyViewMethodAccessor {

        public InternalGridView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshGridView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        public ContextMenu.ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }
    }
    public PullToRefreshGridView(Context context) {
        super(context);
        this.setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshGridView(Context context, int mode) {
        super(context, mode);
        this.setDisableScrollingWhileRefreshing(false);
    }

    @Override
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return ((InternalGridView) getRefreshableView()).getContextMenuInfo();
    }

    @Override
    protected GridView createRefreshableView(Context context, AttributeSet attrs) {
        GridView gv = new InternalGridView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        gv.setId(android.R.id.list);
        return gv;
    }
}



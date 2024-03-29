package com.taihuoniao.fineix.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;

public class PullToRefreshListView extends
        PullToRefreshAdapterViewBase<ListView> {

    class InternalListView extends ListView implements EmptyViewMethodAccessor {

        public InternalListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void setEmptyView(View emptyView) {
            PullToRefreshListView.this.setEmptyView(emptyView);
        }

        @Override
        public void setEmptyViewInternal(View emptyView) {
            super.setEmptyView(emptyView);
        }

        public ContextMenuInfo getContextMenuInfo() {
            return super.getContextMenuInfo();
        }


    }

    public PullToRefreshListView(Context context) {
        super(context);
        this.setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshListView(Context context, int mode) {
        super(context, mode);
        this.setDisableScrollingWhileRefreshing(false);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDisableScrollingWhileRefreshing(false);
    }

    @Override
    public ContextMenuInfo getContextMenuInfo() {
        return ((InternalListView) getRefreshableView()).getContextMenuInfo();
    }

    @Override
    protected final InternalListView createRefreshableView(Context context,
                                                   AttributeSet attrs) {
        InternalListView lv = new InternalListView(context, attrs);

        // Set it to this so it can be used in ListActivity/ListFragment
        lv.setId(android.R.id.list);
        return lv;
    }


}

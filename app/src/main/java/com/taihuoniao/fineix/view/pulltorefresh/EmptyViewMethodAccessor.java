package com.taihuoniao.fineix.view.pulltorefresh;

import android.view.View;

/**
 * Interface that allows PullToRefreshBase to hijack the call to
 * AdapterView.setEmptyView()
 * 
 * @author chris
 */
public interface EmptyViewMethodAccessor {

	/**
	 * Calls upto AdapterView.setEmptyView()
	 *
	 *            to set as Empty View
	 */
	void setEmptyViewInternal(View emptyView);

	/**
	 * Should call PullToRefreshBase.setEmptyView() which will then
	 * automatically call through to setEmptyViewInternal()
	 *
	 *            to set as Empty View
	 */
	void setEmptyView(View emptyView);

}

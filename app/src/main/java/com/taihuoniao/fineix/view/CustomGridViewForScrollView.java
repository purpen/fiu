package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 *  The temporary solution to find/good interface The rounded images do not click on the bug
 *
 *  note: don't use this class, be careful with pit
 */
public class CustomGridViewForScrollView extends GridViewForScrollView {

	public CustomGridViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomGridViewForScrollView(Context context) {
		super(context);
	}

	@Override
	public void requestLayout() {
		isInvaidata();
	}

	private long count  = 0;

	private boolean isInvaidata(){
		if (count < 10 || count % 10 == 0) {
			super.requestLayout();
		}
		count ++ ;
		return false;
	}
}

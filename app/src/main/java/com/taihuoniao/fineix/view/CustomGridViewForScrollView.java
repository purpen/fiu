package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 *  临时解决 发现/好货界面 圆角图片不可点击的bug
 *
 *  注意：不要随便用这个类，小心有坑
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

package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author lilin
 * created at 2016/4/11 15:57
 */
public class CustomListView extends ListView {
    public CustomListView(Context context){
        this(context,null);
    }
    public CustomListView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

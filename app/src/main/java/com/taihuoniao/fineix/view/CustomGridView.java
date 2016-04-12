package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author lilin
 * created at 2016/4/11 15:57
 */
public class CustomGridView extends GridView {
    public CustomGridView(Context context){
        this(context,null);
    }
    public CustomGridView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CustomGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}

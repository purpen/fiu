package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ListView;

/**
 * Created by taihuoniao on 2016/1/29.
 */
public class ListViewForScrollView extends ListView {
    private static int vHeight = 0;
//    private boolean isLabelView = false;//判断是否是label界面

    public ListViewForScrollView(Context context) {
        super(context);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewForScrollView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
//        Log.e("<<<","isLabelView = "+isLabelView);
//        if (isLabelView) {
            setVHeight(getMeasuredHeight());
//        }
    }

    //    public void setIsLabelView(boolean isLabelView) {
//        this.isLabelView = isLabelView;
//    }

    private void setVHeight(int height) {
        Log.e("<<<","measureheight = "+height);
        if (height > vHeight) {
            Log.e("<<<", "listview.height = " + height);
            vHeight = height;
        }
    }

    public static int getVHeight() {
        return vHeight;
    }
}

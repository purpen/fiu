package com.taihuoniao.fineix.zone;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lilin on 2017/2/23.
 */

public class ZoneCoverMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public ZoneCoverMarginDecoration(Context context, int marginDimen) {
        margin = context.getResources().getDimensionPixelSize(marginDimen);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position % 4 == 0) {
            outRect.set(0,margin,margin,margin);
        }else if(position%4==3){
            outRect.set(margin,margin,0,margin);
        }else {
            outRect.set(margin,margin,margin,margin);
        }
    }
}

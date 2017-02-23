package com.taihuoniao.fineix.zone;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lilin on 2017/2/23.
 */

public class MarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public MarginDecoration(Context context, int marginDimen) {
        margin = context.getResources().getDimensionPixelSize(marginDimen);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position % 2 == 0) {
            outRect.set(margin,0, margin / 2, margin);
        } else {
            outRect.set(margin / 2,0, margin, margin);
        }

        if (position == 0 || position == 1) {
            outRect.set(margin, margin, margin / 2, margin);
        }

        if (position==1){
            outRect.set(margin/2, margin, margin, margin);
        }
    }
}

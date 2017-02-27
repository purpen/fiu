package com.taihuoniao.fineix.main;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lilin on 2017/2/23.
 */

public class ShopMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public ShopMarginDecoration(Context context, int marginDimen) {
        margin = context.getResources().getDimensionPixelSize(marginDimen);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        outRect.left = margin;
        outRect.right= margin;
        if (position == 0) {
            outRect.left =0 ;
        }
    }
}

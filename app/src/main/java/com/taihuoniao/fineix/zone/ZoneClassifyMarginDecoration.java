package com.taihuoniao.fineix.zone;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by lilin on 2017/2/23.
 */

public class ZoneClassifyMarginDecoration extends RecyclerView.ItemDecoration {
    private int margin;

    public ZoneClassifyMarginDecoration(Context context, int marginDimen) {
        margin = context.getResources().getDimensionPixelSize(marginDimen);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        if (position%3==0){
            outRect.left = margin;
            outRect.right = margin;
            outRect.top = (int)(margin*0.5);
            outRect.bottom = (int)(margin*0.5);
        }else if(position%(position+2)==0){
            outRect.right=0;
            outRect.left= margin;
            outRect.top = (int)(margin*0.5);
            outRect.bottom = (int)(margin*0.5);
        }else {
            outRect.left = margin;
            outRect.right = margin;
            outRect.top = (int)(margin*0.5);
            outRect.bottom = (int)(margin*0.5);
        }

    }
}

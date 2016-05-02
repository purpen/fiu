package com.taihuoniao.fineix.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class SlidingFocusImageView extends Gallery
{
    public  SlidingFocusImageView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        t.clear();
        View selectedChild = getSelectedView();
        if (child == selectedChild) {
            Matrix matrix = t.getMatrix();
            int w2 = child.getWidth() / 2;
            int h2 = child.getHeight() / 2;
            matrix.postScale(1f, 1f, w2, h2);
        }
        return true;
    }
}

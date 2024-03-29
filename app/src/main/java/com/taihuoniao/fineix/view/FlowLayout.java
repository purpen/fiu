package com.taihuoniao.fineix.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;

/**
 * FlowLayout is much more like a {@link android.widget.LinearLayout}, but it can automatically
 * separate the widgets wrapped in it into multiple lines just like the water flow.
 * <p>
 * Inspired by {@see http://hzqtc.github.io/2013/12/android-custom-layout-flowlayout.html}
 *
 * @author liangfeizc {@see http://www.liangfeizc.com}
 */
public class FlowLayout extends ViewGroup {
    /**
     * private void addChildTo(FlowLayout flowLayout) {
     * for (int i = 'A'; i < 'Z'; i++) {
     * Button btn = new CheckableButton(this);
     * btn.setHeight(dp2px(32));
     * btn.setTextSize(16);
     * btn.setTextColor(getResources().getColorStateList(R.color.checkable_text_color));
     * btn.setBackgroundResource(R.drawable.checkable_background);
     * StringBuilder sb = new StringBuilder();
     * for (int j = 0; j < i - 'A' + 4; j++) {
     * sb.append((char) i);
     * }
     * btn.setText(sb.toString());
     * flowLayout.addView(btn);
     * }
     * }
     */
    private int line = -1;//默认行数,当超过line行就不再添加子view
    private static final int DEFAULT_HORIZONTAL_SPACING = 5;
    private static final int DEFAULT_VERTICAL_SPACING = 5;

    private int mVerticalSpacing;
    private int mHorizontalSpacing;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        try {
            mHorizontalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_horizontalSpacing, DEFAULT_HORIZONTAL_SPACING);
            mVerticalSpacing = a.getDimensionPixelSize(
                    R.styleable.FlowLayout_verticalSpacing, DEFAULT_VERTICAL_SPACING);
        } finally {
            a.recycle();
        }
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setHorizontalSpacing(int pixelSize) {
        mHorizontalSpacing = pixelSize;
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setVerticalSpacing(int pixelSize) {
        mVerticalSpacing = pixelSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int myWidth = resolveSize(0, widthMeasureSpec);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;
        int curentLine = 1;//当前行数
        // Measure each child and put the child to the right of previous child
        // if there's enough room for it, otherwise, wrap the line and put the child to next line.
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            } else {
                continue;
            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                curentLine++;
                if (line > -1 && curentLine > line) {
                    break;
                }
                childLeft = paddingLeft + childWidth;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            } else {
                childLeft += childWidth + mHorizontalSpacing;
            }
        }

        int wantedHeight = childTop + lineHeight + paddingBottom;

        setMeasuredDimension(myWidth, resolveSize(wantedHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int myWidth = r - l;

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();

        int childLeft = paddingLeft;
        int childTop = paddingTop;

        int lineHeight = 0;
        int currentLine = 1;
        for (int i = 0, childCount = getChildCount(); i < childCount; ++i) {
            View childView = getChildAt(i);

            if (childView.getVisibility() == View.GONE) {
                continue;
            }

            int childWidth = childView.getMeasuredWidth();
            int childHeight = childView.getMeasuredHeight();

            lineHeight = Math.max(childHeight, lineHeight);

            if (childLeft + childWidth + paddingRight > myWidth) {
                currentLine++;
                if (line > -1 && currentLine > line) {
                    break;
                }
                childLeft = paddingLeft;
                childTop += mVerticalSpacing + lineHeight;
                lineHeight = childHeight;
            }

            childView.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
            childLeft += childWidth + mHorizontalSpacing;
        }
    }
}

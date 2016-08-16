package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.TagItem;


/**
 * @author tongqian.ni
 */
public class LabelView extends LinearLayout {
    //贴纸
//    private MyImageViewTouch mImageView;
    //容器
    private RelativeLayout gpuRelative;

    private TagItem tagInfo = new TagItem();
    private float parentWidth = 0;
    private float parentHeight = 0;
    private boolean isLeft = true;
    public TextView nameTv;
    public RelativeLayout pointRelative;

    public TagItem getTagInfo() {
        return tagInfo;
    }

    public LabelView(Context context) {
        super(context);
        init(context);
    }

    public LabelView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }
    //判断是左侧还是在右侧
    public boolean isLeft() {
        return isLeft;
    }

    //切换左右
    public void setLeftOrRight() {
        if (isLeft) {
            nameTv.setBackgroundResource(R.drawable.label_right);
            isLeft = false;
            nameTv.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pointRelative.getLayoutParams();
                    layoutParams.leftMargin = (int) (nameTv.getMeasuredWidth() - pointWidth - labelMargin);
                    pointRelative.setLayoutParams(layoutParams);
                    tagInfo.setX(than(left + getMeasuredWidth() - labelMargin - pointWidth / 2, parentWidth));
                    tagInfo.setY(than(top + getMeasuredHeight() - pointWidth / 2, parentHeight));
                    Log.e("<<<", "x=" + tagInfo.getX() + ",y=" + tagInfo.getY());
                }
            });
        } else {
            nameTv.setBackgroundResource(R.drawable.label_left);
            isLeft = true;
            nameTv.post(new Runnable() {
                @Override
                public void run() {
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) pointRelative.getLayoutParams();
                    layoutParams.leftMargin = (int) labelMargin;
                    pointRelative.setLayoutParams(layoutParams);
                    tagInfo.setX(than(left + labelMargin + pointWidth / 2, parentWidth));
                    tagInfo.setY(than(top + getMeasuredHeight() - pointWidth / 2, parentHeight));
                    Log.e("<<<", "x=" + tagInfo.getX() + ",y=" + tagInfo.getY());
                }
            });
        }
    }

    private float pointWidth;
    private float labelMargin;

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
        nameTv = (TextView) findViewById(R.id.name);
        pointRelative = (RelativeLayout) findViewById(R.id.point_relative);
        pointWidth = getResources().getDimension(R.dimen.label_point_width);
        labelMargin = getResources().getDimension(R.dimen.label_point_margin);
    }

    public void init(TagItem tagItem) {
        tagInfo = tagItem;
        nameTv.setText(tagItem.getName());
    }


    /**
     * 将标签放置于对应RelativeLayout的对应位置，考虑引入postion作为参数？？
     *
     * @param parent
     * @param left
     * @param top
     */
    public void addTo(OnClickListener deleteListener, final MyImageViewTouch overlay, RelativeLayout parent, final int left, final int top) {
        if (deleteListener != null) {
//            deleteImg.setOnClickListener(deleteListener);
        }
//        mImageView = overlay;
        gpuRelative = parent;
        this.parentWidth = overlay.getWidth();
        this.parentHeight = overlay.getHeight();
        setupLocation(left, top);
        parent.addView(LabelView.this);
    }

    private void setupLocation(int leftLoc, int topLoc) {
        //在移动的时候，left的位置是相对于overlay的位置，而不是整个relative，。。。。。
        this.left = leftLoc;
        this.top = topLoc;
        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (left + getWidth() > parentWidth) {
            //右边缘
            left = (int) (parentWidth - getWidth());
        }
        if (top + getHeight() > parentHeight) {
            //下边缘
            top = (int) (parentHeight - getHeight());
        }
        if (top < 0) {
            top = 0;
        }
        if (left < 0) {
            //左边缘
            left = 0;
        }
        labelParams.setMargins((int) (left + (gpuRelative.getWidth() - parentWidth) / 2), top, 0, 0);
        setLayoutParams(labelParams);
        if (isLeft) {
            tagInfo.setX(than(left + labelMargin + pointWidth / 2, parentWidth));
        } else {
            tagInfo.setX(than(left + getMeasuredWidth() - labelMargin - pointWidth / 2, parentHeight));
        }
        tagInfo.setY(than(top + getMeasuredHeight() - pointWidth/2, parentHeight));
        Log.e("<<<", "x=" + tagInfo.getX() + ",y=" + tagInfo.getY());
    }

    private double than(double num1, double num2) {
        return num2 == 0 ? 0 : (num1 / num2);
    }

    private int left = -1, top = -1;
    private int imageWidth = 0;
    private int imageHeight = 0;


    public void wave() {
        pointRelative.clearAnimation();
        final ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);
        final ScaleAnimation scaleAnimation1 = new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation1.setDuration(2000);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pointRelative.clearAnimation();
                pointRelative.startAnimation(scaleAnimation1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scaleAnimation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                pointRelative.clearAnimation();
                pointRelative.startAnimation(scaleAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        pointRelative.startAnimation(scaleAnimation);
    }

    public void stopAnim() {
        pointRelative.clearAnimation();
    }

    public void updateLocation(int x, int y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        setupLocation(x, y);
//        wave();
    }

}

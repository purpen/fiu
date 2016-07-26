package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.utils.DensityUtils;


/**
 * @author tongqian.ni
 */
public class LabelView extends LinearLayout {
    //贴纸
    private MyImageViewTouch mImageView;
    //容器
    private RelativeLayout gpuRelative;

    private TagItem tagInfo = new TagItem();
    private float parentWidth = 0;
    private float parentHeight = 0;
    //    private ImageView labelIcon;
//    private RelativeLayout relativeLeft;
//    private TextView nameRight;
//    private TextView priceLeft;
//    private ImageView pointLeft;
    //    private TextView labelTxtLeft;
//    private TextView priceTxtLeft;
    public RelativeLayout relativeRight;
    public TextView nameLeft;
    public TextView priceRight;
    private ImageView pointRight;
    private ImageView animPoint;
    private ImageView deleteImg;
    private TextView yellowPoint;
//    private TextView labelTxtRight;
//    private TextView priceTxtRight;

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

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
        relativeRight = (RelativeLayout) findViewById(R.id.view_label_right);
        nameLeft = (TextView) findViewById(R.id.view_label_left_name);
        priceRight = (TextView) findViewById(R.id.view_label_right_price);
        pointRight = (ImageView) findViewById(R.id.view_label_right_point);
        animPoint = (ImageView) findViewById(R.id.view_label_anim_point);
        yellowPoint = (TextView) findViewById(R.id.view_label_yellow_point);
        deleteImg = (ImageView) findViewById(R.id.view_label_delete);
    }

    public void init(TagItem tagItem) {
        tagInfo = tagItem;
        nameLeft.setText(tagItem.getName());
        priceRight.setText(tagItem.getPrice());
    }


    /**
     * 将标签放置于对应RelativeLayout的对应位置，考虑引入postion作为参数？？
     *
     * @param parent
     * @param left
     * @param top
     */
//    public void draw(ViewGroup parent, final int left, final int top, boolean isLeft) {
//        this.parentWidth = parent.getWidth();
//        if (parentWidth <= 0) {
//            parentWidth = MainApplication.getContext().getScreenWidth();
//        }
//        setImageWidth((int) parentWidth);
//        this.parentHeight = parentWidth;
//        if (isLeft) {
//            relativeRight.setVisibility(View.VISIBLE);
//            relativeLeft.setVisibility(View.GONE);
//            setupLocation(left, top);
//            parent.addView(this);
//        } else {
//            relativeRight.setVisibility(View.GONE);
//            relativeLeft.setVisibility(View.VISIBLE);
//            setupLocation(left, top);
//            parent.addView(this);
//        }
//
//    }

    /**
     * 将标签放置于对应RelativeLayout的对应位置，考虑引入postion作为参数？？
     *
     * @param parent
     * @param left
     * @param top
     */
    public void addTo(OnClickListener deleteListener, final MyImageViewTouch overlay, RelativeLayout parent, final int left, final int top) {
        if (deleteListener != null) {
            deleteImg.setOnClickListener(deleteListener);
        }
//        if (left > parent.getWidth() / 2) {
//            tagInfo.setLeft(false);
//        }
        mImageView = overlay;
        gpuRelative = parent;
//        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
        this.parentWidth = overlay.getWidth();
//        setImageWidth((int) parentWidth);
        this.parentHeight = overlay.getHeight();
        double bi = (double) overlay.getWidth() / (double) parent.getWidth();
//        Log.e("<<<添加labelview", "bi=" + bi);
        priceRight.getLayoutParams().width = (int) (bi * DensityUtils.dp2px(getContext(), 63));
        priceRight.getLayoutParams().height = (int) (bi * DensityUtils.dp2px(getContext(), 24));
        nameLeft.getLayoutParams().width = (int) (bi * DensityUtils.dp2px(getContext(), 114));
        nameLeft.getLayoutParams().height = (int) (bi * DensityUtils.dp2px(getContext(), 24));
        RelativeLayout.LayoutParams pointRightLp = (RelativeLayout.LayoutParams) pointRight.getLayoutParams();
        pointRightLp.width = (int) (bi * DensityUtils.dp2px(getContext(), 4));
        pointRightLp.height = (int) (bi * DensityUtils.dp2px(getContext(), 4));
//        pointRightLp.rightMargin = (int) (bi * DensityUtils.dp2px(getContext(), 7));
        pointRight.setLayoutParams(pointRightLp);
        RelativeLayout.LayoutParams animPointLp = (RelativeLayout.LayoutParams) animPoint.getLayoutParams();
        animPointLp.width = (int) (bi * DensityUtils.dp2px(getContext(), 4));
        animPointLp.height = (int) (bi * DensityUtils.dp2px(getContext(), 4));
//        animPointLp.rightMargin = (int) (bi * DensityUtils.dp2px(getContext(), 7));
        animPoint.setLayoutParams(animPointLp);
        RelativeLayout.LayoutParams yellowPointLp = (RelativeLayout.LayoutParams) yellowPoint.getLayoutParams();
        yellowPointLp.width = (int) (bi * DensityUtils.dp2px(getContext(), 7));
        yellowPointLp.height = (int) (bi * DensityUtils.dp2px(getContext(), 7));
//        yellowPointLp.rightMargin = (int) (bi * DensityUtils.dp2px(getContext(), 7));
        yellowPoint.setLayoutParams(yellowPointLp);
        relativeRight.setVisibility(View.VISIBLE);
//            relativeLeft.setVisibility(View.GONE);
        setupLocation(left, top);
        parent.addView(LabelView.this);

    }

    private void setupLocation(int leftLoc, int topLoc) {
        //在移动的时候，left的位置是相对于overlay的位置，而不是整个relative，。。。。。
        this.left = leftLoc;
        this.top = topLoc;

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (left + getWidth() > mImageView.getWidth()) {
            //右边缘
            left = mImageView.getWidth() - getWidth();
        }
        if (top + getHeight() > mImageView.getHeight()) {
            //下边缘
            top = mImageView.getHeight() - getHeight();
        }
        if (top < 0) {
            top = 0;
        }
        if (left < 0) {
            //左边缘
            left = 0;
        }
        //left=179,top=461,画布宽=451,画布高=802
        // x=0.3968957871396896,y=0.5748129675810474
        labelParams.setMargins(left + (gpuRelative.getWidth() - mImageView.getWidth()) / 2, top, 0, 0);
//        if (tagInfo.isLeft()) {
//        Log.e("<<<", "left=" + left + ",top=" + top + ",画布宽=" + mImageView.getWidth()
//                + ",画布高=" + mImageView.getHeight());
        //0.2641975308641975,0.5
        tagInfo.setX(than(left, mImageView.getWidth()));
        tagInfo.setY(than(top, mImageView.getHeight()));
//        } else {
//            tagInfo.setX(than(left + nameLeft.getWidth() - DensityUtils.dp2px(getContext(), 15) - DensityUtils.dp2px(getContext(), 2), params.width));
//            tagInfo.setY(than(top + DensityUtils.dp2px(getContext(), 4), params.height));
//        }
//        Log.e("<<<", "x=" + tagInfo.getX() + ",y=" + tagInfo.getY());
        setLayoutParams(labelParams);
    }

    private double than(double num1, double num2) {
        return num2 == 0 ? 0 : (num1 / num2);
    }

//    private void setImageWidth(int width) {
//        this.imageWidth = width;
//    }

//    private int getImageWidth() {
//        return imageWidth <= 0 ? mImageView.getWidth() : imageWidth;
//    }
//
//    private void setImageHeight(int height) {
//        this.imageHeight = height;
//    }
//
//    private int getImageHeight() {
//        return imageHeight <= 0 ? (int) this.parentHeight : imageHeight;
//    }

    private int left = -1, top = -1;
    private int imageWidth = 0;
    private int imageHeight = 0;

    private static final int ANIMATIONEACHOFFSET = 1000;

//    private boolean emptyItem = false;
//
//    public void setEmpty() {
//        emptyItem = true;
//        relativeLeft.setVisibility(View.GONE);
//        relativeRight.setVisibility(View.GONE);
//    }

    //显示全部内容还是只显示点
    public void pointOrAll(boolean isLeft, boolean showAll) {
//        if (isLeft) {
//            relativeRight.setVisibility(GONE);
//            nameLeft.setVisibility(GONE);
//            pointRight.setVisibility(GONE);
//            priceRight.setVisibility(GONE);
//            if (showAll) {
//                relativeLeft.setVisibility(VISIBLE);
//                priceLeft.setVisibility(VISIBLE);
//                pointLeft.setVisibility(VISIBLE);
//                nameRight.setVisibility(VISIBLE);
//            } else {
//                relativeLeft.setVisibility(VISIBLE);
//                priceLeft.setVisibility(INVISIBLE);
//                nameRight.setVisibility(INVISIBLE);
//                pointLeft.setVisibility(VISIBLE);
//            }
//        } else {

//            relativeLeft.setVisibility(GONE);
//            nameRight.setVisibility(GONE);
//            pointLeft.setVisibility(GONE);
//            priceLeft.setVisibility(GONE);
        deleteImg.setVisibility(GONE);
        if (showAll) {
            relativeRight.setVisibility(VISIBLE);
            priceRight.setVisibility(VISIBLE);
            pointRight.setVisibility(VISIBLE);
            nameLeft.setVisibility(VISIBLE);
        } else {
            relativeRight.setVisibility(VISIBLE);
            priceRight.setVisibility(INVISIBLE);
            nameLeft.setVisibility(INVISIBLE);
            pointRight.setVisibility(VISIBLE);
        }
//        }
    }

    public void setA(float alpha) {
        relativeRight.setAlpha(alpha);
        priceRight.setAlpha(alpha);
        nameLeft.setAlpha(alpha);
    }

    public void wave() {
        final AnimationSet as = new AnimationSet(true);
        as.setRepeatCount(Animation.INFINITE);
        ScaleAnimation sa = new ScaleAnimation(1f, 4f, 1f, 4f, ScaleAnimation.RELATIVE_TO_SELF,
                0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATIONEACHOFFSET * 3);
        AlphaAnimation aniAlp = new AlphaAnimation(1, 0f);
        as.setDuration(ANIMATIONEACHOFFSET * 3);
        as.addAnimation(sa);
        sa.setRepeatCount(Animation.INFINITE);
        as.addAnimation(aniAlp);
        aniAlp.setRepeatCount(Animation.INFINITE);
        final AnimationSet as1 = new AnimationSet(true);
        as1.setRepeatCount(Animation.INFINITE);
        ScaleAnimation sa1 = new ScaleAnimation(1f, 6f, 1f, 6f, ScaleAnimation.RELATIVE_TO_SELF,
                0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa1.setDuration(ANIMATIONEACHOFFSET * 3);
        AlphaAnimation aniAlp1 = new AlphaAnimation(1, 0f);
        as1.setDuration(ANIMATIONEACHOFFSET * 3);
        as1.addAnimation(sa1);
        sa1.setRepeatCount(Animation.INFINITE);
        as1.addAnimation(aniAlp1);
        aniAlp1.setRepeatCount(Animation.INFINITE);
        animPoint.setVisibility(VISIBLE);
        pointRight.startAnimation(as);
        animPoint.startAnimation(as1);
//        pointRight.setScaleX(3f);
//        pointRight.setScaleY(3f);
//        animPoint.setScaleX(6f);
//        animPoint.setScaleY(6f);
    }

    public void stopAnim() {
        pointRight.clearAnimation();
        animPoint.clearAnimation();
    }

    public void updateLocation(int x, int y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        setupLocation(x, y);
        wave();
    }

}

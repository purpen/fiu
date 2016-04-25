package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
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
    private ViewGroup gpuRelative;

    private TagItem tagInfo = new TagItem();
    private float parentWidth = 0;
    private float parentHeight = 0;
    //    private ImageView labelIcon;
    private RelativeLayout relativeLeft;
    private TextView nameRight;
    private TextView priceLeft;
    private ImageView pointLeft;
    //    private TextView labelTxtLeft;
//    private TextView priceTxtLeft;
    private RelativeLayout relativeRight;
    public TextView nameLeft;
    private TextView priceRight;
    private ImageView pointRight;
//    private TextView labelTxtRight;
//    private TextView priceTxtRight;

    public TagItem getTagInfo() {
        return tagInfo;
    }

    public LabelView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
//        labelIcon = (ImageView) findViewById(R.id.view_label_point);
        relativeLeft = (RelativeLayout) findViewById(R.id.view_label_left);
        priceLeft = (TextView) findViewById(R.id.view_label_left_price);
        nameRight = (TextView) findViewById(R.id.view_label_right_name);
        pointLeft = (ImageView) findViewById(R.id.view_label_left_point);
//        labelTxtLeft = (TextView) findViewById(R.id.view_label_left_name);
//        priceTxtLeft = (TextView) findViewById(R.id.view_label_left_price);
        relativeRight = (RelativeLayout) findViewById(R.id.view_label_right);
        nameLeft = (TextView) findViewById(R.id.view_label_left_name);
        priceRight = (TextView) findViewById(R.id.view_label_right_price);
        pointRight = (ImageView) findViewById(R.id.view_label_right_point);
//        labelTxtRight = (TextView) findViewById(R.id.view_label_right_name);
//        priceTxtRight = (TextView) findViewById(R.id.view_label_right_price);
    }

    public LabelView(Context context, AttributeSet attr) {
        super(context, attr);
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
//        labelIcon = (ImageView) findViewById(R.id.view_label_point);
        relativeLeft = (RelativeLayout) findViewById(R.id.view_label_left);
        priceLeft = (TextView) findViewById(R.id.view_label_left_price);
        nameRight = (TextView) findViewById(R.id.view_label_right_name);
        pointLeft = (ImageView) findViewById(R.id.view_label_left_point);
//        labelTxtLeft = (TextView) findViewById(R.id.view_label_left_name);
//        priceTxtLeft = (TextView) findViewById(R.id.view_label_left_price);
        relativeRight = (RelativeLayout) findViewById(R.id.view_label_right);
        nameLeft = (TextView) findViewById(R.id.view_label_left_name);
        priceRight = (TextView) findViewById(R.id.view_label_right_price);
        pointRight = (ImageView) findViewById(R.id.view_label_right_point);
//        labelTxtRight = (TextView) findViewById(R.id.view_label_right_name);
//        priceTxtRight = (TextView) findViewById(R.id.view_label_right_price);
    }

    public void init(TagItem tagItem) {
//        tagInfo.setName(tagItem.getName());
//        tagInfo.setId(tagItem.getId());
//        tagInfo.setType(tagItem.getType());
        tagInfo = tagItem;
        nameLeft.setText(tagItem.getName());
        nameRight.setText(tagItem.getName());
        priceLeft.setText(tagItem.getPrice());
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
    public void addTo(final MyImageViewTouch overlay, ViewGroup parent, final int left, final int top) {
        if (left > parent.getWidth() / 2) {
            tagInfo.setLeft(false);
        }
        mImageView = overlay;
        gpuRelative = parent;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) overlay.getLayoutParams();
        this.parentWidth = lp.width;
        setImageWidth((int) parentWidth);
        this.parentHeight = lp.height;
        setImageHeight((int) this.parentHeight);
        if (tagInfo.isLeft()) {
            relativeRight.setVisibility(View.GONE);
            relativeLeft.setVisibility(View.VISIBLE);
            //点到左边缘的位置,不是整个控件
            int toLeft = left - DensityUtils.dp2px(getContext(), 62) - DensityUtils.dp2px(getContext(), 2) - (gpuRelative.getWidth() - overlay.getWidth()) / 2;
            Log.e("<<<", "toLeft=" + toLeft + ",top=" + top);
            setupLocation(toLeft, top);
            parent.addView(this);
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    RelativeLayout.LayoutParams pointLeftLp = (RelativeLayout.LayoutParams) pointLeft.getLayoutParams();
//                    //点到左边缘的位置,不是整个控件
//                    int toLeft = left - priceLeft.getMeasuredWidth() - pointLeftLp.leftMargin;
//                    setupLocation(toLeft, top);
//                    relativeLeft.setVisibility(View.VISIBLE);
//                }
//            });
        } else {
            relativeRight.setVisibility(View.INVISIBLE);
            relativeLeft.setVisibility(View.GONE);
            setupLocation(20, 20);
            parent.addView(this);
            post(new Runnable() {
                @Override
                public void run() {
                    //因为商品名称的控件宽度不定。所以需要等布局完成才能加载
                    //点到左边缘的位置,不是整个控件
                    int toLeft = left - nameLeft.getWidth() + DensityUtils.dp2px(getContext(), 15) + DensityUtils.dp2px(getContext(), 2) - (gpuRelative.getWidth() - overlay.getWidth()) / 2;
                    setupLocation(toLeft, top);
                    relativeRight.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    private void setupLocation(int leftLoc, int topLoc) {
        //在移动的时候，left的位置是相对于overlay的位置，而不是整个relative，。。。。。
        this.left = leftLoc;
        this.top = topLoc;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mImageView.getLayoutParams();
        RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (left + getWidth() > params.width) {
            //右边缘
            left = params.width - getWidth();
        }
        if (top + getHeight() > params.height) {
            //下边缘
            top = params.height - getHeight();
        }
        if (top < 0) {
            top = 0;
        }
        if (left < 0) {
            //左边缘
            left = 0;
        }
        labelParams.setMargins(left + (gpuRelative.getWidth() - params.width) / 2, top, 0, 0);
//        if (tagInfo.isLeft()) {
        tagInfo.setX(than(left, params.width));
        tagInfo.setY(than(top, params.height));
//        } else {
//            tagInfo.setX(than(left + nameLeft.getWidth() - DensityUtils.dp2px(getContext(), 15) - DensityUtils.dp2px(getContext(), 2), params.width));
//            tagInfo.setY(than(top + DensityUtils.dp2px(getContext(), 4), params.height));
//        }
        setLayoutParams(labelParams);
    }

    private double than(double num1, double num2) {
        return num2 == 0 ? 0 : (num1 / num2);
    }

    private void setImageWidth(int width) {
        this.imageWidth = width;
    }

    private int getImageWidth() {
        return imageWidth <= 0 ? mImageView.getWidth() : imageWidth;
    }

    private void setImageHeight(int height) {
        this.imageHeight = height;
    }

    private int getImageHeight() {
        return imageHeight <= 0 ? (int) this.parentHeight : imageHeight;
    }

    private int left = -1, top = -1;
    private int imageWidth = 0;
    private int imageHeight = 0;

    private static final int ANIMATIONEACHOFFSET = 600;

//    private boolean emptyItem = false;
//
//    public void setEmpty() {
//        emptyItem = true;
//        relativeLeft.setVisibility(View.GONE);
//        relativeRight.setVisibility(View.GONE);
//    }

    //显示全部内容还是只显示点
    public void pointOrAll(boolean isLeft, boolean showAll) {
        if (isLeft) {
            relativeRight.setVisibility(GONE);
            nameLeft.setVisibility(GONE);
            pointRight.setVisibility(GONE);
            priceRight.setVisibility(GONE);
            if (showAll) {
                relativeLeft.setVisibility(VISIBLE);
                priceLeft.setVisibility(VISIBLE);
                pointLeft.setVisibility(VISIBLE);
                nameRight.setVisibility(VISIBLE);
            } else {
                relativeLeft.setVisibility(VISIBLE);
                priceLeft.setVisibility(INVISIBLE);
                nameRight.setVisibility(INVISIBLE);
                pointLeft.setVisibility(VISIBLE);
            }
        } else {
            relativeLeft.setVisibility(GONE);
            nameRight.setVisibility(GONE);
            pointLeft.setVisibility(GONE);
            priceLeft.setVisibility(GONE);
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
        }
    }

    public void wave() {
        final AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, ScaleAnimation.RELATIVE_TO_SELF,
                0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATIONEACHOFFSET * 3);
//        sa.setRepeatCount(10);// 设置循环
        AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
//        aniAlp.setRepeatCount(10);// 设置循环
        as.setDuration(ANIMATIONEACHOFFSET * 3);
        as.addAnimation(sa);
        as.addAnimation(aniAlp);
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (pointLeft.getVisibility() == VISIBLE) {
                    pointLeft.startAnimation(as);
                }
                if (pointRight.getVisibility() == VISIBLE) {
                    pointRight.startAnimation(as);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (pointLeft.getVisibility() == VISIBLE) {
            pointLeft.startAnimation(as);
        }
        if (pointRight.getVisibility() == VISIBLE) {
            pointRight.startAnimation(as);
        }
    }

    public void stopAnim() {
        pointRight.clearAnimation();
        pointLeft.clearAnimation();
    }

    public void updateLocation(int x, int y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        setupLocation(x, y);
        wave();
    }
}

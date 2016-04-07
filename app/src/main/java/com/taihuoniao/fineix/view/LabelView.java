package com.taihuoniao.fineix.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.TagItem;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.utils.EffectUtil;


/**
 * @author tongqian.ni
 */
public class LabelView extends LinearLayout {

    private TagItem tagInfo = new TagItem();
    private float parentWidth = 0;
    private float parentHeight = 0;
    private ImageView labelIcon;
    private RelativeLayout relativeLeft;
    private TextView labelTxtLeft;
    private TextView priceTxtLeft;
    private RelativeLayout relativeRight;
    private TextView labelTxtRight;
    private TextView priceTxtRight;

    public TagItem getTagInfo() {
        return tagInfo;
    }

    public LabelView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
        labelIcon = (ImageView) findViewById(R.id.view_label_point);
        relativeLeft = (RelativeLayout) findViewById(R.id.view_label_left);
        labelTxtLeft = (TextView) findViewById(R.id.view_label_left_name);
        priceTxtLeft = (TextView) findViewById(R.id.view_label_left_price);
        relativeRight = (RelativeLayout) findViewById(R.id.view_label_right);
        labelTxtRight = (TextView) findViewById(R.id.view_label_right_name);
        priceTxtRight = (TextView) findViewById(R.id.view_label_right_price);
    }

    public LabelView(Context context, AttributeSet attr) {
        super(context, attr);
        LayoutInflater.from(context).inflate(R.layout.view_label, this);
        labelIcon = (ImageView) findViewById(R.id.view_label_point);
        relativeLeft = (RelativeLayout) findViewById(R.id.view_label_left);
        labelTxtLeft = (TextView) findViewById(R.id.view_label_left_name);
        priceTxtLeft = (TextView) findViewById(R.id.view_label_left_price);
        relativeRight = (RelativeLayout) findViewById(R.id.view_label_right);
        labelTxtRight = (TextView) findViewById(R.id.view_label_right_name);
        priceTxtRight = (TextView) findViewById(R.id.view_label_right_price);
    }

    public void init(TagItem tagItem) {
//        tagInfo.setName(tagItem.getName());
//        tagInfo.setId(tagItem.getId());
//        tagInfo.setType(tagItem.getType());
        tagInfo = tagItem;
        labelTxtLeft.setText(tagItem.getName());
        labelTxtRight.setText(tagItem.getName());
        priceTxtLeft.setText(tagItem.getPrice());
        priceTxtRight.setText(tagItem.getPrice());
        if (tagItem.getType() == 1) {
            labelIcon.setImageResource(R.mipmap.ic_launcher);
        }
    }

    /**
     * 将标签放置于对应RelativeLayout的对应位置，考虑引入postion作为参数？？
     *
     * @param parent
     * @param left
     * @param top
     */
    public void draw(ViewGroup parent, final int left, final int top, boolean isLeft) {
        this.parentWidth = parent.getWidth();
        if (parentWidth <= 0) {
            parentWidth = MainApplication.getContext().getScreenWidth();
        }
        setImageWidth((int) parentWidth);
        this.parentHeight = parentWidth;
        if (isLeft) {
            relativeRight.setVisibility(View.VISIBLE);
            relativeLeft.setVisibility(View.GONE);
            setupLocation(left, top);
            parent.addView(this);
        } else {
            relativeRight.setVisibility(View.GONE);
            relativeLeft.setVisibility(View.VISIBLE);
            setupLocation(left, top);
            parent.addView(this);
        }

    }

    /**
     * 将标签放置于对应RelativeLayout的对应位置，考虑引入postion作为参数？？
     *
     * @param parent
     * @param left
     * @param top
     */
    public void addTo(MyImageViewTouch overlay, ViewGroup parent, final int left, final int top) {
        if (left > parent.getWidth() / 2) {
            tagInfo.setLeft(false);
        }
        this.parentWidth = overlay.getMeasuredWidth();
        if (parentWidth <= 0) {
            parentWidth = MainApplication.getContext().getScreenWidth();
        }
        setImageWidth((int) parentWidth);
        this.parentHeight = overlay.getMeasuredHeight();
        setImageHeight((int) this.parentHeight);
        if (emptyItem) {
            relativeRight.setVisibility(View.GONE);
            relativeLeft.setVisibility(View.GONE);
            setupLocation(left, top);
            parent.addView(this);
        } else if (tagInfo.isLeft()) {
            relativeRight.setVisibility(View.VISIBLE);
            relativeLeft.setVisibility(View.GONE);
            setupLocation(left, top);
            parent.addView(this);
        } else {
            relativeRight.setVisibility(View.GONE);
            relativeLeft.setVisibility(View.INVISIBLE);
            setupLocation(20, 20);
            parent.addView(this);
            post(new Runnable() {
                @Override
                public void run() {
                    int toLeft = left - getWidth() + labelIcon.getWidth();
                    setupLocation(toLeft, top);
                    relativeLeft.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    private void setupLocation(int leftLoc, int topLoc) {
        this.left = leftLoc;
        this.top = topLoc;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        if (getImageWidth() - left - getWidth() < 0) {
            left = getImageWidth() - getWidth();
        }
        if (getImageHeight() - top - getHeight() < 0) {
            top = getImageHeight() - getHeight();
        }
        if (left < 0 && top < 0) {
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if (left < 0) {
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.setMargins(0, top, 0, 0);
        } else if (top < 0) {
            params.addRule(RelativeLayout.CENTER_VERTICAL);
            params.setMargins(left, 0, 0, 0);
        } else {
            params.setMargins(left, top, 0, 0);
        }

        tagInfo.setX(EffectUtil.getStandDis(left, this.parentWidth));
        tagInfo.setY(EffectUtil.getStandDis(top, this.parentHeight));
        setLayoutParams(params);
    }

    private void setImageWidth(int width) {
        this.imageWidth = width;
    }

    private int getImageWidth() {
        return imageWidth <= 0 ? MainApplication.getContext().getScreenWidth() : imageWidth;
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

    private boolean emptyItem = false;

    public void setEmpty() {
        emptyItem = true;
        relativeLeft.setVisibility(View.GONE);
        relativeRight.setVisibility(View.GONE);
    }

    //链接点的动画
    public void wave() {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, ScaleAnimation.RELATIVE_TO_SELF,
                0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(ANIMATIONEACHOFFSET * 3);
        sa.setRepeatCount(10);// 设置循环
        AlphaAnimation aniAlp = new AlphaAnimation(1, 0.1f);
        aniAlp.setRepeatCount(10);// 设置循环
        as.setDuration(ANIMATIONEACHOFFSET * 3);
        as.addAnimation(sa);
        as.addAnimation(aniAlp);
        labelIcon.startAnimation(as);
    }

    public void updateLocation(int x, int y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        setupLocation(x, y);
        wave();
    }
}

package com.taihuoniao.fineix.view;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;

/**
 * Created by taihuoniao on 2016/3/16.
 */
public class GlobalTitleLayout extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private View view;
    private ImageButton backImg;
    private ImageView shareImg;
    private ImageView cancelImg;//默认隐藏
    private LinearLayout titleLinear;
    private TextView titleName;
    private ImageView arrowImg;//默认隐藏
    private TextView continueTv;
    private ImageView flashImg;//默认隐藏
    private RelativeLayout cartRelative;//右侧购物车  默认隐藏
    private TextView cartNum;//购物车数量  默认隐藏

    public GlobalTitleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        setListener();
    }

    private void setListener() {
        cancelImg.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    private void initView() {
        view = View.inflate(context, R.layout.title, GlobalTitleLayout.this);
        view.setBackgroundResource(R.color.black);
        backImg = (ImageButton) view.findViewById(R.id.title_back);
        shareImg = (ImageView) view.findViewById(R.id.title_share);
        cancelImg = (ImageView) view.findViewById(R.id.title_cancel);
        titleLinear = (LinearLayout) view.findViewById(R.id.title_linear);
        titleName = (TextView) view.findViewById(R.id.title_name);
        arrowImg = (ImageView) view.findViewById(R.id.title_arrow);
        continueTv = (TextView) view.findViewById(R.id.title_continue);
        flashImg = (ImageView) view.findViewById(R.id.title_flash);
        cartRelative = (RelativeLayout) view.findViewById(R.id.title_cart);
        cartNum = (TextView) view.findViewById(R.id.cart_number);
    }


    public void setCartListener(OnClickListener onClickListener) {
        cartRelative.setVisibility(VISIBLE);
        continueTv.setVisibility(GONE);
        cartRelative.setOnClickListener(onClickListener);
    }

    public void setCartNum(int num) {
        if (num > 0) {
            cartNum.setText(num + "");
            cartNum.setVisibility(VISIBLE);
        } else {
            cartNum.setVisibility(GONE);
        }
    }

    //设置背景颜色
    public void setColor(int resid) {
        view.setBackgroundResource(resid);
    }

    //控制取消按钮的显示隐藏
    public void setCancelImgVisible(boolean visible) {
        if (visible) {
            cancelImg.setVisibility(VISIBLE);
            backImg.setVisibility(GONE);
        } else {
            cancelImg.setVisibility(GONE);
        }
    }

    //设置分享按钮的显示隐藏
    public void setShareImgVisible(boolean visible, OnClickListener onClickListener) {
        if (visible) {
            shareImg.setVisibility(VISIBLE);
        } else {
            shareImg.setVisibility(GONE);
        }
        shareImg.setOnClickListener(onClickListener);
    }

    //设置取消按钮的图片
    public void setCancelImg(int resid) {
        cancelImg.setImageResource(resid);
    }

    //取消按钮的监听
    public void setCancelListener(OnClickListener onClickListener) {
        cancelImg.setOnClickListener(onClickListener);
    }

    //控制返回按钮的显示隐藏
    public void setBackImgVisible(boolean visible) {
        if (visible)
            backImg.setVisibility(VISIBLE);
        else
            backImg.setVisibility(GONE);
    }

    //返回按钮的监听
    public void setBackListener(OnClickListener onClickListener) {
        backImg.setOnClickListener(onClickListener);
    }

    //返回按钮的图标
    public void setBackImg(int imgresid) {
        backImg.setImageResource(imgresid);
    }

    //标题的监听
    public void setTitleLinearListener(OnClickListener onClickListener) {
        titleLinear.setOnClickListener(onClickListener);
    }

    //设置标题是否可见,默认可见
    public void setTitleVisible(boolean visible) {
        if (visible) {
            titleLinear.setVisibility(VISIBLE);
        } else {
            titleLinear.setVisibility(GONE);
        }
    }

    //设置标题
    public void setTitle(String title) {
        titleName.setText(title);
    }

    public void setTitle(int resid) {
        titleName.setText(resid);
    }

    public void setTitle(int resid, int color) {
        titleName.setText(resid);
        titleName.setTextColor(color);
    }

    //控制title箭头的显示隐藏
    public void setArrowImgVisible(boolean visible) {
        if (visible)
            arrowImg.setVisibility(VISIBLE);
        else
            arrowImg.setVisibility(GONE);
    }

    //设置箭头图片
    public void setArrowImgResource(int resid) {
        arrowImg.setVisibility(VISIBLE);
        arrowImg.setImageResource(resid);
    }

    //控制继续按钮的显示隐藏
    public void setContinueTvVisible(boolean visible) {
        if (visible)
            continueTv.setVisibility(VISIBLE);
        else
            continueTv.setVisibility(GONE);
    }


    //改变右侧按钮的名称、颜色，并设置监听
    public void setRightTv(int resid, int color, OnClickListener onClickListener) {
        continueTv.setText(resid);
        continueTv.setVisibility(VISIBLE);
        continueTv.setTextColor(color);
        if (onClickListener != null) {
            continueTv.setOnClickListener(onClickListener);
        }
    }

    //设置继续监听
    public void setContinueListener(OnClickListener onClickListener) {
        continueTv.setVisibility(VISIBLE);
        continueTv.setOnClickListener(onClickListener);
    }


    //闪光灯显示并设置监听
    public void setFlashImgListtener(OnClickListener onClickListener) {
        flashImg.setVisibility(VISIBLE);
        flashImg.setOnClickListener(onClickListener);
    }

    //闪光灯设置图片
    public void setFlashImgResource(int resid) {
        flashImg.setImageResource(resid);
    }


    @Override
    public void onClick(View v) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.onBackPressed();
        }
    }
}

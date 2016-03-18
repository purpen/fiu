package com.taihuoniao.fineix.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
    private ImageView backImg;
    private ImageView cancelImg;//默认隐藏
    private LinearLayout titleLinear;
    private TextView titleName;
    private ImageView arrowImg;//默认隐藏
    private TextView continueTv;
    private ImageView flashImg;//默认隐藏

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
        View view = View.inflate(context, R.layout.title, GlobalTitleLayout.this);
        backImg = (ImageView) view.findViewById(R.id.title_back);
        cancelImg = (ImageView) view.findViewById(R.id.title_cancel);
        titleLinear = (LinearLayout) view.findViewById(R.id.title_linear);
        titleName = (TextView) view.findViewById(R.id.title_name);
        arrowImg = (ImageView) view.findViewById(R.id.title_arrow);
        continueTv = (TextView) view.findViewById(R.id.title_continue);
        flashImg = (ImageView) view.findViewById(R.id.title_flash);
    }

    //控制取消按钮的显示隐藏
    public void setCancelImgVisible(boolean visible) {
        if (visible)
            cancelImg.setVisibility(VISIBLE);
        else
            cancelImg.setVisibility(GONE);
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

    //标题的监听
    public void setTitleLinearListener(OnClickListener onClickListener) {
        titleLinear.setOnClickListener(onClickListener);
    }

    //设置标题
    public void setTitle(String title) {
        titleName.setText(title);
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
        arrowImg.setImageResource(resid);
    }

    //控制继续按钮的显示隐藏
    public void setContinueTvVisible(boolean visible) {
        if (visible)
            continueTv.setVisibility(VISIBLE);
        else
            continueTv.setVisibility(GONE);
    }

    //设置继续监听
    public void setContinueListener(OnClickListener onClickListener) {
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

package com.taihuoniao.fineix.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.utils.Util;

public class CustomHeadView extends RelativeLayout {
    private Context context;
    private ImageView head_goback;
    private ImageView iv_left;
    private TextView head_center_tv;
    private ImageView iv_center_logo;
    private ImageView iv_head_search;
    private ImageView head_view_shop;
    private RelativeLayout rl_head_shop;
    private TextView tv_head_right;
    private TextView tv_tip_num;
    private SearchView sv;
    public CustomHeadView(Context context) {
        this(context, null);
    }

    public CustomHeadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        inflatelayout(context);
    }

    private void inflatelayout(Context context) {
        View view = Util.inflateView(context, R.layout.custom_headview_layout, this);
        initViews(view);
    }

    private void initViews(View view) {
        head_goback = (ImageView) view.findViewById(R.id.head_goback);
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        head_center_tv = (TextView) view.findViewById(R.id.head_center_tv);
        iv_center_logo = (ImageView) view.findViewById(R.id.iv_center_logo);
        sv = (SearchView) view.findViewById(R.id.sv_head_view);
        iv_head_search = (ImageView) view.findViewById(R.id.iv_head_search);
        rl_head_shop = (RelativeLayout) view.findViewById(R.id.rl_head_shop);
//        head_view_shop = (ImageButton) view.findViewById(R.id.head_view_shop);
        tv_tip_num=(TextView)view.findViewById(R.id.tv_tip_num);
        tv_head_right = (TextView) view.findViewById(R.id.tv_head_right);
        head_goback.setOnClickListener(onClickListener);
//        head_view_shop.setOnClickListener(onClickListener);
        rl_head_shop.setOnClickListener(onClickListener);
        iv_head_search.setOnClickListener(onClickListener);
        iv_left.setOnClickListener(onClickListener);
    }


    private OnClickListener onClickListener=new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.head_goback:
                    ((Activity)context).onBackPressed();
                    break;
                case R.id.iv_left:
                    //TODO
                    break;
//                case R.id.head_view_shop:
//                    if (LoginUtil.isLogin()){
//                        activity.startActivity(new Intent(activity, UserShopCartActivity.class));
//                    }else {
//                        activity.startActivity(new Intent(activity, UserLoginActivity.class));
//                    }
//                    break;
                case R.id.rl_head_shop:
//                    if (LoginUtil.isLogin()){
//                        activity.startActivity(new Intent(activity, UserShopCartActivity.class));
//                    }else {
//                        activity.startActivity(new Intent(activity, UserLoginActivity.class));
//                    }
                    break;
                case R.id.iv_head_search:
//                    activity.startActivity(new Intent(activity, UserSearchActivity.class));
                    break;
            }
        }
    };

    public void setTipsNum(int num){
        if (num>0){
            tv_tip_num.setVisibility(VISIBLE);
            tv_tip_num.setText(String.valueOf(num));
        }else {
            tv_tip_num.setVisibility(GONE);
        }
    }

    public void setHeadGoBackShow(boolean isShow){
        if (isShow) {
            head_goback.setVisibility(View.VISIBLE);
        } else {
            head_goback.setVisibility(View.GONE);
        }
    }

    public void setIvLeft(int imgId){
            iv_left.setImageResource(imgId);
    }

    public ImageView getIvLeft(){
       return iv_left;
    }
    public void setHeadSearchShow(boolean isShow) {
        if (isShow) {
            iv_head_search.setVisibility(View.VISIBLE);
        } else {
            iv_head_search.setVisibility(View.GONE);
        }

    }

    public void setHeadShopShow(boolean isShow) {
        if (isShow) {
            rl_head_shop.setVisibility(VISIBLE);
//            head_view_shop.setVisibility(VISIBLE);
        } else {
            rl_head_shop.setVisibility(GONE);
//            head_view_shop.setVisibility(GONE);
        }

    }

    public void setHeadLogoShow(boolean isShow) {
        if (isShow) {
            iv_center_logo.setVisibility(View.VISIBLE);
        } else {
            iv_center_logo.setVisibility(View.GONE);
        }

    }

    public void setHeadCenterTxtShow(boolean isShow, int resId) {
        if (isShow) {
            head_center_tv.setVisibility(View.VISIBLE);
            head_center_tv.setText(resId);
        } else {
            head_center_tv.setVisibility(View.GONE);
        }

    }
    public void setHeadCenterTxtShow(boolean isShow, String string) {
        if (isShow) {
            head_center_tv.setVisibility(View.VISIBLE);
            head_center_tv.setText(string);
        } else {
            head_center_tv.setVisibility(View.GONE);
        }

    }

    public void setHeadRightTxtShow(boolean isShow, int resId) {
        if (isShow) {
            tv_head_right.setVisibility(View.VISIBLE);
            tv_head_right.setText(resId);
        } else {
            tv_head_right.setVisibility(View.GONE);
        }

    }

    public TextView getHeadRightTV(){
        return tv_head_right;
    }
    public SearchView getSearchView(){
        return sv;
    }
}

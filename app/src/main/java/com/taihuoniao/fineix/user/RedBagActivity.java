package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CheckRedBagUsable;
import com.taihuoniao.fineix.beans.RedBagUntimeout;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;


public class RedBagActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    //下拉刷新
    private PullToRefreshListView mPullLayout;
    private Animation mRotateUpAnimation;
    private Animation mRotateDownAnimation;
    private boolean mInLoading = false;
    private View mProgress;
    private View mActionImage;
    private TextView mActionText;
    private TextView mTimeText;

    private List<RedBagUntimeout> mUntimeoutList = new ArrayList<>();//
    private View mUntimeoutView;
    private View mTimeoutView;
    private LinearLayout mUntimeoutLinear;
    private LinearLayout mTimeoutLinear;
    private TextView mLook;
    private LinearLayout mLinearLook;
    public static final String UNUSED = "1";//未使用过
    public static final String ALLUSED = "0";//使用和未使用的全部
    public static final String UNTIMEOUT = "1";//未过期
    public static final String TIMEOUT = "2";//已过期
    private boolean mLookClick = false;
    private String mRid;//订单号
    private WaittingDialog mDialog;

    public RedBagActivity(){
        super(R.layout.activity_red_bag);
    }

    //在这处理onSnapToTop()方法中传出来的handler message:
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CUSTOM_PULLTOREFRESH_HOME:
                    dataLoadFinished();
                    break;
                case DataConstants.PARSER_CHECK_REDBAG_USABLE:
                    if (msg.obj != null) {
                        if (msg.obj instanceof CheckRedBagUsable) {
                            CheckRedBagUsable checkRedBagUsable= (CheckRedBagUsable) msg.obj;
                            if ("1".equals(checkRedBagUsable.getUseful())) {
                                //红包可用
                                Intent intent = new Intent();
                                intent.putExtra("code", checkRedBagUsable.getCode());//红包码
                                intent.putExtra("money", checkRedBagUsable.getCoin_money());//红包金额
                                setResult(DataConstants.RESULTCODE_REDBAG, intent);
                                finish();
                            }else {
                                //红包不可用
                                Toast.makeText(RedBagActivity.this,"这个红包不可用",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_MY_REDBAG_UNTIMEOUT:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mUntimeoutList.clear();
                            mUntimeoutList.addAll((List<RedBagUntimeout>) msg.obj);
                            if (mUntimeoutLinear != null) {
                                mUntimeoutLinear.removeAllViews();
                            }
                            for (int i = 0; i < mUntimeoutList.size(); i++) {
                                mUntimeoutView = LayoutInflater.from(RedBagActivity.this).inflate(R.layout.account_redbag_untimeout, null);

                                TextView mRedbagCode = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_redbag_code);
                                TextView mMinMoney = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_min_money);
                                TextView mRedbagMoney = (TextView) mUntimeoutView
                                        .findViewById(R.id.tv_money);

                                mRedbagCode
                                        .setText("红包码：" + mUntimeoutList.get(i).getCode());
                                mMinMoney
                                        .setText("最低使用限额：" + mUntimeoutList.get(i).getMin_amount() + "");
                                mRedbagMoney.setText(mUntimeoutList.get(i).getAmount() + "");
                                final int j = i;
                                mUntimeoutView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (mRid != null) {
//                                            验证红包是否可用
                                            DataPaser.checkRedbagUsableParser(MainApplication.uuid, mRid, mUntimeoutList.get(j).getCode(), mHandler);
                                        }
                                    }
                                });
                                mUntimeoutLinear.addView(mUntimeoutView);
                            }
                            mDialog.dismiss();
                        }
                    }
                    break;
                case DataConstants.PARSER_MY_REDBAG_TIMEOUT:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mUntimeoutList.clear();
                            mUntimeoutList.addAll((List<RedBagUntimeout>) msg.obj);
                            mLinearLook.setVisibility(View.GONE);
                            if (mTimeoutLinear != null) {
                                mTimeoutLinear.removeAllViews();
                            }
                            for (int i = 0; i < mUntimeoutList.size(); i++) {
                                mTimeoutView = LayoutInflater.from(RedBagActivity.this).inflate(R.layout.account_redbag_timeout, null);
                                TextView mRedbagCode = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_redbag_code);
                                TextView mMinMoney = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_min_money);
                                TextView mRedbagMoney = (TextView) mTimeoutView
                                        .findViewById(R.id.tv_money);
                                TextView mThnRedbag = (TextView) mTimeoutView.findViewById(R.id.tv_thn_redbag);
                                TextView mMoneySign = (TextView) mTimeoutView.findViewById(R.id.tv_money_sign);
                                mThnRedbag.setTextColor(getResources().getColor(R.color.color_333));
                                mMoneySign.setTextColor(getResources().getColor(R.color.color_333));
                                mRedbagCode.setText("红包码：" + mUntimeoutList.get(i).getCode());
                                mRedbagCode.setTextColor(getResources().getColor(R.color.color_333));
                                mMinMoney.setText("最低使用限额：" + mUntimeoutList.get(i).getMin_amount() + "");
                                mMinMoney.setTextColor(getResources().getColor(R.color.color_333));
                                mRedbagMoney.setText(mUntimeoutList.get(i).getAmount() + "");
                                mRedbagMoney.setTextColor(getResources().getColor(R.color.color_333));

                                mTimeoutLinear.addView(mTimeoutView);
                            }
                            mDialog.dismiss();
                        }
                    }
                    break;
            }
        }
    };

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"我的红包");
//        title.setRightSearchButton(false);
//        title.setRightShopCartButton(false);
        //下拉刷新
//        mRotateUpAnimation = AnimationUtils.loadAnimation(RedBagActivity.this,
//                R.anim.rotate_up);
//        mRotateDownAnimation = AnimationUtils.loadAnimation(RedBagActivity.this,
//                R.anim.rotate_down);
        mPullLayout = (PullToRefreshListView) RedBagActivity.this
                .findViewById(R.id.pull_container);
//        mPullLayout.setOnActionPullListener(this);
//        mPullLayout.setOnPullStateChangeListener(this);
//        mPullLayout.setEnableStopInActionView(true);
//        mProgress = RedBagActivity.this.findViewById(R.id.progress);
//        mActionImage = RedBagActivity.this.findViewById(R.id.icon);
//        mActionImage.setVisibility(View.VISIBLE);
//        mActionText = (TextView) RedBagActivity.this.findViewById(R.id.pull_note);
//        mActionText.setText("下拉刷新");
//        mTimeText = (TextView) RedBagActivity.this.findViewById(R.id.refresh_time);

        mUntimeoutLinear = (LinearLayout) findViewById(R.id.linear_no_timeout);
        mTimeoutLinear = (LinearLayout) findViewById(R.id.linear_timeout);
        mLook = (TextView) findViewById(R.id.tv_look_redbag);
        mLinearLook = (LinearLayout) findViewById(R.id.linear_look_redbag);

        mDialog = new WaittingDialog(this);
        mDialog.show();

        mRid = getIntent().getStringExtra("rid");
        Log.e(">>>", ">>>ridredbag>>" + mRid);
        //未过期未使用
        DataPaser.unTimeoutParser(MainApplication.uuid, UNUSED, UNTIMEOUT, mHandler);

        mLook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show();
                //已过期、使没使用全有
                mLookClick = true;
//                DataParser.unTimeoutParser(ALLUSED, TIMEOUT, mHandler);
                DataPaser.unTimeoutParser(MainApplication.uuid, ALLUSED, UNTIMEOUT, mHandler);
            }
        });

    }

    @Override
    protected void installListener() {
        mPullLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {

            }
        });

        mPullLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mInLoading) {
                    mInLoading = true;
                    DataPaser.unTimeoutParser(MainApplication.uuid, UNUSED, UNTIMEOUT, mHandler);
                    if (mLookClick) {  //查看过期红包
                        DataPaser.unTimeoutParser(MainApplication.uuid, ALLUSED, TIMEOUT, mHandler);
                        DataPaser.unTimeoutParser(MainApplication.uuid, ALLUSED, UNTIMEOUT, mHandler);
                    }
                    mHandler.sendEmptyMessageDelayed(DataConstants.CUSTOM_PULLTOREFRESH_HOME, 1000);
                }
            }
        });
    }

    private void dataLoadFinished() {
        if (mInLoading) {
            mInLoading = false;
            mPullLayout.onRefreshComplete();
            mPullLayout.setLoadingTime();
//            mPullLayout.setEnableStopInActionView(false);
//            mPullLayout.hideActionView();
//            mActionImage.setVisibility(View.VISIBLE);
//            mProgress.setVisibility(View.GONE);

//            if (mPullLayout.isPullOut()) {
//                mActionText.setText(R.string.note_pull_refresh);
//                mActionImage.clearAnimation();
//                mActionImage.startAnimation(mRotateUpAnimation);
//            } else {
//                mActionText.setText(R.string.note_pull_down);
//            }

//            mTimeText.setText(getString(R.string.note_update_at,
//                    formatDate(new Date(System.currentTimeMillis()))));
        }
    }

    //定义日期格式
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }

    private static DateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");


//    @Override
    public void onSnapToTop() {
// 下拉后，弹到顶部时，开始刷新数据
        if (!mInLoading) {
            mInLoading = true;
//            mPullLayout.setEnableStopInActionView(true);
//            mActionImage.clearAnimation();
//            mActionImage.setVisibility(View.GONE);
//            mProgress.setVisibility(View.VISIBLE);
//            mActionText.setText(R.string.note_pull_loading);

//            DataPaser.unTimeoutParser(MainApplication.uuid, UNUSED, UNTIMEOUT, mHandler);
//            if (mLookClick) {  //查看过期红包
//                DataPaser.unTimeoutParser(MainApplication.uuid, ALLUSED, TIMEOUT, mHandler);
//                DataPaser.unTimeoutParser(MainApplication.uuid, ALLUSED, UNTIMEOUT, mHandler);
//            }
//            mHandler.sendEmptyMessageDelayed(DataConstants.CUSTOM_PULLTOREFRESH_HOME, 1000);
        }
    }
//    @Override
//    public void onPullOut() {
//        if (!mInLoading) {
//            mActionText.setText(R.string.note_pull_refresh);
//            mActionImage.clearAnimation();
//            mActionImage.startAnimation(mRotateUpAnimation);
//        }
//    }
//
//    @Override
//    public void onPullIn() {
//        if (!mInLoading) {
//            mActionText.setText(R.string.note_pull_down);
//            mActionImage.clearAnimation();
//            mActionImage.startAnimation(mRotateDownAnimation);
//        }
//    }

}

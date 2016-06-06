package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ApplyForRefund;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApplyForRefundActivity extends Activity implements View.OnClickListener {
    private WaittingDialog mDialog = null;
    private TextView mReason;
    private TextView mMoney;
    private EditText mEditTextReason;
    private RelativeLayout mReasonLayout;
    private PopupWindow popupWindow = null;
    private MyGlobalTitleLayout title = null;
    private Button mCommit;
    private ImageView mImage;
    private String mRid;
    private String mReasonCode = "1";
    private String mReasonEditTxt = "";
    private List<OrderDetails> mList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_ORDER_DETAILS:
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mList.clear();
                            mList.addAll((Collection<? extends OrderDetails>) msg.obj);

                            for (int i = 0; i < mList.size(); i++) {
                                mMoney.setText("¥" + mList.get(i).getPay_money());
                            }
                        }
                    }
                    break;
                case DataConstants.PARSER_APPLY_REFUND:
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                    if (msg.obj != null) {
                        if (msg.obj instanceof ApplyForRefund) {
                            ApplyForRefund refund = (ApplyForRefund) msg.obj;

                            if ("true".equals(refund.getSuccess())) {
                                ToastUtils.showSuccess(refund.getMessage());
//                                new SVProgressHUD(ApplyForRefundActivity.this).showSuccessWithStatus(refund.getMessage());
                                onBackPressed();
                            } else {
                                ToastUtils.showError(refund.getMessage());
//                                new SVProgressHUD(ApplyForRefundActivity.this).showErrorWithStatus(refund.getMessage());
                            }
                        }
                    }
                case DataConstants.NETWORK_FAILURE:
                    mDialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_apply_for_refund);
        mDialog = new WaittingDialog(this);
        initView();
        initPopwindow();
        initData();
    }

    private void initData() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mRid = getIntent().getStringExtra("rid");
        DataPaser.orderPayDetailsParser(mRid, mHandler);
    }

    private void initView() {
        title = (MyGlobalTitleLayout) findViewById(R.id.title_refund);
        title.setTitle("申请退款");
        title.setBackgroundResource(R.color.white);
        title.setBackImg(R.mipmap.back_black);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackButtonVisibility(true);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        mEditTextReason = (EditText) findViewById(R.id.et_refund);
        mCommit = (Button) findViewById(R.id.bt_commit_refund);
        mMoney = (TextView) findViewById(R.id.tv_money_refund);
        mReason = (TextView) findViewById(R.id.tv_reason_refund);
        mImage = (ImageView) findViewById(R.id.image_refund);
        mReasonLayout = (RelativeLayout) findViewById(R.id.layout_reason_refund);
        mCommit.setOnClickListener(this);
        mReason.setOnClickListener(this);
        mReasonLayout.setOnClickListener(this);
        mImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_reason_refund:
                showPopupWindow();
                break;
            case R.id.bt_commit_refund:
                if (!mDialog.isShowing()) {
                    mDialog.show();
                }
                mReasonEditTxt = mEditTextReason.getText() + "";
                DataPaser.applyForRefundParser(mRid, mReasonCode, mReasonEditTxt, mHandler);
                break;
            case R.id.image_refund:
                showPopupWindow();
                break;
            case R.id.tv_reason_refund:
                showPopupWindow();
                break;
        }
    }

    private void initPopwindow() {
        View view = LayoutInflater.from(this).inflate(
                R.layout.popupwindow_refund, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }
        });
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout mEmptyLayout = (LinearLayout) view
                .findViewById(R.id.linearlayout_refund);
        mEmptyLayout.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
        Button mWant = (Button) view.findViewById(R.id.bt_not_want_refund);
        mWant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReason.setText("我不想要了");
                mReasonCode = "1";
                mEditTextReason.setVisibility(View.INVISIBLE);
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }

            }
        });
        Button mOther = (Button) view.findViewById(R.id.bt_other_refund);
        mOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReason.setText("其他");
                mReasonCode = "0";
                mEditTextReason.setVisibility(View.VISIBLE);
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
            }
        });
        Button mCancel = (Button) view.findViewById(R.id.bt_cancel_refund);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != popupWindow) {
                    popupWindow.dismiss();
                }
            }
        });
    }

    public void showPopupWindow() {
        popupWindow.setAnimationStyle(R.style.dialogstyle);
        popupWindow.showAsDropDown(title);
    }
}

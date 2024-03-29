package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.ConstantCfg;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.bean.ShoppingDetailBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class PayDetailsActivity extends BaseActivity {
    private WaittingDialog mDialog;
    private TextView mSuccessOrFailed;
    private ImageView mImage;
    private TextView mOrderNum;
    private TextView mTvPayway;
    private TextView mTradeTime;
    private String mRid, mPayway;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    public PayDetailsActivity(){
        super(R.layout.activity_pay_details);
    }

    @Override
    public void onBackPressed() {

        new DefaultDialog(this, App.getString(R.string.hint_dialog_goBack_mainPage_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
            @Override
            public void clickRight() {
                startActivity(new Intent(PayDetailsActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra("payway")){
            mPayway = getIntent().getStringExtra("payway");
        }
        if (intent.hasExtra("rid")){
            mRid = getIntent().getStringExtra("rid");
        }
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(mRid)) return;
        HashMap<String, String> params = ClientDiscoverAPI.getOrderPayNetRequestParams(mRid);
        HttpRequest.post(params,  URL.SHOPPING_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (mDialog!=null) mDialog.show();
            }

            @Override
            public void onSuccess(String json) {
                mDialog.dismiss();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<ShoppingDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ShoppingDetailBean>>() {
                });
                if (response.isSuccess()){
                    ShoppingDetailBean data = response.getData();
                    if (data.getStatus() == 10 || data.getStatus()==16) {  // 10 代表支付成功，16代表自提支付成功
                        mSuccessOrFailed.setText("您的订单已支付成功");
                        mImage.setImageResource(R.mipmap.success);
                    } else {
                        mSuccessOrFailed.setText("支付异常，请联系客服");
                        mImage.setImageResource(R.mipmap.fail);
                    }

                    if (ConstantCfg.ALI_PAY.equals(mPayway)) {
                        mTvPayway.setText("支付宝");
                    } else if (ConstantCfg.WX_PAY.equals(mPayway)) {
                        mTvPayway.setText("微信");
                    } else if (ConstantCfg.JD_PAY.equals(mPayway)) {
                        mTvPayway.setText("京东");
                    } else {
                        mTvPayway.setText("无");
                    }
                    mOrderNum.setText(data.getRid());
                    mTradeTime.setText(data.getCreated_at());
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                mDialog.dismiss();
                Util.makeToast("网络异常");
            }
        });
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"订单支付详情");
        custom_head.setHeadRightTxtShow(true, "完成");
        mDialog = new WaittingDialog(this);

        mSuccessOrFailed = (TextView) findViewById(R.id.tv_success_or_failed_pay_detail);
        mImage = (ImageView) findViewById(R.id.image_paydetails);
        Button mLookDetails = (Button) findViewById(R.id.bt_look_order_pay_details);
        mOrderNum = (TextView) findViewById(R.id.tv_number_order_details);
        mTvPayway = (TextView) findViewById(R.id.tv_payway_order_details);
        mTradeTime = (TextView) findViewById(R.id.tv_time_order_details);

        mLookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOrderDetailsActivity();
            }
        });
        WindowUtils.chenjin(this);
    }


    @OnClick(R.id.tv_head_right)
    void performClick(View v){
        switch (v.getId()){
            case R.id.tv_head_right:
                startActivity(new Intent(PayDetailsActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void toOrderDetailsActivity() {
        Intent intent = new Intent(PayDetailsActivity.this, OrderDetailsActivity.class);
        intent.putExtra("rid", mRid);
        startActivity(intent);
    }
}

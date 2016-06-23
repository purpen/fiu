package com.taihuoniao.fineix.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import butterknife.Bind;
import butterknife.OnClick;

public class PayDetailsActivity extends BaseActivity {
    private WaittingDialog mDialog;
    private TextView mSuccessOrFailed;
    private ImageView mImage;
    private TextView mOrderNum;
    private TextView mTvPayway;
    private TextView mTradeTime;
    private Button mLookDetails;
    private String mRid, mPayway;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
//    private List<OrderDetails> orderDetailsList = new ArrayList<>();
    public PayDetailsActivity(){
        super(R.layout.activity_pay_details);
    }
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DataConstants.PARSER_ORDER_DETAILS:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof List) {
//                            orderDetailsList.clear();
//                            orderDetailsList.addAll((Collection<? extends OrderDetails>) msg.obj);
//                            for (int i = 0; i < orderDetailsList.size(); i++) {
//                                if ("10".equals(orderDetailsList.get(i).getStatus())) {
//                                    mSuccessOrFailed.setText("您的订单已支付成功");
//                                    mImage.setImageResource(R.mipmap.success);
//                                } else {
//                                    mSuccessOrFailed.setText("支付异常，请联系客服");
//                                    mImage.setImageResource(R.mipmap.fail);
//                                }
//                                if (mPayway.equals("alipay")) {
//                                    mTvPayway.setText("支付宝");
//                                } else {
//                                    mTvPayway.setText("微信");
//                                }
//                                mOrderNum.setText(orderDetailsList.get(i).getRid());
//                                mTradeTime.setText(orderDetailsList.get(i).getCreated_at());
//                            }
//                            mDialog.dismiss();
//                        }
//                    }
//                    break;
//            }
//        }
//    };

    @Override
    public void onBackPressed() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(PayDetailsActivity.this);
        dialog.setTitle("您确定要回到主界面吗？");
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(PayDetailsActivity.this, MainActivity.class));
                finish();
            }
        });
        dialog.show();

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
//        setContentView(R.layout.activity_pay_details);
//        ActivityUtil.getInstance().addActivity(this);

//        initView();
//        initData();
//    }


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
        ClientDiscoverAPI.OrderPayNet(mRid, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (mDialog!=null) mDialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                mDialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<OrderDetails> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<OrderDetails>>() {
                });
                if (response.isSuccess()){
                    OrderDetails data = response.getData();
                    if ("10".equals(data.getStatus())) {
                        mSuccessOrFailed.setText("您的订单已支付成功");
                        mImage.setImageResource(R.mipmap.success);
                    } else {
                        mSuccessOrFailed.setText("支付异常，请联系客服");
                        mImage.setImageResource(R.mipmap.fail);
                    }
                    if (mPayway.equals("alipay")) {
                        mTvPayway.setText("支付宝");
                    } else {
                        mTvPayway.setText("微信");
                    }
                    mOrderNum.setText(data.getRid());
                    mTradeTime.setText(data.getCreated_at());
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mDialog.dismiss();
                Util.makeToast("网络异常");
            }
        });
    }

    private void initData() {
//        mDialog.show();
//        mPayway = getIntent().getStringExtra("payway");
//        mRid = getIntent().getStringExtra("rid");
//        DataParser.orderPayDetailsParser(THNApplication.uuid,mRid, mHandler);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"订单支付详情");
        custom_head.setHeadRightTxtShow(true, "完成");
        mDialog = new WaittingDialog(this);

        mSuccessOrFailed = (TextView) findViewById(R.id.tv_success_or_failed_pay_detail);
        mImage = (ImageView) findViewById(R.id.image_paydetails);
        mLookDetails = (Button) findViewById(R.id.bt_look_order_pay_details);
        mOrderNum = (TextView) findViewById(R.id.tv_number_order_details);
        mTvPayway = (TextView) findViewById(R.id.tv_payway_order_details);
        mTradeTime = (TextView) findViewById(R.id.tv_time_order_details);

        mLookDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOrderDetailsActivity();
            }
        });
    }


    @OnClick(R.id.tv_head_right)
    void performClick(View v){
        switch (v.getId()){
            case R.id.tv_head_right:
                toOrderDetailsActivity();
                break;
        }
    }

    private void toOrderDetailsActivity() {
        Intent intent = new Intent(PayDetailsActivity.this, OrderDetailsActivity.class);
        intent.putExtra("rid", mRid);
        startActivity(intent);
    }
}

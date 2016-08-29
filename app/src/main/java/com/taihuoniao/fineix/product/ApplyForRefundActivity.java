package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.ApplyForRefund;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.beans.OrderDetailsAddress;
import com.taihuoniao.fineix.beans.OrderDetailsProducts;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApplyForRefundActivity extends Activity implements View.OnClickListener {
    private WaittingDialog mDialog = null;
    private TextView mReason;
    private TextView mMoney;
    private EditText mEditTextReason;
    private RelativeLayout mReasonLayout;
    private PopupWindow popupWindow = null;
    private GlobalTitleLayout title = null;
    private Button mCommit;
    private ImageView mImage;
    private String mRid;
    private String mReasonCode = "1";
    private String mReasonEditTxt = "";
    private List<OrderDetails> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_apply_for_refund);
        mDialog = new WaittingDialog(this);
        initView();
        WindowUtils.chenjin(this);
        initPopwindow();
        initData();
    }

    private void initData() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        mRid = getIntent().getStringExtra("rid");
//        DataPaser.orderPayDetailsParser(mRid, mHandler);
        ClientDiscoverAPI.OrderPayNet(mRid, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e(">>>", ">>>OOO>>>" + responseInfo.result);
                List<OrderDetails> ordersList = new ArrayList<OrderDetails>();
//                Message msg = new Message();
//                msg.what = DataConstants.PARSER_ORDER_DETAILS;
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject orderObj = obj.getJSONObject("data");
                    OrderDetails details = new OrderDetails();
                    details.setRid(orderObj.optString("rid"));
                    details.setExpress_company(orderObj.optString("express_company"));
                    details.setExpress_no(orderObj.optString("express_no"));
                    details.setCreated_at(orderObj.optString("created_at"));
                    details.setFreight(orderObj.optString("freight"));
                    details.setItems_count(orderObj.optString("items_count"));
                    details.setPay_money(orderObj.optString("pay_money"));
                    details.setPayment_method(orderObj.optString("payment_method"));
                    details.setTotal_money(orderObj.optString("total_money"));
                    details.setStatus(orderObj.optString("status"));
                    JSONObject arr = orderObj.getJSONObject("express_info");
                    List<OrderDetailsAddress> addressList = new ArrayList<OrderDetailsAddress>();
                    OrderDetailsAddress address = new OrderDetailsAddress();
                    address.setAddress(arr.optString("address"));
                    address.setName(arr.optString("name"));
                    address.setCity(arr.optString("city"));
                    address.setPhone(arr.optString("phone"));
                    address.setProvince(arr.optString("province"));
                    addressList.add(address);

                    details.setAddresses(addressList);
                    JSONArray productsArrays = orderObj.getJSONArray("items");
                    List<OrderDetailsProducts> productsList = new ArrayList<OrderDetailsProducts>();
                    for (int j = 0; j < productsArrays.length(); j++) {
                        JSONObject productsArr = productsArrays.getJSONObject(j);
                        OrderDetailsProducts products = new OrderDetailsProducts();
                        products.setName(productsArr.optString("name"));
                        products.setCover_url(productsArr.optString("cover_url"));
                        products.setPrice(productsArr.optString("price"));
                        products.setProduct_id(productsArr.optString("product_id"));
                        products.setQuantity(productsArr.optString("quantity"));
                        products.setSale_price(productsArr.optDouble("sale_price"));
                        products.setSku(productsArr.optString("sku"));
                        products.setSku_name(productsArr.optString("sku_name"));
                        productsList.add(products);
                    }
                    details.setProducts(productsList);
                    ordersList.add(details);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                mList.clear();
                mList.addAll(ordersList);
                for (int i = 0; i < mList.size(); i++) {
                    mMoney.setText("¥" + mList.get(i).getPay_money());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                mDialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void initView() {
        title = (GlobalTitleLayout) findViewById(R.id.title_refund);
        title.setTitle("申请退款");
    title.setContinueTvVisible(false);
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
//                DataPaser.applyForRefundParser(mRid, mReasonCode, mReasonEditTxt, mHandler);
                ClientDiscoverAPI.applyForRefundNet(mRid, mReasonCode, mReasonEditTxt, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                        Log.e("<<<申请退款", responseInfo.result);
                        ApplyForRefund refund = new ApplyForRefund();
//                        Message msg = new Message();
//                        msg.what = DataConstants.PARSER_APPLY_REFUND;
                        try {
                            JSONObject obj = new JSONObject(responseInfo.result);
                            JSONObject refundObj = obj.getJSONObject("data");
                            refund.setSuccess(obj.optString("success"));
                            refund.setMessage(obj.optString("message"));
                            refund.setRid(refundObj.optString("rid"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        if ("true".equals(refund.getSuccess())) {
                            ToastUtils.showSuccess(refund.getMessage());
//                                new SVProgressHUD(ApplyForRefundActivity.this).showSuccessWithStatus(refund.getMessage());
                            onBackPressed();
                        } else {
                            ToastUtils.showError(refund.getMessage());
//                                new SVProgressHUD(ApplyForRefundActivity.this).showErrorWithStatus(refund.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        mDialog.dismiss();
                        ToastUtils.showError("网络错误");
                    }
                });
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
        View view = View.inflate(this,R.layout.popupwindow_refund,null);
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

package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.Base2Activity;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.personal.salesevice.ChargeBackActivity;
import com.taihuoniao.fineix.personal.salesevice.KEY;
import com.taihuoniao.fineix.personal.salesevice.SalesReturnActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.PayWayActivity;
import com.taihuoniao.fineix.user.bean.OrderDetailBean;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.lang.reflect.Type;
import java.util.List;

public class OrderDetailsActivity extends Base2Activity implements View.OnClickListener {
    private static final String TAG = "OrderDetailsActivity";

    private TextView mDeliverMan;
    private TextView mCity;
    private TextView mProvince;
    private TextView mDetailsAddress;
    private TextView mPhone;
    private TextView mOrderNum;
    private TextView mPayway;
    private TextView mTotalMoney;
    private TextView mPayMoney;
    private TextView mFreight;
    private TextView mRightButton;//最底部右侧按钮
    private TextView mLeftButton;//最底部左侧按钮
    private RelativeLayout mBottomLayout;//放最底部那俩按钮的布局
    private LinearLayout mContainerLayout;//动态容纳商品item

    private String mRid;
    private String mOptFragmentFlag;//跳回碎片列表时，用该值选中跳来之前的那个碎片
    private RelativeLayout mLogisticsCompanyLayout;//物流公司
    private RelativeLayout mLogisticsNumberLayout;//物流单号
    private TextView mLogisticsNumber, mLogisticsCompany;

    private WaittingDialog mDialog;
//    private AlertDialog.Builder alertDialog;
    private TextView mCounty;
    private TextView mTown;
    private OrderDetailBean orderDetailBean;

    private void toShopOrderListActivity() {
        Intent in = new Intent(OrderDetailsActivity.this, ShopOrderListActivity.class);
        in.putExtra("optFragmentFlag", mOptFragmentFlag);
        OrderDetailsActivity.this.startActivity(in);
        OrderDetailsActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initView();
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mRid = getIntent().getStringExtra("rid");
        mOptFragmentFlag = getIntent().getStringExtra("optFragmentFlag");
        requestData();
    }

    private void requestData() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        ClientDiscoverAPI.OrderPayNet(mRid, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                try {
                    HttpResponse<OrderDetailBean> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<OrderDetailBean>>() { });
                    if (response.isError()) {
                        LogUtil.e(TAG, "---------> responseInfo: " + responseInfo.reasonPhrase);
                        return;
                    }
                    orderDetailBean = response.getData();
                } catch (Exception e) {
                    LogUtil.e(TAG, "-----------> e: " + e);
                }

                refreshUI();

                if (mDialog != null) {
                    if (mDialog.isShowing()) {
                        mDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mDialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void refreshUI() {
        initOrderStatus();
        setBottomData();
        setOrderGoodsList();
    }

    private void setOrderGoodsList() {
        int exist_sub_order = orderDetailBean.getExist_sub_order();
        if (mContainerLayout != null) {
            mContainerLayout.removeAllViews();
        }
        if (exist_sub_order == 1) { //有子订单
            List<OrderDetailBean.SubOrdersBean> sub_orders = orderDetailBean.getSub_orders();

            View inflate = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.layout_goods_details_up_sub, null);
            ((TextView)inflate.findViewById(R.id.textView_suborder_number)).setText(orderDetailBean.getRid());
            mContainerLayout.addView(inflate);

            for (int m = 0; m < sub_orders.size(); m++) {
                LinearLayout subOrderView = (LinearLayout) LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.layout_goods_details_order_multi, null);
                TextView textView1 = (TextView) subOrderView.findViewById(R.id.textView_order_number);
                TextView textView2 = (TextView) subOrderView.findViewById(R.id.textView_order_status);
                TextView textViewExpress = (TextView) subOrderView.findViewById(R.id.textView_express);
                TextView textViewExpressCode = (TextView) subOrderView.findViewById(R.id.textView_express_code);
                LinearLayout linearLayoutContainerGoods = (LinearLayout) subOrderView.findViewById(R.id.linearLayout_container_subOrder_details);
                TextView textViewExpressTracking = (TextView) subOrderView.findViewById(R.id.textView_express_tracking);
                OrderDetailBean.SubOrdersBean subOrdersEntity = sub_orders.get(m);

                textView1.setText(subOrdersEntity.getId());
                textView2.setVisibility(View.INVISIBLE);

                if (TextUtils.isEmpty(subOrdersEntity.getExpress_company()) || TextUtils.isEmpty(subOrdersEntity.getExpress_no())) {
                    LinearLayout llExpress = (LinearLayout) subOrderView.findViewById(R.id.linearLayout_express_container);
                    llExpress.removeAllViews();
                    TextView textViewHint = new TextView(OrderDetailsActivity.this);
                    textViewHint.setText("暂时没有物流信息");
                    llExpress.setGravity(Gravity.CENTER);
                    llExpress.addView(textViewHint);
                } else {
                    textViewExpress.setText(subOrdersEntity.getExpress_company());
                    textViewExpressCode.setText(subOrdersEntity.getExpress_no());
                    textViewExpressTracking.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO: 2016/12/8 查看物流信息
                            Intent intent = new Intent(OrderDetailsActivity.this, OrderTrackActivity.class);
                            intent.putExtra("rid", orderDetailBean.getRid());
                            intent.putExtra("express_no", orderDetailBean.getExpress_no());
                            intent.putExtra("express_caty", orderDetailBean.getExpress_caty());
                            intent.putExtra("express_company", orderDetailBean.getExpress_company());
                            startActivity(intent);
                        }
                    });
                }

                TextView orderNumberTitle = (TextView) subOrderView.findViewById(R.id.textView_order_code_title);
                orderNumberTitle.setText("子订单号: ");

                final List<OrderDetailBean.SubOrdersBean.ItemsBean> items = subOrdersEntity.getItems();
                setSubOrderGoodsItem(linearLayoutContainerGoods, items);
                mContainerLayout.addView(subOrderView);
            }
        } else {
            View subOrderView = View.inflate(OrderDetailsActivity.this, R.layout.layout_goods_details_order_single, null);
            TextView textView1 = (TextView) subOrderView.findViewById(R.id.textView_order_number);
            TextView textView2 = (TextView) subOrderView.findViewById(R.id.textView_order_status);
            TextView textViewExpress = (TextView) subOrderView.findViewById(R.id.textView_express);
            TextView textViewExpressCode = (TextView) subOrderView.findViewById(R.id.textView_express_code);
            LinearLayout linearLayoutContainerGoods = (LinearLayout) subOrderView.findViewById(R.id.linearLayout_container_subOrder_details);
            TextView textViewExpressTracking = (TextView) subOrderView.findViewById(R.id.textView_express_tracking);

            textView1.setText(orderDetailBean.getRid());
            textView2.setVisibility(View.INVISIBLE);

            if (TextUtils.isEmpty(orderDetailBean.getExpress_company()) || TextUtils.isEmpty(orderDetailBean.getExpress_no())) {
                LinearLayout llExpress = (LinearLayout) subOrderView.findViewById(R.id.linearLayout_express_container);
                llExpress.removeAllViews();
                TextView textViewHint = new TextView(OrderDetailsActivity.this);
                textViewHint.setText("暂时没有物流信息");
                llExpress.setGravity(Gravity.CENTER);
                llExpress.addView(textViewHint);
            } else {
                textViewExpress.setText(orderDetailBean.getExpress_company());
                textViewExpressCode.setText(orderDetailBean.getExpress_no());
                textViewExpressTracking.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2016/12/8 查看物流信息
                        Intent intent = new Intent(OrderDetailsActivity.this, OrderTrackActivity.class);
                        intent.putExtra("rid", orderDetailBean.getRid());
                        intent.putExtra("express_no", orderDetailBean.getExpress_no());
                        intent.putExtra("express_caty", orderDetailBean.getExpress_caty());
                        intent.putExtra("express_company", orderDetailBean.getExpress_company());
                        startActivity(intent);
                    }
                });
            }

            final List<OrderDetailBean.ItemsBean> itemsEntityList = orderDetailBean.getItems();
            setGoodsItem(linearLayoutContainerGoods, itemsEntityList);
            mContainerLayout.addView(subOrderView);
        }
    }

    private void setBottomData() {
        OrderDetailBean.ExpressInfoBean express_info = orderDetailBean.getExpress_info();
        mDeliverMan.setText(String.format("收货人：%s", express_info.getName()));
        mProvince.setText(express_info.getProvince());
        mCity.setText(express_info.getCity());
        mCounty.setText(TextUtils.isEmpty(express_info.getCounty()) ? "" : express_info.getCounty());
        mTown.setText(TextUtils.isEmpty(express_info.getTown()) ? "" : express_info.getTown());
        mDetailsAddress.setText(express_info.getAddress());
        mPhone.setText(express_info.getPhone());
    }

    private void initView() {
        GlobalTitleLayout title = (GlobalTitleLayout) findViewById(R.id.title_order_details);
        title.setContinueTvVisible(false);
        title.setTitle("订单详情");
        mDialog = new WaittingDialog(this);
//        alertDialog = new AlertDialog.Builder(this);
        mLogisticsCompany = (TextView) findViewById(R.id.tv_logistics_company_number_order_details);
        mLogisticsNumber = (TextView) findViewById(R.id.tv_logistics_number_order_details);
        mLogisticsCompanyLayout = (RelativeLayout) findViewById(R.id.layout_logistics_company);
        mLogisticsNumberLayout = (RelativeLayout) findViewById(R.id.layout_logistics_number);
        mContainerLayout = (LinearLayout) findViewById(R.id.linear_item_order_details);
        mDeliverMan = (TextView) findViewById(R.id.tv_deliver_man_order_details);
        mProvince = (TextView) findViewById(R.id.tv_province_order_details);
        mCity = (TextView) findViewById(R.id.tv_city_order_details);
        mCounty = (TextView) findViewById(R.id.tv_county);
        mTown = (TextView) findViewById(R.id.tv_town);
        mDetailsAddress = (TextView) findViewById(R.id.tv_address_order_details);
        mPhone = (TextView) findViewById(R.id.tv_phone_order_details);
        mOrderNum = (TextView) findViewById(R.id.tv_number_order_details);
        mPayway = (TextView) findViewById(R.id.tv_payway_order_details);
        mTotalMoney = (TextView) findViewById(R.id.tv_totalmoney_order_details);
        mFreight = (TextView) findViewById(R.id.tv_freight_order_details);
        mPayMoney = (TextView) findViewById(R.id.tv_paymoney_order_details);
        TextView mCall = (TextView) findViewById(R.id.bt_call_order_details);
        mRightButton = (TextView) findViewById(R.id.bt_right_order_details);
        mLeftButton = (TextView) findViewById(R.id.bt_left_order_details);
        mBottomLayout = (RelativeLayout) findViewById(R.id.layout_two_button_bottom_order_details);
        TextView mTotalMoney2 = (TextView) findViewById(R.id.tv_totalmoney_order_details2);

        mBottomLayout.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_call_order_details:
                new DefaultDialog(OrderDetailsActivity.this, "联系客服：400-879-8751", App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                    @Override
                    public void clickRight() {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:4008798751"));
                        startActivity(intent);
                    }
                });
//                alertDialog.setTitle("联系客服：400-879-8751");
//                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent(Intent.ACTION_DIAL);
//                        intent.setData(Uri.parse("tel:4008798751"));
//                        startActivity(intent);
//                    }
//                });
//                alertDialog.show();
                break;
        }
    }


    /**
     * 加载订单产品条目
     *
     * @param linearLayoutContainerGoods
     * @param itemsEntityList
     */
    private void setGoodsItem(LinearLayout linearLayoutContainerGoods, final List<OrderDetailBean.ItemsBean> itemsEntityList) {
        for (int k = 0; k < itemsEntityList.size(); k++) {
            View subOrderGoodsItemView = View.inflate(OrderDetailsActivity.this, R.layout.layout_goods_details, null);
            ImageView imageViewGoods = (ImageView) subOrderGoodsItemView.findViewById(R.id.imageView_goods);
            TextView textViewGoodsDescription = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_goods_description);
            TextView textViewSpecification = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_specification);
            TextView textViewStatus = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_status);
            TextView textViewPrice = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_price);
            TextView textView1 = (TextView) subOrderGoodsItemView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_button_status);
            GlideUtils.displayImage(itemsEntityList.get(k).getCover_url(), imageViewGoods);

            textViewGoodsDescription.setText(itemsEntityList.get(k).getName());
            if ("null".equals(itemsEntityList.get(k).getSku_name())) {
                textViewSpecification.setVisibility(View.INVISIBLE);
            } else {
                textViewSpecification.setText(itemsEntityList.get(k).getSku_name() + String.format(" * %s", itemsEntityList.get(k).getQuantity()));
            }

            String refund_label = itemsEntityList.get(k).getRefund_label();
            if (TextUtils.isEmpty(refund_label)) {
                textViewStatus.setVisibility(View.GONE);
            } else {
                textViewStatus.setText(refund_label);
                textViewStatus.setVisibility(View.VISIBLE);
            }
            textViewPrice.setText(String.format("¥%s", itemsEntityList.get(k).getSale_price()));

            final int refund_button = itemsEntityList.get(k).getRefund_button();
            switch (refund_button) { //退款行为：0.隐藏；1.退款；2.退货／款
                case 0:
                    textView2.setVisibility(View.INVISIBLE);
//                    textViewStatus.setVisibility(View.GONE);
                    break;
                case 1:
                    textView2.setText("退款");
                    break;
                case 2:
                    textView2.setText("退货");
                    break;
            }

            final int y = k;
            subOrderGoodsItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailsActivity.this, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(itemsEntityList.get(y).getProduct_id()));
                    OrderDetailsActivity.this.startActivity(intent);
                }
            });
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (refund_button == 1) {
                        intent = new Intent(OrderDetailsActivity.this, ChargeBackActivity.class);
                    } else if (refund_button == 2) {
                        intent = new Intent(OrderDetailsActivity.this, SalesReturnActivity.class);
                    }
                    if (intent != null) {
                        intent.putExtra(KEY.R_ID, orderDetailBean.getRid());
                        int sku = itemsEntityList.get(y).getSku();
                        intent.putExtra(KEY.SKU_ID, String.valueOf(sku));
                        startActivity(intent);
                    }
                }
            });
            linearLayoutContainerGoods.addView(subOrderGoodsItemView);
        }
    }

    /**
     * 加载子订单产品条目
     *
     * @param linearLayoutContainerGoods
     * @param items
     */
    private void setSubOrderGoodsItem(LinearLayout linearLayoutContainerGoods, final List<OrderDetailBean.SubOrdersBean.ItemsBean> items) {
        for (int k = 0; k < items.size(); k++) {
            View subOrderGoodsItemView = View.inflate(OrderDetailsActivity.this, R.layout.layout_goods_details, null);
            ImageView imageViewGoods = (ImageView) subOrderGoodsItemView.findViewById(R.id.imageView_goods);
            TextView textViewGoodsDescription = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_goods_description);
            TextView textViewSpecification = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_specification);
            TextView textViewStatus = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_status);
            TextView textViewPrice = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_price);
            TextView textView1 = (TextView) subOrderGoodsItemView.findViewById(R.id.textView1);
            TextView textView2 = (TextView) subOrderGoodsItemView.findViewById(R.id.textView_button_status);
            GlideUtils.displayImage(items.get(k).getCover_url(), imageViewGoods);

            textViewGoodsDescription.setText(items.get(k).getName());
            if ("null".equals(items.get(k).getSku_name())) {
                textViewSpecification.setVisibility(View.INVISIBLE);
            } else {
                textViewSpecification.setText(items.get(k).getSku_name() + String.format(" * %s", items.get(k).getQuantity()));
            }
            textViewStatus.setText(items.get(k).getRefund_label());
            textViewPrice.setText(String.format("¥%s", items.get(k).getSale_price()));
//            textView1.setText(String.format("× %s", items.get(k).getQuantity()));
            final int refund_button = items.get(k).getRefund_button();
            switch (refund_button) { //退款行为：0.隐藏；1.退款；2.退货／款
                case 0:
                    textView2.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    textView2.setText("退款");
                    break;
                case 2:
                    textView2.setText("退货");
                    break;
            }

            final int y = k;
            subOrderGoodsItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(OrderDetailsActivity.this, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(items.get(y).getProduct_id()));
                    OrderDetailsActivity.this.startActivity(intent);
                }
            });
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = null;
                    if (refund_button == 1) {
                        intent = new Intent(OrderDetailsActivity.this, ChargeBackActivity.class);
                    } else if (refund_button == 2) {
                        intent = new Intent(OrderDetailsActivity.this, SalesReturnActivity.class);
                    }
                    if (intent != null) {
                        intent.putExtra(KEY.R_ID, orderDetailBean.getRid());
                        intent.putExtra(KEY.SKU_ID, String.valueOf(items.get(y).getSku()));
                        startActivity(intent);
                    }
                }
            });
            linearLayoutContainerGoods.addView(subOrderGoodsItemView);
        }
    }

    private void initOrderStatus() {
        String rid1 = orderDetailBean.getRid();
        double pay_money = orderDetailBean.getPay_money();
        mOrderNum.setText(rid1);
        mPayMoney.setText("¥" + pay_money);//实付金额

        String payment_method = orderDetailBean.getTrade_site_name();
        if (TextUtils.isEmpty(payment_method)) {
            ((RelativeLayout) mPayway.getParent()).setVisibility(View.GONE);
        } else {
            mPayway.setText(payment_method);
        }

        mTotalMoney.setText("¥" + orderDetailBean.getTotal_money());//商品总额
        mFreight.setText("¥" + orderDetailBean.getFreight());//运费
        if (!orderDetailBean.getExpress_no().isEmpty()) {
            mLogisticsCompanyLayout.setVisibility(View.VISIBLE);
            mLogisticsNumberLayout.setVisibility(View.VISIBLE);
            mLogisticsNumber.setText(orderDetailBean.getExpress_no());
            mLogisticsCompany.setText(orderDetailBean.getExpress_company());
        }

        mLogisticsCompanyLayout.setVisibility(View.GONE);
        mLogisticsNumberLayout.setVisibility(View.GONE);

        final double paymoney = orderDetailBean.getPay_money();
        final int status = orderDetailBean.getStatus();
        final String rid = mRid;//订单唯一编号
        String deleteOrder = "删除订单", payNow = "立即支付", applyForRefund = "申请退款", confirmReceived = "确认收货", publishEvaluate = "发表评价", cancelOrder = "取消订单";
        switch (status) {
            case 0://已取消状态
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(deleteOrder);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();
                    }
                });
                break;
            case 1://待付款
                mLeftButton.setText(cancelOrder);
                mLeftButton.setVisibility(View.VISIBLE);
                mRightButton.setText(payNow);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent paynowIntent = new Intent(OrderDetailsActivity.this, PayWayActivity.class);
                        paynowIntent.putExtra("paymoney", paymoney);
                        paynowIntent.putExtra("orderId", rid);
                        startActivity(paynowIntent);
                    }
                });

                mLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_cancel_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.cancelOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {
                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要取消订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.cancelOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                break;
            case 10://待发货
                // 取消底部申请退款按钮
                mLeftButton.setText(applyForRefund);
                mLeftButton.setVisibility(View.INVISIBLE);

                mRightButton.setText("提醒发货");
                mRightButton.setVisibility(View.VISIBLE);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mDialog.isShowing()) {
                            mDialog.show();
                        }
                        ClientDiscoverAPI.tixingFahuo(rid, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> responseInfo) {
                                mDialog.dismiss();
                                NetBean netBean = new NetBean();
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<NetBean>() {
                                    }.getType();
                                    netBean = gson.fromJson(responseInfo.result, type);
                                } catch (JsonSyntaxException e) {
                                    Log.e("<<<提醒发货", "数据解析异常");
                                }
                                if (netBean.isSuccess()) {
                                    ToastUtils.showSuccess("提醒发货成功!");
                                } else {
                                    ToastUtils.showInfo(netBean.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(HttpException error, String msg) {
                                mDialog.dismiss();
                                ToastUtils.showError("网络错误");
                            }
                        });
                    }
                });
                break;
            case 13://已退款
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(deleteOrder);
                mRightButton.setVisibility(View.INVISIBLE);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                break;
            case 15://待收货
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(confirmReceived);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = App.getString(R.string.hint_dialog_confirm_receipt_title);
                        String[] textButtons = App.getStringArray(R.array.text_dialog_button2);
                        new DefaultDialog(OrderDetailsActivity.this, title, textButtons, new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.confirmReceiveNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您要确认收货吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.confirmReceiveNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                break;
            case 16://待评价
                mLeftButton.setText(deleteOrder);
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(publishEvaluate);//发表评价
                mLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(OrderDetailsActivity.this, PublishEvaluateActivity.class);
                        in.putExtra("rid", rid);
                        OrderDetailsActivity.this.startActivity(in);
                    }
                });
                break;
            case 20://已完成状态
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(deleteOrder);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                break;
            case -1://已过期
                mLeftButton.setVisibility(View.INVISIBLE);
                mRightButton.setText(deleteOrder);
                mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(OrderDetailsActivity.this, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button2), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(ResponseInfo<String> responseInfo) {
                                        toShopOrderListActivity();
                                    }

                                    @Override
                                    public void onFailure(HttpException e, String s) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        toShopOrderListActivity();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();

                    }
                });
                break;
            case 12://从退货的查看详情点进来，底部那俩按钮都要消失
                mBottomLayout.setVisibility(View.GONE);
                break;
        }
    }
}

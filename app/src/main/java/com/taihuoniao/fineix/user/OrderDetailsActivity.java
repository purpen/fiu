package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.ApplyForRefundActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.PayWayActivity;
import com.taihuoniao.fineix.user.bean.ShoppingD;
import com.taihuoniao.fineix.user.bean.ShoppingDetailBean;
import com.taihuoniao.fineix.user.bean.ShoppingDetailsBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.List;

public class OrderDetailsActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "OrderDetailsActivity";

    private GlobalTitleLayout title;
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
    private TextView mCall;//联系客服
    private TextView mRightButton;//最底部右侧按钮
    private TextView mLeftButton;//最底部左侧按钮
    private RelativeLayout mBottomLayout;//放最底部那俩按钮的布局
    private LinearLayout mContainerLayout;//动态容纳商品item
    private View mItemView;//动态商品的一条

    private String mRid;
    private String mOptFragmentFlag;//跳回碎片列表时，用该值选中跳来之前的那个碎片
//    private List<OrderDetails> mDetailsList = new ArrayList<>();
    private RelativeLayout mLogisticsCompanyLayout;//物流公司
    private RelativeLayout mLogisticsNumberLayout;//物流单号
    private TextView mLogisticsNumber, mLogisticsCompany;

    private BitmapUtils mBitmapUtils;
    private WaittingDialog mDialog;
    private AlertDialog.Builder alertDialog;
    private TextView mCounty;
    private TextView mTown;

    private ShoppingDetailBean shoppingdDetailBean;
    private List<ShoppingDetailBean.ItemsEntity> mListProducts;
//    private EvaluateAdapter2 mAdapter;

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
//        mListProducts = new ArrayList<>();
//        mAdapter = new EvaluateAdapter2(mListProducts, this);
//        listViewForScrollView.setAdapter(mAdapter);

        if(!mDialog.isShowing()){
            mDialog.show();
        }
        mRid = getIntent().getStringExtra("rid");
        mOptFragmentFlag = getIntent().getStringExtra("optFragmentFlag");
//        DataPaser.orderPayDetailsParser(mRid, mHander);
        ClientDiscoverAPI.OrderPayNet(mRid, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;

                try {
                    HttpResponse<ShoppingD> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ShoppingD>>() { });
                    if (response.isError()) {
                        LogUtil.e(TAG, "---------> responseInfo: " + responseInfo.reasonPhrase);
                        return;
                    }
                    ShoppingD shoppingD = response.getData();
                    String rid1 = shoppingD.getRid();
                    String pay_money = shoppingD.getPay_money();
                    mOrderNum.setText(rid1);
                    mPayMoney.setText("¥" + pay_money);//实付金额

                    String payment_method = shoppingD.getPayment_method();
                    mPayway.setText(("a".equals(payment_method) ? "在线支付" : ("b".equals(payment_method) ? "货到付款" : "c".equals(payment_method) ? "其他" : "")));

                    mTotalMoney.setText("¥" + shoppingD.getTotal_money());//商品总额
                    mFreight.setText("¥" + shoppingD.getFreight());//运费
                    if (!shoppingD.getExpress_no().isEmpty()) {
                        mLogisticsCompanyLayout.setVisibility(View.VISIBLE);
                        mLogisticsNumberLayout.setVisibility(View.VISIBLE);
                        mLogisticsNumber.setText(shoppingD.getExpress_no());
                        mLogisticsCompany.setText(shoppingD.getExpress_company());
                    } else {
                        mLogisticsCompanyLayout.setVisibility(View.GONE);
                        mLogisticsNumberLayout.setVisibility(View.GONE);
                    }
                    final String paymoney = shoppingD.getPay_money();
                    final int status = shoppingD.getStatus();
                    final String rid = mRid;//订单唯一编号
                    String deleteOrder = "删除订单", payNow = "立即支付", applyForRefund = "申请退款",
                            confirmReceived = "确认收货", publishEvaluate = "发表评价", cancelOrder = "取消订单";


                    if (!"1".equals(response.getData().getExist_sub_order())) {
                        HttpResponse<ShoppingDetailBean> response2 = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ShoppingDetailBean>>() { });
                        shoppingdDetailBean = response2.getData();
                    }




//
//                final String paymoney = mDetailsList.get(i).getPay_money();
//                final int status = Integer.parseInt(mDetailsList.get(i).getStatus());
//
////                Log.e(">>>", ">>>OOO>>>" + responseInfo.result);
//                List<OrderDetails> ordersList = new ArrayList<>();
//                try {
//                    JSONObject obj = new JSONObject(responseInfo.result);
//                    JSONObject orderObj = obj.getJSONObject("data");
//                    OrderDetails details = new OrderDetails();
//                    details.setRid(orderObj.optString("rid"));
//                    details.setExpress_company(orderObj.optString("express_company"));
//                    details.setExpress_no(orderObj.optString("express_no"));
//                    details.setCreated_at(orderObj.optString("created_at"));
//                    details.setFreight(orderObj.optString("freight"));
//                    details.setItems_count(orderObj.optString("items_count"));
//                    details.setPay_money(orderObj.optString("pay_money"));
//                    details.setPayment_method(orderObj.optString("payment_method"));
//                    details.setTotal_money(orderObj.optString("total_money"));
//                    details.setStatus(orderObj.optString("status"));
//                    JSONObject arr = orderObj.getJSONObject("express_info");
//                    List<OrderDetailsAddress> addressList = new ArrayList<>();
//                    OrderDetailsAddress address = new OrderDetailsAddress();
//                    address.setAddress(arr.optString("address"));
//                    address.setName(arr.optString("name"));
//                    address.setCity(arr.optString("city"));
//                    address.setPhone(arr.optString("phone"));
//                    address.setProvince(arr.optString("province"));
//                    address.setCounty(arr.optString("county"));
//                    address.setTown(arr.optString("town"));
//                    addressList.add(address);
//
//                    details.setAddresses(addressList);
//                    JSONArray productsArrays = orderObj.getJSONArray("items");
//                    List<OrderDetailsProducts> productsList = new ArrayList<>();
//                    for (int j = 0; j < productsArrays.length(); j++) {
//                        JSONObject productsArr = productsArrays.getJSONObject(j);
//                        OrderDetailsProducts products = new OrderDetailsProducts();
//                        products.setName(productsArr.optString("name"));
//                        products.setCover_url(productsArr.optString("cover_url"));
//                        products.setPrice(productsArr.optString("price"));
//                        products.setProduct_id(productsArr.optString("product_id"));
//                        products.setQuantity(productsArr.optString("quantity"));
//                        products.setSale_price(productsArr.optDouble("sale_price"));
//                        products.setSku(productsArr.optString("sku"));
//                        products.setSku_name(productsArr.optString("sku_name"));
//                        productsList.add(products);
//                    }
//                    details.setProducts(productsList);
//                    ordersList.add(details);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                mDetailsList.clear();
//                mDetailsList.addAll(ordersList);
//                for (int i = 0; i < mDetailsList.size(); i++) {
//                    mOrderNum.setText(mDetailsList.get(i).getRid());
//                    mPayMoney.setText("¥" + mDetailsList.get(i).getPay_money());//实付金额
//                    if ("a".equals(mDetailsList.get(i).getPayment_method())) {
//                        mPayway.setText("在线支付");
//                    } else if ("b".equals(mDetailsList.get(i).getPayment_method())) {
//                        mPayway.setText("货到付款");
//                    } else {
//                        mPayway.setText("其他");
//                    }
//                    mTotalMoney.setText("¥" + mDetailsList.get(i).getTotal_money());//商品总额
//                    mFreight.setText("¥" + mDetailsList.get(i).getFreight());//运费
//                    if (!mDetailsList.get(i).getExpress_no().isEmpty()) {
//                        mLogisticsCompanyLayout.setVisibility(View.VISIBLE);
//                        mLogisticsNumberLayout.setVisibility(View.VISIBLE);
//                        mLogisticsNumber.setText(mDetailsList.get(i).getExpress_no());
//                        mLogisticsCompany.setText(mDetailsList.get(i).getExpress_company());
//                    } else {
//                        mLogisticsCompanyLayout.setVisibility(View.GONE);
//                        mLogisticsNumberLayout.setVisibility(View.GONE);
//                    }
//
//                    final String paymoney = mDetailsList.get(i).getPay_money();
//                    final int status = Integer.parseInt(mDetailsList.get(i).getStatus());
//                    final String rid = mRid;//订单唯一编号
//                    String deleteOrder = "删除订单", payNow = "立即支付", applyForRefund = "申请退款",
//                            confirmReceived = "确认收货", publishEvaluate = "发表评价", cancelOrder = "取消订单";
                    switch (status) {
                        case 0://已取消状态
                            mLeftButton.setVisibility(View.INVISIBLE);
                            mRightButton.setText(deleteOrder);
                            mRightButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.setTitle("您确定要删除订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();
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
                                    alertDialog.setTitle("您确定要取消订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

                                }
                            });
                            break;
                        case 10://待发货
                            mLeftButton.setText("提醒发货");
                            mLeftButton.setVisibility(View.VISIBLE);
                            mRightButton.setText(applyForRefund);
                            mLeftButton.setOnClickListener(new View.OnClickListener() {
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
//                                                            new SVProgressHUD(OrderDetailsActivity.this).showSuccessWithStatus("提醒发货成功!");
//                                                            Toast.makeText(OrderDetailsActivity.this, "提醒发货成功！", Toast.LENGTH_SHORT).show();
                                            } else {
                                                ToastUtils.showInfo(netBean.getMessage());
//                                                            new SVProgressHUD(OrderDetailsActivity.this).showInfoWithStatus(netBean.getMessage());
//                                                            Toast.makeText(OrderDetailsActivity.this, netBean.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(HttpException error, String msg) {
                                            mDialog.dismiss();
                                            ToastUtils.showError("网络错误");
//                                                        mDialog.showErrorWithStatus("网络错误");
//                                                        Toast.makeText(OrderDetailsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                            mRightButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent refundIntent = new Intent(OrderDetailsActivity.this, ApplyForRefundActivity.class);
                                    refundIntent.putExtra("refundMoney", paymoney);
                                    refundIntent.putExtra("rid", rid);
                                    OrderDetailsActivity.this.startActivity(refundIntent);
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
                                    alertDialog.setTitle("您确定要删除订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

                                }
                            });
                            break;
                        case 15://待收货
                            mLeftButton.setVisibility(View.INVISIBLE);
                            mRightButton.setText(confirmReceived);
                            mRightButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.setTitle("您要确认收货吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

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
                                    alertDialog.setTitle("您确定要删除订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

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
                                    alertDialog.setTitle("您确定要删除订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

                                }
                            });
                            break;
                        case -1://已过期
                            mLeftButton.setVisibility(View.INVISIBLE);
                            mRightButton.setText(deleteOrder);
                            mRightButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.setTitle("您确定要删除订单吗？");
                                    alertDialog.setNegativeButton("不了", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
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
                                    alertDialog.show();

                                }
                            });
                            break;
                        case 12://从退货的查看详情点进来，底部那俩按钮都要消失
                            mBottomLayout.setVisibility(View.GONE);
                            break;
                    }

                    ShoppingDetailBean.ExpressInfoEntity express_info = shoppingdDetailBean.getExpress_info();
                    mDeliverMan.setText(String.format("收货人：%s", express_info.getName()));
                    mProvince.setText(express_info.getProvince());
                    mCity.setText(express_info.getCity());
                    mCounty.setText(TextUtils.isEmpty(express_info.getCounty()) ? "" : express_info.getCounty());
                    mTown.setText(TextUtils.isEmpty(express_info.getTown()) ? "" : express_info.getTown());
                    mDetailsAddress.setText(express_info.getAddress());
                    mPhone.setText(express_info.getPhone());

                    if (mContainerLayout != null) {
                        mContainerLayout.removeAllViews();
                    }

                    final List<ShoppingDetailBean.ItemsEntity> itemsEntityList = shoppingdDetailBean.getItems();
                    for (int k = 0; k < itemsEntityList.size(); k++) {
                        mItemView = View.inflate(OrderDetailsActivity.this, R.layout.item_order_content, null);
                        TextView mTitleItem = (TextView) mItemView.findViewById(R.id.tv_title_order_inner);
                        TextView mColorItem = (TextView) mItemView.findViewById(R.id.tv_color_order_inner);
                        TextView mMoneyItem = (TextView) mItemView.findViewById(R.id.tv_money_order_inner);
                        TextView mCountItem = (TextView) mItemView.findViewById(R.id.tv_count_order_inner);
                        ImageView mImageItem = (ImageView) mItemView.findViewById(R.id.image_order_inner);
                        mBitmapUtils.display(mImageItem, itemsEntityList.get(k).getCover_url());
                        mTitleItem.setText(itemsEntityList.get(k).getName());
                        if ("null".equals(itemsEntityList.get(k).getSku_name())) {
                            mColorItem.setVisibility(View.INVISIBLE);
                        } else {
                            mColorItem.setText(itemsEntityList.get(k).getSku_name());
                        }
                        mColorItem.setText(itemsEntityList.get(k).getSku_name());
                        mCountItem.setText(String.format("× %s", itemsEntityList.get(k).getQuantity()));
                        mMoneyItem.setText(String.format("¥%s", itemsEntityList.get(k).getSale_price()));
                        final int y = k;
                        mItemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(OrderDetailsActivity.this, BuyGoodsDetailsActivity.class);
                                intent.putExtra("id", itemsEntityList.get(y).getProduct_id());
                                OrderDetailsActivity.this.startActivity(intent);
                            }
                        });
                        mContainerLayout.addView(mItemView);
                    }


//
//                List<OrderDetailsAddress> addresses = mDetailsList.get(i).getAddresses();
//                    for (int j = 0; j < addresses.size(); j++) {
//                        mDeliverMan.setText(String.format("收货人：%s", mDetailsList.get(i).getAddresses().get(j).getName()));
//                        mProvince.setText(addresses.get(j).getProvince());
//                        mCity.setText(addresses.get(j).getCity());
//                        if (TextUtils.isEmpty(addresses.get(j).getCounty())){
//                            mCounty.setText("");
//                        }else {
//                            mCounty.setText(addresses.get(j).getCounty());
//                        }
//
//                        if (TextUtils.isEmpty(addresses.get(j).getTown())){
//                            mTown.setText("");
//                        }else {
//                            mTown.setText(addresses.get(j).getTown());
//                        }
//
//                        mDetailsAddress.setText(addresses.get(j).getAddress());
//                        mPhone.setText(addresses.get(j).getPhone());
//                    }
//                    if (mContainerLayout != null) {
//                        mContainerLayout.removeAllViews();
//                    }
//                    for (int k = 0; k < mDetailsList.get(i).getProducts().size(); k++) {
//                        mItemView = View.inflate(OrderDetailsActivity.this,R.layout.item_order_content,null);
////                        mItemView = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.item_order_content, null);
//                        TextView mTitleItem = (TextView) mItemView.findViewById(R.id.tv_title_order_inner);
//                        TextView mColorItem = (TextView) mItemView.findViewById(R.id.tv_color_order_inner);
//                        TextView mMoneyItem = (TextView) mItemView.findViewById(R.id.tv_money_order_inner);
//                        TextView mCountItem = (TextView) mItemView.findViewById(R.id.tv_count_order_inner);
//                        ImageView mImageItem = (ImageView) mItemView.findViewById(R.id.image_order_inner);
//                        mBitmapUtils.display(mImageItem, mDetailsList.get(i).getProducts().get(k).getCover_url());
//                        mTitleItem.setText(mDetailsList.get(i).getProducts().get(k).getName());
//                        if ("null".equals(mDetailsList.get(i).getProducts().get(k).getSku_name())) {
//                            mColorItem.setVisibility(View.INVISIBLE);
//                        } else {
//                            mColorItem.setText(mDetailsList.get(i).getProducts().get(k).getSku_name());
//                        }
//                        mColorItem.setText(mDetailsList.get(i).getProducts().get(k).getSku_name());
//                        mCountItem.setText(String.format("× %s", mDetailsList.get(i).getProducts().get(k).getQuantity()));
//                        mMoneyItem.setText(String.format("¥%s", mDetailsList.get(i).getProducts().get(k).getSale_price()));
//                        final int x = i;
//                        final int y = k;
//                        mItemView.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(OrderDetailsActivity.this, BuyGoodsDetailsActivity.class);
//                                intent.putExtra("id", mDetailsList.get(x).getProducts().get(y).getProduct_id());
//                                OrderDetailsActivity.this.startActivity(intent);
//                            }
//                        });
//                        mContainerLayout.addView(mItemView);
//                    }
//                }
                    if (mDialog != null) {
                        if (mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "-----------> e: " + e);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                mDialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void initView() {
        title = (GlobalTitleLayout) findViewById(R.id.title_order_details);
        title.setContinueTvVisible(false);
        title.setTitle("订单详情");
        mDialog = new WaittingDialog(this);
        alertDialog = new AlertDialog.Builder(this);
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
        mCall = (TextView) findViewById(R.id.bt_call_order_details);
        mRightButton = (TextView) findViewById(R.id.bt_right_order_details);
        mLeftButton = (TextView) findViewById(R.id.bt_left_order_details);
        mBottomLayout = (RelativeLayout) findViewById(R.id.layout_two_button_bottom_order_details);

        mBottomLayout.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mRightButton.setOnClickListener(this);
        mLeftButton.setOnClickListener(this);

        String diskCachePath = StorageUtils.getCacheDirectory(MainApplication.getContext()).getAbsolutePath();
        mBitmapUtils = new BitmapUtils(this, diskCachePath)
                .configMemoryCacheEnabled(true)
                .configDefaultCacheExpiry(1024 * 1024 * 4)
                .configDefaultBitmapMaxSize(300, 300)
                .configDefaultBitmapConfig(Bitmap.Config.ALPHA_8)
//                .configDefaultLoadingImage(R.mipmap.default_shopcart)
//                .configDefaultLoadFailedImage(R.mipmap.default_shopcart)
                .configThreadPoolSize(5)
                .configDefaultImageLoadAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_call_order_details:
                alertDialog.setTitle("联系客服：400-879-8751");
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:4008798751"));
                        startActivity(intent);
                    }
                });
                alertDialog.show();
                break;
        }
    }
}

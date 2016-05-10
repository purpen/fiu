package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.beans.OrderDetails;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.product.ApplyForRefundActivity;
import com.taihuoniao.fineix.product.MyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.PayWayActivity;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OrderDetailsActivity extends Activity implements View.OnClickListener {
    private MyGlobalTitleLayout title;
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
    private String mProductId;//跳去发表评价界面时所需
    private String mSkuId;//跳去发表评价界面时所需
    private List<OrderDetails> mDetailsList = new ArrayList<>();
    private RelativeLayout mLogisticsCompanyLayout;//物流公司
    private RelativeLayout mLogisticsNumberLayout;//物流单号
    private TextView mLogisticsNumber, mLogisticsCompany;

    private BitmapUtils mBitmapUtils;
    private WaittingDialog mDialog;
    private AlertDialog.Builder alertDialog;

    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DataConstants.PARSER_ORDER_DETAILS:
                    if (msg.obj != null) {
                        if (msg.obj instanceof List) {
                            mDetailsList.clear();
                            mDetailsList.addAll((Collection<? extends OrderDetails>) msg.obj);
                            for (int i = 0; i < mDetailsList.size(); i++) {
                                mOrderNum.setText(mDetailsList.get(i).getRid());
                                mPayMoney.setText("¥" + mDetailsList.get(i).getPay_money());//实付金额
                                if ("a".equals(mDetailsList.get(i).getPayment_method())) {
                                    mPayway.setText("在线支付");
                                } else if ("b".equals(mDetailsList.get(i).getPayment_method())) {
                                    mPayway.setText("货到付款");
                                } else {
                                    mPayway.setText("其他");
                                }
                                mTotalMoney.setText("¥" + mDetailsList.get(i).getTotal_money());//商品总额
                                mFreight.setText("¥" + mDetailsList.get(i).getFreight());//运费
                                if (!mDetailsList.get(i).getExpress_no().isEmpty()) {
                                    mLogisticsCompanyLayout.setVisibility(View.VISIBLE);
                                    mLogisticsNumberLayout.setVisibility(View.VISIBLE);
                                    mLogisticsNumber.setText(mDetailsList.get(i).getExpress_no());
                                    mLogisticsCompany.setText(mDetailsList.get(i).getExpress_company());
                                } else {
                                    mLogisticsCompanyLayout.setVisibility(View.GONE);
                                    mLogisticsNumberLayout.setVisibility(View.GONE);
                                }

                                final String paymoney = mDetailsList.get(i).getPay_money();
                                final int status = Integer.parseInt(mDetailsList.get(i).getStatus());
                                final String rid = mRid;//订单唯一编号
                                String deleteOrder = "删除订单", payNow = "立即支付", applyForRefund = "申请退款",
                                        confirmReceived = "确认收货", publishEvaluate = "发表评价", cancelOrder = "取消订单";
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
                                        mLeftButton.setVisibility(View.INVISIBLE);
                                        mRightButton.setText(applyForRefund);
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
                                                in.putExtra("productId", mProductId);
                                                in.putExtra("skuId", mSkuId);
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

                                for (int j = 0; j < mDetailsList.get(i).getAddresses().size(); j++) {
                                    mDeliverMan.setText(String.format("收货人：%s", mDetailsList.get(i).getAddresses().get(j).getName()));
                                    mProvince.setText(mDetailsList.get(i).getAddresses().get(j).getProvince());
                                    mCity.setText(mDetailsList.get(i).getAddresses().get(j).getCity());
                                    mDetailsAddress.setText(mDetailsList.get(i).getAddresses().get(j).getAddress());
                                    mPhone.setText(mDetailsList.get(i).getAddresses().get(j).getPhone());
                                }
                                if (mContainerLayout != null) {
                                    mContainerLayout.removeAllViews();
                                }
                                for (int k = 0; k < mDetailsList.get(i).getProducts().size(); k++) {
                                    mItemView = LayoutInflater.from(OrderDetailsActivity.this).inflate(R.layout.item_order_content, null);
                                    TextView mTitleItem = (TextView) mItemView.findViewById(R.id.tv_title_order_inner);
                                    TextView mColorItem = (TextView) mItemView.findViewById(R.id.tv_color_order_inner);
                                    TextView mMoneyItem = (TextView) mItemView.findViewById(R.id.tv_money_order_inner);
                                    TextView mCountItem = (TextView) mItemView.findViewById(R.id.tv_count_order_inner);
                                    ImageView mImageItem = (ImageView) mItemView.findViewById(R.id.image_order_inner);
                                    mBitmapUtils.display(mImageItem, mDetailsList.get(i).getProducts().get(k).getCover_url());
                                    mTitleItem.setText(mDetailsList.get(i).getProducts().get(k).getName());
                                    if ("null".equals(mDetailsList.get(i).getProducts().get(k).getSku_name())) {
                                        mColorItem.setVisibility(View.INVISIBLE);
                                    } else {
                                        mColorItem.setText(mDetailsList.get(i).getProducts().get(k).getSku_name());
                                    }
                                    mColorItem.setText(mDetailsList.get(i).getProducts().get(k).getSku_name());
                                    mCountItem.setText(String.format("× %s", mDetailsList.get(i).getProducts().get(k).getQuantity()));
                                    mMoneyItem.setText(String.format("¥%s", mDetailsList.get(i).getProducts().get(k).getSale_price()));
                                    final int x = i;
                                    final int y = k;
                                    mItemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(OrderDetailsActivity.this, MyGoodsDetailsActivity.class);
                                            intent.putExtra("id", mDetailsList.get(x).getProducts().get(y).getProduct_id());
                                            OrderDetailsActivity.this.startActivity(intent);
                                        }
                                    });
                                    mContainerLayout.addView(mItemView);
                                }
                            }
                            if (mDialog != null) {
                                if (mDialog.isShowing()) {
                                    mDialog.dismiss();
                                }
                            }
                        }
                    }
                    break;
            }
        }
    };

    private void toShopOrderListActivity() {
        Intent in = new Intent(OrderDetailsActivity.this, ShopOrderListActivity.class);
        in.putExtra("optFragmentFlag", mOptFragmentFlag);
        OrderDetailsActivity.this.startActivity(in);
        OrderDetailsActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_order_details);
        ActivityUtil.getInstance().addActivity(this);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mDialog.show();
        mProductId = getIntent().getStringExtra("productId");
        mSkuId = getIntent().getStringExtra("skuId");
        mRid = getIntent().getStringExtra("rid");
        mOptFragmentFlag = getIntent().getStringExtra("optFragmentFlag");
        DataPaser.orderPayDetailsParser(mRid, mHander);
    }

    private void initView() {
        title = (MyGlobalTitleLayout) findViewById(R.id.title_order_details);
        title.setTitle("订单详情");
        title.setBackgroundResource(R.color.white);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackImg(R.mipmap.back_black);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
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

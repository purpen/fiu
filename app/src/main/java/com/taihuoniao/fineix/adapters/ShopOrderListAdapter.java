package com.taihuoniao.fineix.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.NetBean;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.beans.OrderItem;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.PayWayActivity;
import com.taihuoniao.fineix.user.OrderDetailsActivity;
import com.taihuoniao.fineix.user.PublishEvaluateActivity;
import com.taihuoniao.fineix.user.ShopOrderListActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListenerConfirmBack;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 *  optFragmentFlag 跳转订单详情时传过去，以便在订单详情页点击“取消订单”或“确认收货”这类立即要跳回订单列表的按钮之
 *  后，能根据这个值记住跳去之前选中的是哪个碎片，从而能在跳回之后，选中之前的碎片
 *
 * Created by android on 2016/2/22.
 */
public class ShopOrderListAdapter extends THNBaseAdapter {

    private LayoutInflater inflater = null;
    private Context context;
//    private AlertDialog.Builder alertDialog;
    private WaittingDialog mdialog;

//    private BitmapUtils bitmapUtils_listview = null;
    private List<OrderEntity> list;
    private String optFragmentFlag;

    public ShopOrderListAdapter(List<OrderEntity> list, Context context, String optFragmentFlag) {
        super(list, context);
        this.list = list;
        this.optFragmentFlag = optFragmentFlag;
        this.context = context;
//        alertDialog = new AlertDialog.Builder(context);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        String diskCachePath = StorageUtils.getCacheDirectory(MainApplication.getContext()).getAbsolutePath();
//        bitmapUtils_listview = new BitmapUtils(context, diskCachePath)
//                .configMemoryCacheEnabled(true)
//                .configDefaultCacheExpiry(1024 * 1024 * 4)
//                .configDefaultBitmapMaxSize(300, 300)
//                .configDefaultBitmapConfig(Bitmap.Config.ALPHA_8)
//                .configThreadPoolSize(5)
//                .configDefaultImageLoadAnimation(
//                        AnimationUtils.loadAnimation(context, R.anim.fade_in));
        mdialog = new WaittingDialog(context);
    }

    @Override
    public View getItemView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_order, parent, false);
            mHolder.mItem = (LinearLayout) convertView.findViewById(R.id.linear_item_order);
            mHolder.mDate = (TextView) convertView.findViewById(R.id.tv_date_order);
            mHolder.mFreight = (TextView) convertView.findViewById(R.id.tv_freight_order);
            mHolder.mLeftButton = (TextView) convertView.findViewById(R.id.bt_left_order);
            mHolder.mRightButton = (TextView) convertView.findViewById(R.id.bt_right_order);
            mHolder.mProductCounts = (TextView) convertView.findViewById(R.id.tv_count_order);
            mHolder.mPayMoney = (TextView) convertView.findViewById(R.id.tv_paymoney_order);
            mHolder.mTotalMoney = (TextView) convertView.findViewById(R.id.tv_totalmoney_order);
            mHolder.mTradeStatus = (TextView) convertView.findViewById(R.id.tv_status_order);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.mTradeStatus.setText(list.get(position).getStatus_label());
        mHolder.mProductCounts.setText("共" + list.get(position).getItems_count() + "件好货");
        
//        double p  = Double.parseDouble(list.get(position).getTotal_money())-Double.parseDouble(list.get(position).getPay_money());
        mHolder.mTotalMoney.setText("合计：¥" + list.get(position).getTotal_money()+" 优惠：¥"+ list.get(position).getDiscount_money());
        mHolder.mPayMoney.setText("实付：¥" + list.get(position).getPay_money());
        mHolder.mFreight.setText("");
        mHolder.mDate.setText(list.get(position).getCreated_at());

        final int status = Integer.parseInt(list.get(position).getStatus());
        final String rid = list.get(position).getRid();//订单唯一编号
        final List<OrderItem> orderItem = list.get(position).getOrderItem();
        final String deleteOrder = "删除订单", payNow = "立即支付", applyForRefund = "申请退款", seeDtails = "查看详情", tixingFahuo = "提醒发货",
                confirmReceived = "确认收货", publishEvaluate = "发表评价", cancelOrder = "取消订单";
        switch (status) {
            case 0://已取消状态
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(deleteOrder);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getdeleteOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_DELETE_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        mdialog.dismiss();
                                        removeItem(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        mdialog.dismiss();
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
//                                if (!mdialog.isShowing()) {
//                                    mdialog.show();
//                                }
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        mdialog.dismiss();
//                                        removeItem(position);
//                                        notifyDataSetChanged();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//                                        mdialog.dismiss();
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();
                    }
                });
                break;
            case 1://待付款
                mHolder.mLeftButton.setText(cancelOrder);
                mHolder.mLeftButton.setVisibility(View.VISIBLE);
                mHolder.mRightButton.setText(payNow);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent paynowIntent = new Intent(context, PayWayActivity.class);
                        paynowIntent.putExtra("paymoney", list.get(position).getPay_money());
                        paynowIntent.putExtra("orderId", rid);
                        context.startActivity(paynowIntent);
                    }
                });
                mHolder.mLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_cancel_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getcancelOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_CANCEL_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.cancelOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        mdialog.dismiss();
                                        removeItem(position);
                                        //TODO 待付款
                                        ((ShopOrderListActivity) context).changeNum(1);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {
                                        mdialog.dismiss();
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
//                                if (!mdialog.isShowing()) {
//                                    mdialog.show();
//                                }
//                                ClientDiscoverAPI.cancelOrderNet(rid, new RequestCallBack<String>() {
//                                    @Override
//                                    public void onSuccess(ResponseInfo<String> responseInfo) {
//                                        mdialog.dismiss();
//                                        removeItem(position);
//                                        //TODO 待付款
//                                        ((ShopOrderListActivity) context).changeNum(1);
//                                        notifyDataSetChanged();
//                                    }
//
//                                    @Override
//                                    public void onFailure(HttpException e, String s) {
//                                        mdialog.dismiss();
//                                    }
//                                });
//                            }
//                        });
//                        alertDialog.show();
                    }
                });
                break;
            case 10://待发货
                mHolder.mLeftButton.setVisibility(View.GONE);
                mHolder.mRightButton.setVisibility(View.VISIBLE);
                mHolder.mRightButton.setText(tixingFahuo);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mdialog.isShowing()){
                        mdialog.show();}
                        HashMap<String, String> params = ClientDiscoverAPI.gettixingFahuoRequestParams(rid);
                        HttpRequest.post(params, URL.SHOPPING_ALERT_SEND_GOODS, new GlobalDataCallBack(){
//                        ClientDiscoverAPI.tixingFahuo(rid, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(String json) {
                                mdialog.dismiss();
                                NetBean netBean = new NetBean();
                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<NetBean>() {
                                    }.getType();
                                    netBean = gson.fromJson(json, type);
                                } catch (JsonSyntaxException e) {
//                                    Log.e("<<<提醒发货", "数据解析异常");
                                }
                                if (netBean.isSuccess()) {
                                    ToastUtils.showSuccess("提醒发货成功!");
                                } else {
                                    ToastUtils.showInfo(netBean.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                mdialog.dismiss();
                                ToastUtils.showError("网络错误");
                            }
                        });
                    }
                });
                break;
            case 13://已退款
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(deleteOrder);
                mHolder.mRightButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getdeleteOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_DELETE_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        removeItem(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {

                                    }
                                });
                            }
                        });
//                        alertDialog.setTitle("您确定要删除订单吗？");
//                        alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
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
//                                        removeItem(position);
//                                        notifyDataSetChanged();
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
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(confirmReceived);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_confirm_receipt_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getconfirmReceiveNetRequestParams(rid);
                                HttpRequest.post(params,  URL.SHOPPING_TAKE_DELIVERY, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.confirmReceiveNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        removeItem(position);
                                        //TODO 待收货
                                        ((ShopOrderListActivity) context).changeNum(3);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {

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
//                                        removeItem(position);
//                                        //TODO 待收货
//                                        ((ShopOrderListActivity) context).changeNum(3);
//                                        notifyDataSetChanged();
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
                mHolder.mLeftButton.setText(deleteOrder);
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(publishEvaluate);//发表评价
                mHolder.mLeftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getdeleteOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_DELETE_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        removeItem(position);
                                        ((ShopOrderListActivity) context).changeNum(4);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {
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
//                                        removeItem(position);
//                                        ((ShopOrderListActivity) context).changeNum(4);
//                                        notifyDataSetChanged();
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
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(context, PublishEvaluateActivity.class);
                        for (int i = 0; i < list.get(position).getOrderItem().size(); i++) {
                            in.putExtra("productId", list.get(position).getOrderItem().get(i).getProduct_id());
                            in.putExtra("skuId", list.get(position).getOrderItem().get(i).getSku());
                            in.putExtra("rid", rid);
                        }
                        context.startActivity(in);
                    }
                });
                break;
            case 20://已完成状态
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(deleteOrder);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack() {
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getdeleteOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_DELETE_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        removeItem(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {

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
//                                        removeItem(position);
//                                        notifyDataSetChanged();
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
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(deleteOrder);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DefaultDialog(context, App.getString(R.string.hint_dialog_delete_order_title), App.getStringArray(R.array.text_dialog_button), new IDialogListenerConfirmBack(){
                            @Override
                            public void clickRight() {
                                HashMap<String, String> params = ClientDiscoverAPI.getdeleteOrderNetRequestParams(rid);
                                HttpRequest.post(params,  URL.MY_DELETE_ORDER, new GlobalDataCallBack(){
//                                ClientDiscoverAPI.deleteOrderNet(rid, new RequestCallBack<String>() {
                                    @Override
                                    public void onSuccess(String json) {
                                        removeItem(position);
                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onFailure(String error) {

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
//                                        removeItem(position);
//                                        notifyDataSetChanged();
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
            case 12://退款中
                mHolder.mLeftButton.setVisibility(View.INVISIBLE);
                mHolder.mRightButton.setText(seeDtails);
                mHolder.mRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(context, OrderDetailsActivity.class);
                        in.putExtra("rid", rid);
                        context.startActivity(in);
                    }
                });
                break;
        }

        if (mHolder.mItem != null) {
            mHolder.mItem.removeAllViews();
        }
        for (int i = 0; i < list.get(position).getOrderItem().size(); i++) {
            View mView = View.inflate(context, R.layout.item_order_content, null);
            ImageView mImageView = (ImageView) mView.findViewById(R.id.image_order_inner);
            TextView mTitle = (TextView) mView.findViewById(R.id.tv_title_order_inner);
            TextView mColor = (TextView) mView.findViewById(R.id.tv_color_order_inner);
            TextView mCount = (TextView) mView.findViewById(R.id.tv_count_order_inner);
            TextView mMoney = (TextView) mView.findViewById(R.id.tv_money_order_inner);
            mTitle.setText(list.get(position).getOrderItem().get(i).getName());
            if (list.get(position).getOrderItem().get(i).getSku_name().equals("null")) {
                mColor.setVisibility(View.INVISIBLE);
            } else {
                mColor.setText(list.get(position).getOrderItem().get(i).getSku_name());
            }
            mCount.setText("× " + list.get(position).getOrderItem().get(i).getQuantity());
            mMoney.setText("¥" + list.get(position).getOrderItem().get(i).getSale_price());
            GlideUtils.displayImage(list.get(position).getOrderItem().get(i).getCover_url(), mImageView);
//            bitmapUtils_listview.display(mImageView, list.get(position).getOrderItem().get(i).getCover_url());
            mHolder.mItem.addView(mView);
        }
        mHolder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, OrderDetailsActivity.class);
                in.putExtra("rid", list.get(position).getRid());
                in.putExtra("optFragmentFlag", optFragmentFlag);
                context.startActivity(in);
            }
        });

        return convertView;
    }

    class ViewHolder {
        private LinearLayout mItem;
        private TextView mDate;
        private TextView mTradeStatus;
        private TextView mProductCounts;
        private TextView mTotalMoney;
        private TextView mPayMoney;
        private TextView mFreight;
        private TextView mLeftButton;
        private TextView mRightButton;
    }

    // 删除一条数据
    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}

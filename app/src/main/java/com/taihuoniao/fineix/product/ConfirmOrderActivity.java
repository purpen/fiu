package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ConfirmOrderProductsAdapter;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.DefaultAddressBean;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.NowConfirmBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.user.PayDetailsActivity;
import com.taihuoniao.fineix.user.SelectAddressActivity;
import com.taihuoniao.fineix.user.UsableRedPacketActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomDialogForPay;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;

/**
 * Created by taihuoniao on 2016/2/25.
 * 确认订单界面
 * 商品详情ratingbar
 */
public class ConfirmOrderActivity extends Activity implements View.OnClickListener {
    //商品详情界面传递过来的数据
    private NowBuyBean nowBuyBean;
    //购物车界面传递过来的数据
    private CartDoOrder cartBean;
    //界面下的控件
    private GlobalTitleLayout titleLayout;
    private RelativeLayout addressRelative;
    private TextView noAddressTv;
    private TextView nameTv;
    private TextView addressTv;
    private TextView addressDetailsTv;
    private TextView phoneTv;
    private ListViewForScrollView productsListView;
    private ConfirmOrderProductsAdapter productsAdapter;
    private RelativeLayout timeRelative;
    private TextView timeTv;
    private RelativeLayout redBagRelative;
    private TextView redBagTv;
    private TextView redBagCancelTv;
    private EditText editText;
    private TextView saveTv;
    private TextView saveMoneyTv;
    private TextView payPriceTv;
    private Button payBtn;
    //网络请求dialog
    private WaittingDialog dialog;
    //网络请求返回值
    private DefaultAddressBean addressDefaultBean;
    //收货地址界面选择的返回值
    private AddressListBean.AddressListItem addressListItem;
    //收货时间界面选择的返回值
    private String transfer_time = "a";
    private String bonus_code;
    private DecimalFormat df = null;
    //跳转到红包界面
    //rid传递过去的临时订单编号
    //返回数据 money  code
    private CustomDialogForPay mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        initView();
        WindowUtils.chenjin(this);
        setData();
        if (!dialog.isShowing()) {
            dialog.show();
        }
        getDefaultAddress();
    }

    private void getDefaultAddress() {
        ClientDiscoverAPI.getDefaultAddressNet(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<默认收货地址", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                DefaultAddressBean defaultAddressBean = new DefaultAddressBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<DefaultAddressBean>() {
                    }.getType();
                    defaultAddressBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<默认收货地址", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                DefaultAddressBean netAddress = defaultAddressBean;
                dialog.dismiss();
                if (netAddress.getData().getHas_default() == 1) {
                    addressDefaultBean = netAddress;
                    setAddressData(addressDefaultBean);
                } else {
                    ToastUtils.showInfo("默认地址不存在!");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
//                    Toast.makeText(ConfirmOrderActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        cancelNet();
        super.onDestroy();
    }

    private void setData() {
        nowBuyBean = (NowBuyBean) getIntent().getSerializableExtra("NowBuyBean");
        cartBean = (CartDoOrder) getIntent().getSerializableExtra("cartBean");
        if (nowBuyBean == null && cartBean == null) {
            ToastUtils.showError("数据异常，请重试");
//            dialog.showErrorWithStatus("数据异常，请重试");
//            Toast.makeText(ConfirmOrderActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        df = new DecimalFormat("######0.00");
        titleLayout.setContinueTvVisible(false);
        titleLayout.setTitle("确认订单");
        addressRelative.setOnClickListener(this);
        timeRelative.setOnClickListener(this);
        redBagRelative.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        if (nowBuyBean != null) {
            productsAdapter = new ConfirmOrderProductsAdapter(nowBuyBean.getData().getOrder_info().getDict().getItems(), ConfirmOrderActivity.this, null);
        } else if (cartBean != null) {
            productsAdapter = new ConfirmOrderProductsAdapter(null, ConfirmOrderActivity.this, cartBean.getCartOrderContentItems());
        }
        productsListView.setAdapter(productsAdapter);
        if (nowBuyBean != null) {
            payPriceTv.setText("¥ " + df.format(nowBuyBean.getData().getPay_money()));
            if (nowBuyBean.getData().getBonus().size() > 0) {
                redBagTv.setText("选择红包");
            }
        } else if (cartBean != null) {
            payPriceTv.setText("¥ " + df.format(Double.valueOf(cartBean.getPay_money())));
            if (cartBean.getBonus().size() > 0) {
                redBagTv.setText("选择红包");
            }
        }
        addressRelative.setFocusable(true);
        addressRelative.setFocusableInTouchMode(true);
        addressRelative.requestFocus();
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                cancelNet();
//            }
//        });
    }

    private void initView() {
//        StatusBarChange.initWindow(ConfirmOrderActivity.this);
        mDialog = new CustomDialogForPay(this);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_confirmorder_title);
        addressRelative = (RelativeLayout) findViewById(R.id.activity_confirmorder_addrelative);
        noAddressTv = (TextView) findViewById(R.id.activity_confirmorder_noaddresstv);
        nameTv = (TextView) findViewById(R.id.activity_confirmorder_name);
        addressTv = (TextView) findViewById(R.id.activity_confirmorder_address);
        addressDetailsTv = (TextView) findViewById(R.id.activity_confirmorder_fulladdress);
        phoneTv = (TextView) findViewById(R.id.activity_confirmorder_phone);
        productsListView = (ListViewForScrollView) findViewById(R.id.activity_confirmorder_productlist);
        RelativeLayout transferRelative = (RelativeLayout) findViewById(R.id.activity_confirmorder_transferrelative);
        TextView transferTv = (TextView) findViewById(R.id.activity_confirmorder_transfertv);
        timeRelative = (RelativeLayout) findViewById(R.id.activity_confirmorder_timerelative);
        timeTv = (TextView) findViewById(R.id.activity_confirmorder_timetv);
        redBagRelative = (RelativeLayout) findViewById(R.id.activity_confirmorder_redbagrelative);
        redBagTv = (TextView) findViewById(R.id.activity_confirmorder_redbagtv);
        editText = (EditText) findViewById(R.id.activity_confirmorder_edit);
        saveTv = (TextView) findViewById(R.id.activity_confirmorder_savetv);
        saveMoneyTv = (TextView) findViewById(R.id.activity_confirmorder_savemoney);
        payPriceTv = (TextView) findViewById(R.id.activity_confirmorder_payprice);
        payBtn = (Button) findViewById(R.id.activity_confirmorder_paybtn);
        dialog = new WaittingDialog(ConfirmOrderActivity.this);
        redBagCancelTv = (TextView) findViewById(R.id.activity_confirmorder_redbag_cannot_cancel);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_confirmorder_addrelative:
                //跳转到收货地址界面
                Intent intent = new Intent(ConfirmOrderActivity.this, SelectAddressActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_ADDRESS);
                break;
            case R.id.activity_confirmorder_timerelative:
                Intent intent1 = new Intent(ConfirmOrderActivity.this, TransferTimeActivity.class);
                intent1.putExtra("transfer_time", transfer_time);
                startActivityForResult(intent1, DataConstants.REQUESTCODE_TRANSFER_TIME);
                break;
            case R.id.activity_confirmorder_redbagrelative:

                //跳转到红包界面
                Intent intent2 = new Intent(ConfirmOrderActivity.this, UsableRedPacketActivity.class);
                intent2.putExtra("rid", nowBuyBean == null ? cartBean.getRid() : nowBuyBean.getData().getOrder_info().getRid());

                startActivityForResult(intent2, DataConstants.REQUESTCODE_REDBAG);
                break;
            case R.id.activity_confirmorder_paybtn:
//                startActivity(new Intent(ConfirmOrderActivity.this, PayWayActivity.class));
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                if (addressListItem == null && addressDefaultBean == null) {
                    dialog.dismiss();
                    ToastUtils.showError("请选择收货地址!");
//                    dialog.showErrorWithStatus("请选择收货地址!");
//                    Toast.makeText(ConfirmOrderActivity.this, "请选择收货地址...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nowBuyBean != null)
                    confirmOrder(nowBuyBean.getData().getOrder_info().get_id(), addressListItem == null ? addressDefaultBean.getData().get_id() : addressListItem.get_id(), nowBuyBean.getData().getIs_nowbuy() + "", editText.getText().toString(), transfer_time, bonus_code);
                else if (cartBean != null)
                    confirmOrder(cartBean.get_id(), addressListItem == null ? addressDefaultBean.getData().get_id() : addressListItem.get_id(), cartBean.getIs_nowbuy(), editText.getText().toString(), transfer_time, bonus_code);
                break;
        }
    }

    private void confirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code) {
        ClientDiscoverAPI.nowConfirmOrder(rrid, addbook_id, is_nowbuy, summary, transfer_time, bonus_code, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NowConfirmBean nowConfirmBean = new NowConfirmBean();
                try {
                    JSONObject job = new JSONObject(responseInfo.result);
                    nowConfirmBean.setSuccess(job.optBoolean("success"));
                    nowConfirmBean.setMessage(job.optString("message"));
                    JSONObject data = job.getJSONObject("data");
                    if (nowConfirmBean.isSuccess()) {
                        nowConfirmBean.setRid(data.optString("rid"));
                        nowConfirmBean.setPay_money(data.optString("pay_money"));
                        nowConfirmBean.status = data.optInt("status");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                //                    Toast.makeText(ConfirmOrderActivity.this, netConfirmBean.getMessage(), Toast.LENGTH_SHORT).show();
                if (nowConfirmBean.isSuccess()) {
                    ToastUtils.showSuccess(nowConfirmBean.getMessage());
//                        dialog.showSuccessWithStatus(netConfirmBean.getMessage());
//                        netConfirmBean.getRid();     //订单rid
                    Intent intent;
                    switch (nowConfirmBean.status) {
                        case 1:// 等待付款
                            intent = new Intent(ConfirmOrderActivity.this, PayWayActivity.class);
                            intent.putExtra("paymoney", nowConfirmBean.getPay_money());
                            intent.putExtra("orderId", nowConfirmBean.getRid());
                            startActivity(intent);
                            finish();
                            break;
                        case 10: //10.等待发货(0元，跳过支付流程)
//                            intent = new Intent(ConfirmOrderActivity.this, PayDetailsActivity.class);
//                            intent.putExtra("rid",nowConfirmBean.getRid());
//                            startActivity(intent);
//                            finish();
                            delayToPayDetail(nowConfirmBean);
                            break;
                        default:
                            ToastUtils.showError("订单状态异常！");
                            break;
                    }
                } else {
                    ToastUtils.showError(nowConfirmBean.getMessage());
//                        dialog.showErrorWithStatus(netConfirmBean.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
//                    Toast.makeText(ConfirmOrderActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //延时三秒再跳转去订单支付详情界面是为给服务器留时间以确保其及时更新数据
    private void delayToPayDetail(final NowConfirmBean nowConfirmBean) {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDialog.dismiss();
                toPayDetailsActivity(nowConfirmBean);
            }
        }, 2000);
    }

    //跳到订单支付详情界面
    private void toPayDetailsActivity(NowConfirmBean nowConfirmBean) {
        Intent intent = new Intent(this, PayDetailsActivity.class);
        intent.putExtra("rid", nowConfirmBean.getRid());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (resultCode) {
            case DataConstants.RESULTCODE_REDBAG:
                String money = data.getStringExtra("money");
                bonus_code = data.getStringExtra("code");
                if (money != null && bonus_code != null) {
                    saveTv.setVisibility(View.VISIBLE);
                    saveMoneyTv.setVisibility(View.VISIBLE);
                    saveMoneyTv.setText("¥ " + df.format(Double.valueOf(money)));
                    redBagTv.setText("使用了" + money + "元红包");
                    redBagCancelTv.setText("您使用了一张" + money + "元红包，下单后将不可恢复");
                    if (nowBuyBean != null) {
                        double nowPrice = nowBuyBean.getData().getPay_money() - Double.valueOf(money);
                        if (nowPrice <= 0) {
                            payPriceTv.setText("¥ 0.00");
                        } else {
                            payPriceTv.setText("¥ " + df.format(nowPrice));
                        }
                    } else if (cartBean != null) {
                        double nowPrice = Double.valueOf(cartBean.getPay_money()) - Double.valueOf(money);
                        if (nowPrice <= 0) {
                            payPriceTv.setText("¥ 0.00");
                        } else {
                            payPriceTv.setText(String.format("¥ %s", df.format(Double.valueOf(cartBean.getPay_money()) - Double.valueOf(money))));
                        }

                    }
                }
                break;
            case DataConstants.RESULTCODE_ADDRESS:
                addressListItem = (AddressListBean.AddressListItem) data.getSerializableExtra("addressBean");
                if (addressListItem != null) {
                    setAddressData(addressListItem);
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    getDefaultAddress();
//                    DataPaser.getDefaultAddress(mHandler);
                }
                break;
            case DataConstants.RESULTCODE_TRANSFER_TIME:
                transfer_time = data.getStringExtra("transfer_time");
                switch (transfer_time) {
                    case "a":
                        timeTv.setText(R.string.transfer_time_a);
                        break;
                    case "b":
                        timeTv.setText(R.string.transfer_time_b);
                        break;
                    case "c":
                        timeTv.setText(R.string.transfer_time_c);
                        break;
                }
                break;
        }
    }

    private void setAddressData(DefaultAddressBean address) {
        noAddressTv.setVisibility(View.GONE);
        nameTv.setText(address.getData().getName());
        addressTv.setText(address.getData().getProvince_name() + "  " + address.getData().getCity_name());
        addressDetailsTv.setText(address.getData().getAddress());
        phoneTv.setText(address.getData().getPhone());
    }

    private void setAddressData(AddressListBean.AddressListItem address) {
        noAddressTv.setVisibility(View.GONE);
        nameTv.setText(address.getName());
        addressTv.setText(address.getProvince_name() + "  " + address.getCity_name());
        addressDetailsTv.setText(address.getAddress());
        phoneTv.setText(address.getPhone());
    }


    private void cancelNet() {
        NetworkManager.getInstance().cancel("getDefaultAddress");
        NetworkManager.getInstance().cancel("nowConfirmOrder");
    }

}

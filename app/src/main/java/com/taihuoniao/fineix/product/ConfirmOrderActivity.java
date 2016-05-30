package com.taihuoniao.fineix.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ConfirmOrderProductsAdapter;
import com.taihuoniao.fineix.beans.AddressBean;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.NowConfirmBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.user.SelectAddressActivity;
import com.taihuoniao.fineix.user.UsableRedPacketActivity;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

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
    private MyGlobalTitleLayout titleLayout;
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
    private SVProgressHUD dialog;
    //网络请求返回值
    private AddressBean addressBean;
    //收货地址界面选择的返回值
    private AddressBean address;
    //收货时间界面选择的返回值
    private String transfer_time = "a";
    //红包界面选择的返回值
    private String money;
    private String bonus_code;
    private DecimalFormat df = null;
    //跳转到红包界面
    //rid传递过去的临时订单编号
    //返回数据 money  code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        initView();
        setData();
        dialog.show();
        DataPaser.getDefaultAddress(mHandler);
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
            Toast.makeText(ConfirmOrderActivity.this, "数据异常，请重试", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        df = new DecimalFormat("######0.00");
        titleLayout.setRightSearchButton(false);
        titleLayout.setRightShopCartButton(false);
        titleLayout.setTitleColor(getResources().getColor(R.color.black333333));
        titleLayout.setTitle("确认订单");
        addressRelative.setOnClickListener(this);
        timeRelative.setOnClickListener(this);
        redBagRelative.setOnClickListener(this);
        payBtn.setOnClickListener(this);
        if (nowBuyBean != null) {
            productsAdapter = new ConfirmOrderProductsAdapter(nowBuyBean.getItemList(), ConfirmOrderActivity.this, null);
        } else if (cartBean != null) {
            productsAdapter = new ConfirmOrderProductsAdapter(null, ConfirmOrderActivity.this, cartBean.getCartOrderContentItems());
        }
        productsListView.setAdapter(productsAdapter);
        if (nowBuyBean != null) {
            payPriceTv.setText("¥ " + df.format(Double.valueOf(nowBuyBean.getPay_money())));
            if (nowBuyBean.getBonus_number() > 0) {
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
        titleLayout = (MyGlobalTitleLayout) findViewById(R.id.activity_confirmorder_title);
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
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
        dialog = new SVProgressHUD(ConfirmOrderActivity.this);
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
                intent2.putExtra("rid", nowBuyBean == null ? cartBean.getRid() : nowBuyBean.getRid());

                startActivityForResult(intent2, DataConstants.REQUESTCODE_REDBAG);
                break;
            case R.id.activity_confirmorder_paybtn:
//                startActivity(new Intent(ConfirmOrderActivity.this, PayWayActivity.class));
                dialog.show();
                if (address == null && addressBean == null) {
                    dialog.dismiss();
                    Toast.makeText(ConfirmOrderActivity.this, "请选择收货地址...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (nowBuyBean != null)
                    DataPaser.nowConfirmOrder(nowBuyBean.get_id(), address == null ? addressBean.get_id() : address.get_id(), nowBuyBean.getIs_nowbuy(), editText.getText().toString(), transfer_time, bonus_code, mHandler);
                else if (cartBean != null)
                    DataPaser.nowConfirmOrder(cartBean.get_id(), address == null ? addressBean.get_id() : address.get_id(), cartBean.getIs_nowbuy(), editText.getText().toString(), transfer_time, bonus_code, mHandler);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (resultCode) {
            case DataConstants.RESULTCODE_REDBAG:
                money = data.getStringExtra("money");
                bonus_code = data.getStringExtra("code");
                if (money != null && bonus_code != null) {
                    saveTv.setVisibility(View.VISIBLE);
                    saveMoneyTv.setVisibility(View.VISIBLE);
                    saveMoneyTv.setText("¥ " + df.format(Double.valueOf(money)));
                    redBagTv.setText("使用了"+money+"元红包");
                    redBagCancelTv.setText("您使用了一张"+money+"元红包，下单后将不可恢复");
                    if (nowBuyBean != null) {
                        double nowPrice = Double.valueOf(nowBuyBean.getPay_money())-Double.valueOf(money);
                        if(nowPrice<=0){
                            payPriceTv.setText("¥ 0.00");
                        }else{
                        payPriceTv.setText("¥ " + df.format(nowPrice));}
                    } else if (cartBean != null) {
                        double nowPrice = Double.valueOf(cartBean.getPay_money())-Double.valueOf(money);
                        if(nowPrice<=0){
                            payPriceTv.setText("¥ 0.00");
                        }else{
                            payPriceTv.setText(String.format("¥ %s", df.format(Double.valueOf(cartBean.getPay_money()) - Double.valueOf(money))));
                        }

                    }
                }
                break;
            case DataConstants.RESULTCODE_ADDRESS:
                address = (AddressBean) data.getSerializableExtra("addressBean");
                if (address != null) {
                    setAddressData(address);
                } else {
                    dialog.show();
                    DataPaser.getDefaultAddress(mHandler);
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

    private void setAddressData(AddressBean address) {
        noAddressTv.setVisibility(View.GONE);
        nameTv.setText(address.getName());
        addressTv.setText(address.getProvince_name() + "  " + address.getCity_name());
        addressDetailsTv.setText(address.getAddress());
        phoneTv.setText(address.getPhone());
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.NOW_CONFIRM_ORDER:
                    dialog.dismiss();
                    NowConfirmBean netConfirmBean = (NowConfirmBean) msg.obj;
                    Toast.makeText(ConfirmOrderActivity.this, netConfirmBean.getMessage(), Toast.LENGTH_SHORT).show();
                    if (netConfirmBean.isSuccess()) {
//                        netConfirmBean.getRid();     //订单rid
                        Intent intent = new Intent(ConfirmOrderActivity.this, PayWayActivity.class);
                        intent.putExtra("paymoney", netConfirmBean.getPay_money());
                        intent.putExtra("orderId", netConfirmBean.getRid());
                        startActivity(intent);
                        finish();
                    }
                    break;
                case DataConstants.DEFAULT_ADDRESS:
                    dialog.dismiss();
                    AddressBean netAddress = (AddressBean) msg.obj;
                    dialog.dismiss();
                    if (netAddress != null && "1".equals(netAddress.getHas_default())) {
                        addressBean = netAddress;
                        setAddressData(addressBean);
                    } else {
                        Toast.makeText(ConfirmOrderActivity.this, R.string.no_default_address, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NETWORK_FAILURE:
                    dialog.dismiss();
                    Toast.makeText(ConfirmOrderActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void cancelNet() {
        NetworkManager.getInstance().cancel("getDefaultAddress");
        NetworkManager.getInstance().cancel("nowConfirmOrder");
    }
}

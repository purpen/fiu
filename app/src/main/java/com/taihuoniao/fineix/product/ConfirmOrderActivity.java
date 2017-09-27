package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ConfirmOrderProductsAdapter;
import com.taihuoniao.fineix.base.Base2Activity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.beans.CartDoOrder;
import com.taihuoniao.fineix.beans.DefaultAddressBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.NowBuyBean;
import com.taihuoniao.fineix.beans.NowConfirmBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.bean.FreightBean;
import com.taihuoniao.fineix.user.PayDetailsActivity;
import com.taihuoniao.fineix.user.SelectAddressActivity;
import com.taihuoniao.fineix.user.UsableRedPacketActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.dialog.CustomDialogForPay;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;

import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/2/25.
 * 确认订单界面
 * 商品详情ratingbar
 */
public class ConfirmOrderActivity extends Base2Activity implements View.OnClickListener {

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
    private LinearLayout linearLayout_privilege;
    private TextView textView_privilege;
    private TextView transferTv;
    private RadioButton rbkd;
    private RadioButton rbzt;
    private WaittingDialog dialog;

    //商品详情界面传递过来的数据
    private NowBuyBean nowBuyBean;
    private CartDoOrder cartBean;   //购物车界面传递过来的数据
    private DefaultAddressBean addressDefaultBean;  //网络请求返回值
    private AddressListBean.RowsEntity addressListItem; //收货地址界面选择的返回值
    private String transfer_time = "a"; //收货时间界面选择的返回值
    private String bonus_code;
    private DecimalFormat df = null;
    public static final String DELIVERY_TYPE_KD="1"; //快递发货
    public static final String DELIVERY_TYPE_ZT="2"; //自提
    //跳转到红包界面
    //rid传递过去的临时订单编号
    //返回数据 money  code
    private CustomDialogForPay mDialog;
    private String curAddressId;
    private double privilegeprice;      //节省
    private String freight;     //邮费
    private double sumPrice;
    private TextView tv_zt_tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
        initView();
        installListener();
        setData();
        if (!dialog.isShowing()) {
            dialog.show();
        }
        getDefaultAddress();
    }

    private void installListener() {
        rbkd.setOnClickListener(this);
        rbzt.setOnClickListener(this);
    }

    private Call addressHandler;

    @Override
    protected void onDestroy() {
        cancelNet();
        super.onDestroy();
    }

    private void getDefaultAddress() {
        addressHandler = HttpRequest.post(URL.URLSTRING_DEFAULT_ADDRESS, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<DefaultAddressBean> netAddress = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DefaultAddressBean>>() {});
                dialog.dismiss();
                if (netAddress.getData().getHas_default() == 1) {
                    addressDefaultBean = netAddress.getData();
                    curAddressId=addressDefaultBean.get_id();
                    setAddressData(addressDefaultBean);
                } else {
                    ToastUtils.showInfo("默认地址不存在!");
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    private void setData() {
        nowBuyBean = (NowBuyBean) getIntent().getSerializableExtra("NowBuyBean");
        cartBean = (CartDoOrder) getIntent().getSerializableExtra("cartBean");
        if (nowBuyBean == null && cartBean == null) {
            ToastUtils.showError("数据异常，请重试");
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
            productsAdapter = new ConfirmOrderProductsAdapter(nowBuyBean.getOrder_info().getDict().getItems(), ConfirmOrderActivity.this, null);
        } else if (cartBean != null) {
            productsAdapter = new ConfirmOrderProductsAdapter(null, ConfirmOrderActivity.this, cartBean.getCartOrderContentItems());
        }
        productsListView.setAdapter(productsAdapter);
        if (nowBuyBean != null) {
            sumPrice = nowBuyBean.getPay_money();
            payPriceTv.setText(String.format("¥ %s" , sumPrice));
            if (nowBuyBean.getBonus().size() > 0) {
                redBagTv.setText("选择红包");
            }
            try {
                freight = nowBuyBean.getOrder_info().getDict().getFreight();
                transferTv.setText(String.format(" ¥ %s", TextUtils.isEmpty(freight) ? "0" : freight));

                // 2016/12/2 优惠立减
                if ("5".equals(nowBuyBean.getOrder_info().getKind())) {
                    String coin_money = nowBuyBean.getOrder_info().getDict().getCoin_money();
                    privilegeprice = Double.parseDouble(coin_money);
                    textView_privilege.setText(String.format("¥ %s", coin_money));
                    saveMoneyTv.setText(String.format(" ¥ %s", coin_money));
                    linearLayout_privilege.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_privilege.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (cartBean != null) {
            sumPrice = Double.parseDouble(cartBean.getPay_money());
            payPriceTv.setText(String.format("¥ %s" , sumPrice));
            if (cartBean.getBonus().size() > 0) {
                redBagTv.setText("选择红包");
            }
            try {
                freight = cartBean.getDictBeen().get(0).getFreight();
                transferTv.setText(String.format(" ¥ %s", TextUtils.isEmpty(freight) ? "0" : freight));

                // 2016/12/2 优惠立减
                if ("5".equals(cartBean.getKind())) {
                    String coin_money = cartBean.getDictBeen().get(0).getCoin_money();
                    privilegeprice = Double.parseDouble(coin_money);
                    textView_privilege.setText(String.format("¥ %s", coin_money));
                    saveMoneyTv.setText(String.format(" ¥ %s", coin_money));
                    linearLayout_privilege.setVisibility(View.VISIBLE);
                } else {
                    linearLayout_privilege.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addressRelative.setFocusable(true);
        addressRelative.setFocusableInTouchMode(true);
        addressRelative.requestFocus();
    }

    private void initView() {
        mDialog = new CustomDialogForPay(this);
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_confirmorder_title);
        rbkd = (RadioButton) findViewById(R.id.rb_kd);
        rbzt = (RadioButton) findViewById(R.id.rb_zt);
        tv_zt_tips = (TextView) findViewById(R.id.tv_zt_tips);
        addressRelative = (RelativeLayout) findViewById(R.id.activity_confirmorder_addrelative);
        noAddressTv = (TextView) findViewById(R.id.activity_confirmorder_noaddresstv);
        nameTv = (TextView) findViewById(R.id.activity_confirmorder_name);
        addressTv = (TextView) findViewById(R.id.activity_confirmorder_address);
        addressDetailsTv = (TextView) findViewById(R.id.activity_confirmorder_fulladdress);
        phoneTv = (TextView) findViewById(R.id.activity_confirmorder_phone);
        productsListView = (ListViewForScrollView) findViewById(R.id.activity_confirmorder_productlist);
        transferTv = (TextView) findViewById(R.id.activity_confirmorder_transfertv);
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

        //立减优惠
        linearLayout_privilege = (LinearLayout) this.findViewById(R.id.liearLayout_privilege);
        textView_privilege = (TextView) this.findViewById(R.id.textView_privilege);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_kd:
                addressRelative.setVisibility(View.VISIBLE);
                tv_zt_tips.setVisibility(View.GONE);
                break;
            case R.id.rb_zt:
                addressRelative.setVisibility(View.GONE);
                tv_zt_tips.setVisibility(View.VISIBLE);
                break;
            case R.id.activity_confirmorder_addrelative:
                //跳转到收货地址界面
                Intent intent = new Intent(ConfirmOrderActivity.this, SelectAddressActivity.class);
                intent.putExtra(SelectAddressActivity.class.getSimpleName(),curAddressId);
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
                intent2.putExtra("rid", nowBuyBean == null ? cartBean.getRid() : nowBuyBean.getOrder_info().getRid());

                startActivityForResult(intent2, DataConstants.REQUESTCODE_REDBAG);
                break;
            case R.id.activity_confirmorder_paybtn:
//                快递类型
                String delivery_type;
                String addbook_id;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                if (rbkd.isChecked()){
                    if (addressListItem == null && addressDefaultBean == null) {
                        dialog.dismiss();
                        ToastUtils.showError("请选择收货地址!");
                        return;
                    }
                    delivery_type=DELIVERY_TYPE_KD;
                    addbook_id = addressListItem == null ? addressDefaultBean.get_id() : addressListItem._id;
                }else {
                    delivery_type=DELIVERY_TYPE_ZT;
                    addbook_id="";
                }
                if (nowBuyBean != null)
                    confirmOrder(nowBuyBean.getOrder_info().get_id(), addbook_id, nowBuyBean.getIs_nowbuy() + "", editText.getText().toString(), transfer_time, bonus_code,delivery_type);
                else if (cartBean != null)
                    confirmOrder(cartBean.get_id(), addbook_id, cartBean.getIs_nowbuy(), editText.getText().toString(), transfer_time, bonus_code,delivery_type);
                break;
        }
    }
    private Call confirmHandler;
    private void confirmOrder(String rrid, String addbook_id, String is_nowbuy, String summary, String transfer_time, String bonus_code,String delivery_type) {
        HashMap<String, String> params =ClientDiscoverAPI. getnowConfirmOrderRequestParams(rrid, addbook_id, is_nowbuy, summary, transfer_time, bonus_code,delivery_type);
        confirmHandler = HttpRequest.post(params,  URL.URLSTRING_NOW_CONFIRMORDER, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                NowConfirmBean nowConfirmBean = new NowConfirmBean();
                try {
                    JSONObject job = new JSONObject(json);
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
                if (nowConfirmBean.isSuccess()) {
                    Intent intent;
                    switch (nowConfirmBean.status) {
                        case 1:// 等待付款
                            intent = new Intent(ConfirmOrderActivity.this, PayWayActivity.class);
                            intent.putExtra("paymoney", nowConfirmBean.getPay_money());
                            sumPrice = Double.parseDouble(nowConfirmBean.getPay_money());
                            intent.putExtra("orderId", nowConfirmBean.getRid());
                            startActivity(intent);
                            finish();
                            break;
                        case 10: //10.等待发货(0元，跳过支付流程)
                            delayToPayDetail(nowConfirmBean);
                            break;
                        default:
                            ToastUtils.showError("订单状态异常！");
                            break;
                    }
                } else {
                    ToastUtils.showError(nowConfirmBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
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
                    try {
                        saveMoneyTv.setText(" ¥ " + df.format(Double.parseDouble(money) + privilegeprice));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    redBagTv.setText("使用了" + money + "元红包");
                    redBagCancelTv.setText("您使用了一张" + money + "元红包，下单后将不可恢复");
                    if (nowBuyBean != null) {
                        double nowPrice = nowBuyBean.getPay_money() - Double.parseDouble(money);
                        if (nowPrice <= 0) {
                            payPriceTv.setText("¥ 0.00");
                            sumPrice = 0D;
                        } else {
                            payPriceTv.setText("¥ " + df.format(nowPrice));
                            sumPrice = nowPrice;
                        }
                    } else if (cartBean != null) {
                        double nowPrice = Double.valueOf(cartBean.getPay_money()) - Double.valueOf(money);
                        if (nowPrice <= 0) {
                            payPriceTv.setText("¥ 0.00");
                            sumPrice = 0D;
                        } else {
                            payPriceTv.setText(String.format("¥ %s", df.format(sumPrice)));
                            sumPrice = nowPrice;
                        }
                    }
                }
                break;
            case DataConstants.REQUESTCODE_ADDRESS:
                addressListItem = (AddressListBean.RowsEntity) data.getSerializableExtra("addressBean");
                if (addressListItem != null) {
                    curAddressId=addressListItem._id;
                    setAddressData(addressListItem);

                    fetchFreight(curAddressId, null);
                } else {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    getDefaultAddress();
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
        nameTv.setText(address.getName());
        addressTv.setText(address.province + " " + address.city+" "+ address.county+" "+ address.town);
        addressDetailsTv.setText(address.getAddress());
        phoneTv.setText(address.getPhone());
    }

    private void setAddressData(AddressListBean.RowsEntity address) {
        noAddressTv.setVisibility(View.GONE);
        nameTv.setText(address.name);
        addressTv.setText(address.province +" "+address.city+" "+address.county+" "+address.town);
        addressDetailsTv.setText(address.address);
        phoneTv.setText(address.phone);
    }

    private void cancelNet() {
        if(addressHandler!=null)
            addressHandler.cancel();
        if(confirmHandler!=null)
            confirmHandler.cancel();
    }

    // 2016/12/8 计算邮费
    private void fetchFreight(String addbook_id, String rid){
        String rid2 = nowBuyBean == null ? cartBean.getRid() : nowBuyBean.getOrder_info().getRid();
        HashMap<String, String> params = ClientDiscoverAPI.getFetchFreightRequestParams(addbook_id, rid2);
        HttpRequest.post(params,URL.SHOPPING_FETCH_FREIGHT, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<FreightBean> freightBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<FreightBean>>() {
                });
                if (freightBeanHttpResponse != null) {
                    String newfreight = freightBeanHttpResponse.getData().getFreight();
                    if (newfreight != null) {
                        transferTv.setText(String.format(" ¥ %s", TextUtils.isEmpty(newfreight) ? "0" : newfreight));
                        sumPrice = sumPrice - Double.parseDouble(freight) + Double.parseDouble(newfreight);
                        payPriceTv.setText(String.format("¥ %s", sumPrice));
                    }
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}

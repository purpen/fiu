package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackResultDetailsBean;
import com.taihuoniao.fineix.user.ShopOrderListActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargeBackDetailsActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.imageView_goods)
    ImageView imageViewGoods;
    @Bind(R.id.textView_goods_description)
    TextView textViewGoodsDescription;
    @Bind(R.id.textView_specification)
    TextView textViewSpecification;
    @Bind(R.id.textView_price)
    TextView textViewPrice;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.textView3)
    TextView textView3;
    @Bind(R.id.textView4)
    TextView textView4;
    @Bind(R.id.textView5)
    TextView textView5;
    @Bind(R.id.linearLayout_container)
    LinearLayout linearLayoutContainer;
    @Bind(R.id.textView_chargeBack_requst_date)
    TextView textViewChargeBackRequstDate;

    private String chargeBackId;
    private ChargeBackResultDetailsBean chargeBackResultDetailsBean;

    public ChargeBackDetailsActivity() {
        super(R.layout.activity_salesafter_chargeback_details);
    }

    @Override
    protected void getIntentData() {
        chargeBackId = getIntent().getStringExtra(KEY.CHARGEBACK_ID);
    }

    @Override
    protected void requestNet() {
        super.requestNet();
        requestResultInfo();
    }

    private void requestResultInfo() {
        HashMap<String, String> params = ClientDiscoverAPI.getRefundSuccessInfoRequestParams(chargeBackId);
        HttpRequest.post(params, URL.SHOPPING_REFUND_VIEW, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getRefundSuccessInfo(chargeBackId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                try {
                    HttpResponse<ChargeBackResultDetailsBean> chargeBackBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ChargeBackResultDetailsBean>>() {
                    });
                    if (chargeBackBeanHttpResponse.isSuccess()) {
                        chargeBackResultDetailsBean = chargeBackBeanHttpResponse.getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (chargeBackResultDetailsBean != null) {
                    refreshView();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, App.getString(R.string.title_salesAfter_chargeBack_details));
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private void refreshView() {
        ChargeBackResultDetailsBean.ProductEntity product = chargeBackResultDetailsBean.getProduct();
        GlideUtils.displayImage(product.getCover_url(), imageViewGoods);
        textViewGoodsDescription.setText(product.getTitle());
        textViewSpecification.setText(product.getSku_name() + String.format(" * %s", chargeBackResultDetailsBean.getQuantity()));
        textViewPrice.setText(String.format("¥%s", product.getSale_price()));

        //chargeBackInfo
        textView1.setText(String.format("¥%s", chargeBackResultDetailsBean.getRefund_price()));
        textView2.setText(String.valueOf(chargeBackResultDetailsBean.getReason_label()));
        textView3.setText(chargeBackResultDetailsBean.getContent());
        textView4.setText(String.valueOf(chargeBackResultDetailsBean.get_id()));
        String refund_at = chargeBackResultDetailsBean.getRefund_at();
        textView5.setText(TextUtils.isEmpty(refund_at) ? chargeBackResultDetailsBean.getCreated_at() : refund_at);
        textViewChargeBackRequstDate.setText(TextUtils.isEmpty(refund_at) ? App.getString(R.string.text_salesAfter_chargeBack_details_initDate) : App.getString(R.string.text_salesAfter_chargeBack_details_finishDate));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChargeBackDetailsActivity.this, ShopOrderListActivity.class));
    }
}

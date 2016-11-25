package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackResultBean;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackResultDetailsBean;
import com.taihuoniao.fineix.user.ShopOrderListActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

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
    @Bind(R.id.linearLayout_container)
    LinearLayout linearLayoutContainer;

    private static final String TITLE = "退款详情";
    private String chargeBackId;
    private ChargeBackResultDetailsBean chargeBackResultDetailsBean;

    public ChargeBackDetailsActivity() {
        super(R.layout.activity_chargeback_details);
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
        ClientDiscoverAPI.getRefundSuccessInfo(chargeBackId, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }
                try {
                    HttpResponse<ChargeBackResultDetailsBean> chargeBackBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ChargeBackResultDetailsBean>>(){});
                    if (chargeBackBeanHttpResponse.isSuccess()) {
                        chargeBackResultDetailsBean = chargeBackBeanHttpResponse.getData();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (chargeBackResultDetailsBean != null) {
                    refreshView();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void refreshView(){
        ChargeBackResultDetailsBean.ProductEntity product = chargeBackResultDetailsBean.getProduct();
        GlideUtils.displayImage(product.getCover_url(), imageViewGoods);
        textViewGoodsDescription.setText(product.getTitle());
        textViewSpecification.setText(product.getSku_name());
        textViewPrice.setText(String.format("¥%s", product.getSale_price()));

        //chargeBackInfo
        textView1.setText(String.format("¥%s", chargeBackResultDetailsBean.getRefund_price()));
        textView2.setText(String.valueOf(chargeBackResultDetailsBean.getReason()));
        textView3.setText(chargeBackResultDetailsBean.getContent());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChargeBackDetailsActivity.this, ShopOrderListActivity.class));
    }
}

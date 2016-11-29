package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackResultDetailsBean;
import com.taihuoniao.fineix.user.ShopOrderListActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SalesReturnDetailsActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.textView_goods_district)
    TextView textViewGoodsDistrict;
    @Bind(R.id.textView_goods_address)
    TextView textViewGoodsAddress;
    @Bind(R.id.textView_goods_phoneNumber)
    TextView textViewGoodsPhoneNumber;
    @Bind(R.id.imageView_goods)
    ImageView imageViewGoods;
    @Bind(R.id.textView_goods_description)
    TextView textViewGoodsDescription;
    @Bind(R.id.textView_specification)
    TextView textViewSpecification;
    @Bind(R.id.textView_status)
    TextView textViewStatus;
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

    private static final String TITLE = "退货详情";
    private String chargeBackId;
    private ChargeBackResultDetailsBean chargeBackResultDetailsBean;

    public SalesReturnDetailsActivity() {
        super(R.layout.activity_salereturn_details);
    }

    @Override
    protected void getIntentData() {
        chargeBackId = getIntent().getStringExtra(KEY.CHARGEBACK_ID);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
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
                    HttpResponse<ChargeBackResultDetailsBean> chargeBackBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ChargeBackResultDetailsBean>>() {
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
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void refreshView() {

        //productInfo
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
        textViewChargeBackRequstDate.setText(TextUtils.isEmpty(refund_at) ? "申请时间" : "退货时间");
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SalesReturnDetailsActivity.this, ShopOrderListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

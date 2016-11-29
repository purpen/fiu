package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackListBean;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChargeBackResultActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.imageView_chargeBack_result_status)
    ImageView imageViewChargeBackResultStatus;
    @Bind(R.id.textView_chargeBack_result_description)
    TextView textViewChargeBackResultDescription;
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
    @Bind(R.id.textView_price)
    TextView textViewPrice;
    @Bind(R.id.linerLayout_container_goods_info)
    LinearLayout linerLayoutContainerGoodsInfo;
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
    @Bind(R.id.textView_create_time)
    TextView textViewCreateTime;

    private String[] textStatus = App.getStringArray(R.array.arrays_salesAfter_result_status);
    private int[] iconStatus = {R.mipmap.icon_chargeback_result_success, R.mipmap.icon_chargeback_result_fail};
    private ChargeBackListBean.RowsEntity chargeBackResultDetailsBean;

    public ChargeBackResultActivity() {
        super(R.layout.activity_salesafter_chargeback_result);
    }

    @Override
    protected void getIntentData() {
        chargeBackResultDetailsBean = getIntent().getParcelableExtra(KEY.CHARGEBACK_RESULT);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, App.getString(R.string.title_salesAfter_chargeBack_result));
        WindowUtils.chenjin(this);
    }

    @Override
    protected void requestNet() {
        super.requestNet();
        refreshView();
    }

    private void refreshView() {

        //productInfo
        ChargeBackListBean.RowsEntity.ProductEntity product = chargeBackResultDetailsBean.getProduct();
//        ChargeBackListBean.RowsEntity.ProductEntity product = ;
        GlideUtils.displayImage(product.getCover_url(), imageViewGoods);
        textViewGoodsDescription.setText(product.getTitle());
        textViewSpecification.setText(product.getSku_name() + String.format(" * %s", product.getQuantity()));
        textViewPrice.setText(String.format("¥%s", product.getSale_price()));

        //chargeBackInfo
        textView1.setText(String.format("¥%s", chargeBackResultDetailsBean.getRefund_price()));
        textView2.setText(String.valueOf(chargeBackResultDetailsBean.getReason_label()));
        textView3.setText(chargeBackResultDetailsBean.getContent());
        textView4.setText(String.format("%s", chargeBackResultDetailsBean.get_id()));

        //stage
        String stage = chargeBackResultDetailsBean.getStage();
        switch (stage) { // 进度：0.取消；1.进行中；2.已完成；3.拒绝;
            case "1":
                textViewCreateTime.setText(App.getString(R.string.text_salesAfter_chargeBack_details_initDate));
                textView5.setText(chargeBackResultDetailsBean.getCreated_at());
                break;
            case "2":
                imageViewChargeBackResultStatus.setImageResource(iconStatus[0]);
                textViewChargeBackResultDescription.setText(textStatus[0]);
                textView5.setText(chargeBackResultDetailsBean.getRefund_at());
                break;
            case "3":
                imageViewChargeBackResultStatus.setImageResource(iconStatus[1]);
                textViewChargeBackResultDescription.setText(textStatus[1]);

                textViewCreateTime.setText(App.getString(R.string.text_salesAfter_chargeBack_details_initDate));
                textView5.setText(chargeBackResultDetailsBean.getCreated_at());
                break;
        }

        linerLayoutContainerGoodsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeBackResultActivity.this, BuyGoodsDetailsActivity.class);
                intent.putExtra("id", String.valueOf(chargeBackResultDetailsBean.getProduct_id()));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }
}

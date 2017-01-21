package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.TradeRecordDetailsBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/1/16 17:52
 * Email: 895745843@qq.com
 */

public class TradeRecordeDetailsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.linearLayout1)
    LinearLayout linearLayout1;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @Bind(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @Bind(R.id.linearLayout5)
    LinearLayout linearLayout5;
    @Bind(R.id.linearLayout6)
    LinearLayout linearLayout6;
    @Bind(R.id.linearLayout7)
    LinearLayout linearLayout7;
    private String id;

    public TradeRecordeDetailsActivity() {
        super(R.layout.activity_alliance_trade_record_details);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "交易明细");
        WindowUtils.chenjin(this);
        id = getIntent().getStringExtra("_id");
    }

    @Override
    protected void requestNet() {
        requestDataList();
    }

    private void requestDataList() {
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getTradeRecordedetails(id);
        HttpRequest.post(tradeRecordelist, URL.ALLIANCE_BALANCE_VIEW, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<TradeRecordDetailsBean> tradeRecordeBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<TradeRecordDetailsBean>>() {
                });
                if (tradeRecordeBeanHttpResponse.isSuccess()) {
                    dealUI(tradeRecordeBeanHttpResponse);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    private void dealUI(HttpResponse<TradeRecordDetailsBean> tradeRecordeBeanHttpResponse) {
        TradeRecordDetailsBean data = tradeRecordeBeanHttpResponse.getData();
        ((TextView)linearLayout1.getChildAt(0)).setText(data.getCreated_at());
        ((TextView)linearLayout1.getChildAt(1)).setText(data.getStatus_label());
        ((TextView)linearLayout2.getChildAt(0)).setText("产品");
        ((TextView)linearLayout2.getChildAt(1)).setText(data.getProduct().getShort_title());
        ((TextView)linearLayout3.getChildAt(0)).setText("单价");
        ((TextView)linearLayout3.getChildAt(1)).setText(String.format("¥ %s", data.getSku_price()));
        ((TextView)linearLayout4.getChildAt(0)).setText("收益比率");
        ((TextView)linearLayout4.getChildAt(1)).setText(String.valueOf(Double.valueOf(data.getCommision_percent()) * 100D) + "%");
        ((TextView)linearLayout5.getChildAt(0)).setText("佣金");
        ((TextView)linearLayout5.getChildAt(1)).setText(String.format("¥ %s", data.getUnit_price()));
        ((TextView)linearLayout6.getChildAt(0)).setText("数量");
        ((TextView)linearLayout6.getChildAt(1)).setText(data.getQuantity());
        ((TextView)linearLayout7.getChildAt(0)).setText("分成收益");
        ((TextView)linearLayout7.getChildAt(1)).setText(String.format("¥ %s", data.getTotal_price()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}

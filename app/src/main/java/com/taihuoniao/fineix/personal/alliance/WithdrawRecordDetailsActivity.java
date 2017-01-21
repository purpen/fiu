package com.taihuoniao.fineix.personal.alliance;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.WithdrawRecordDetailsBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 2017/1/20 9:40
 * Email: 895745843@qq.com
 */

public class WithdrawRecordDetailsActivity extends BaseActivity {
    private String id;
    private String amount;

    public WithdrawRecordDetailsActivity() {
        super(R.layout.activity_alliance_withdraw_record_details);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.custom_head);
        customHead.setHeadCenterTxtShow(true, "提现详情");
        WindowUtils.chenjin(this);

        id = getIntent().getStringExtra("id");
        amount = getIntent().getStringExtra("amount");
    }


    @Override
    protected void requestNet() {
        requestDataList();
    }

    private void requestDataList(){
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getWithdrawRecordedetails(id, amount);
        HttpRequest.post(tradeRecordelist, URL.ALLIANCE_BALANCE_WITHDRAW_CASH_VIEW, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<WithdrawRecordDetailsBean > tradeRecordeBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<WithdrawRecordDetailsBean >>() {
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

    private void dealUI(HttpResponse<WithdrawRecordDetailsBean > tradeRecordeBeanHttpResponse){
//        List<TradeRecordBean.RowsEntity> rows = tradeRecordeBeanHttpResponse.getData().getRows();
    }
}

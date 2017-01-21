package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.adpter.SettlementRecordeDetailsAdapter;
import com.taihuoniao.fineix.personal.alliance.bean.SettlementRecordeDetailsBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/1/16 17:11
 * Email: 895745843@qq.com
 */

public class SettlementRecordDetailsActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.textView1)
    TextView textView1;
    @Bind(R.id.textView2)
    TextView textView2;
    @Bind(R.id.pullToRefreshListView_return)
    PullToRefreshListView pullToRefreshListViewReturn;
    @Bind(R.id.return_textView_empty)
    TextView returnTextViewEmpty;

    private SettlementRecordeDetailsAdapter   adapter;
    private String balanceId;

    public SettlementRecordDetailsActivity() {
        super(R.layout.activity_alliance_settlement_record_details);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "结算明细");
        WindowUtils.chenjin(this);
        initUI();

        balanceId = getIntent().getStringExtra("balanceId");
        String balance = getIntent().getStringExtra("balance");
        textView1.setText("总结算收益");
        textView2.setText(String.format("¥ %s", balance));
    }

    private void initUI() {
        ListView listView_show = pullToRefreshListViewReturn.getRefreshableView();
        listView_show.setCacheColorHint(0);

        //下拉监听
        pullToRefreshListViewReturn.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                requestNet();
            }
        });
        // 设置上拉加载下一页
        pullToRefreshListViewReturn.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (isLoadMore) {
                    curPage++;
                    requestNet();
                } else {
                    isLoadMore = true;
                }
            }
        });
        adapter = new SettlementRecordeDetailsAdapter(this);
        listView_show.setAdapter(adapter);
    }


    @Override
    protected void requestNet() {
        requestDataList();
    }

    private void requestDataList() {
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getSettlementRecordedetails(String.valueOf(curPage), "50", "0", balanceId);
        HttpRequest.post(tradeRecordelist, URL.ALLIANCE_BALANCE_RECORD_ITEM, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                pullToRefreshListViewReturn.onRefreshComplete();
                HttpResponse<SettlementRecordeDetailsBean> tradeRecordeBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SettlementRecordeDetailsBean>>() {
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

    private void dealUI(HttpResponse<SettlementRecordeDetailsBean> tradeRecordeBeanHttpResponse) {
        SettlementRecordeDetailsBean data = tradeRecordeBeanHttpResponse.getData();
        if (data != null && data.getRows() != null) {
            List<SettlementRecordeDetailsBean.RowsEntity> rows = data.getRows();
            adapter.setList(rows);
            returnTextViewEmpty.setVisibility(View.GONE);
        } else {
            returnTextViewEmpty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private int curPage = 1;
    private boolean isLoadMore;
}

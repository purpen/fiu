package com.taihuoniao.fineix.personal.alliance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.adpter.TradeRecordeAdapter;
import com.taihuoniao.fineix.personal.alliance.bean.TradeRecordBean;
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
 * Created by Stephen on 2017/1/16 17:09
 * Email: 895745843@qq.com
 */

public class TradeRecordActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.pullToRefreshListView_return)
    PullToRefreshListView pullToRefreshListViewReturn;
    @Bind(R.id.return_textView_empty)
    TextView returnTextViewEmpty;

    private TradeRecordeAdapter adapter;

    public TradeRecordActivity() {
        super(R.layout.activity_alliance_trade_record);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "交易记录");
        WindowUtils.chenjin(this);
        
        initUI();
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

        adapter = new TradeRecordeAdapter(this);
        listView_show.setAdapter(adapter);
        listView_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TradeRecordActivity.this, TradeRecordeDetailsActivity .class);
                String id1 = ((TradeRecordBean.RowsEntity) adapter.getItem(position)).get_id();
                intent.putExtra("_id", id1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void requestNet() {
        requestDataList();
    }

    private void requestDataList(){
        HashMap<String, String> tradeRecordelist = ClientDiscoverAPI.getTradeRecordelist(String.valueOf(curPage), "8", "0");
        HttpRequest.post(tradeRecordelist, URL.ALLIANCE_BALANCE_LIST, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                pullToRefreshListViewReturn.onRefreshComplete();
                HttpResponse<TradeRecordBean> tradeRecordeBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<TradeRecordBean>>() {
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

    private void dealUI(HttpResponse<TradeRecordBean> tradeRecordeBeanHttpResponse){
        List<TradeRecordBean.RowsEntity> rows = tradeRecordeBeanHttpResponse.getData().getRows();
        adapter.setList(rows);
    }

    private int curPage = 1;
    private boolean isLoadMore;
}

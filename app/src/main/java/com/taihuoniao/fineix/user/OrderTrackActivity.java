package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.Base2Activity;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.adapter.OrderTrackAdapter;
import com.taihuoniao.fineix.user.bean.OrderTrackBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.List;

/**
 * Created by Stephen on 2016/12/8 17:45
 * Email: 895745843@qq.com
 */

public class OrderTrackActivity extends BaseActivity {

    private OrderTrackAdapter orderTrackAdapter;

    public OrderTrackActivity() {
        super(R.layout.activity_order_track);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.title_order_tracking);
        customHead.setHeadCenterTxtShow(true, App.getString(R.string.title_orderTracking));
        WindowUtils.chenjin(this);

        Intent intent = getIntent();
        String rid = intent.getStringExtra("rid");
        String express_no = intent.getStringExtra("express_no");
        String express_caty = intent.getStringExtra("express_caty");
        requestOrderTrackInfo(rid, express_no, express_caty);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_express_info, recyclerView, false);
        orderTrackAdapter = new OrderTrackAdapter(OrderTrackActivity.this);
        orderTrackAdapter.setHeaderView(linearLayout);
        recyclerView.setAdapter(orderTrackAdapter);

        textShipperCode = (TextView) linearLayout.findViewById(R.id.text_ShipperCode);
        textLogisticCode = (TextView) linearLayout.findViewById(R.id.text_LogisticCode);
    }


    private void requestOrderTrackInfo(String s1, String s2, String s3) {
        ClientDiscoverAPI.shoppingTracking(s1, s2, s3, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }
                HttpResponse<OrderTrackBean> orderTrackBeanHttpResponse = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<OrderTrackBean>>() {
                });

                if (orderTrackBeanHttpResponse.isError()) {
                    ToastUtils.showError(orderTrackBeanHttpResponse.getMessage());
                    return;
                }

                textShipperCode.setText(String.format(App.getString(R.string.text_order_track_shipperCode), orderTrackBeanHttpResponse.getData().getShipperCode()));
                textLogisticCode.setText(String.format(App.getString(R.string.text_order_track_logisticCode), orderTrackBeanHttpResponse.getData().getLogisticCode()));

                List<OrderTrackBean.TracesEntity> traces = orderTrackBeanHttpResponse.getData().getTraces();

                if (traces != null) {
                    orderTrackAdapter.putAll(traces);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private TextView textShipperCode;
    private TextView textLogisticCode;
}

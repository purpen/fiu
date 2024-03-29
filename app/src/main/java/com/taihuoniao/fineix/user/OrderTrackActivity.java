package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.adapter.OrderTrackAdapter;
import com.taihuoniao.fineix.user.bean.OrderTrackBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Stephen on 2016/12/8 17:45
 * Email: 895745843@qq.com
 */

public class OrderTrackActivity extends BaseActivity {

    private WaittingDialog mDialog;
    private OrderTrackAdapter orderTrackAdapter;
    private String express_company;
    private TextView textShipperCode;
    private TextView textLogisticCode;

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
        express_company = intent.getStringExtra("express_company");
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
        if (mDialog == null) {
            mDialog = new WaittingDialog(this);
            mDialog.show();
        }
        HashMap<String, String> params = ClientDiscoverAPI.getShoppingTrackingRequestParams(s1, s2, s3);
        HttpRequest.post(params,URL.SHOPPING_TRACKING, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
                HttpResponse<OrderTrackBean> orderTrackBeanHttpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<OrderTrackBean>>() { });
                if (orderTrackBeanHttpResponse.isError()) {
                    ToastUtils.showError(orderTrackBeanHttpResponse.getMessage());
                    return;
                }
                textShipperCode.setText(String.format(App.getString(R.string.text_order_track_shipperCode), express_company));
                textLogisticCode.setText(String.format(App.getString(R.string.text_order_track_logisticCode), orderTrackBeanHttpResponse.getData().getLogisticCode()));
                List<OrderTrackBean.TracesEntity> traces = orderTrackBeanHttpResponse.getData().getTraces();
                if (traces != null) {
                    List<OrderTrackBean.TracesEntity> newTraces = new ArrayList<>();
                    for(int i = traces.size() - 1; i>= 0; i--) {
                        newTraces.add(traces.get(i));
                    }
                    orderTrackAdapter.putAll(newTraces);
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}

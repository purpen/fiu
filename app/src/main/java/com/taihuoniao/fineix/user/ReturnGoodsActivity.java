package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ShopOrderListAdapter;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.beans.OrderItem;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ActivityUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//
public class ReturnGoodsActivity extends Activity {
    private MyGlobalTitleLayout title = null;
    private PullToRefreshListView pullToRefreshListView;
    private ProgressBar progressBar;
    private ShopOrderListAdapter mAdapter;
    private List<OrderEntity> mList = new ArrayList<>();
    private String status = "8";
    private WaittingDialog mDialog;
    private int curPage = 1;
    private int size = 10;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case DataConstants.PARSER_ORDER:
//                    if (msg.obj != null) {
//                        if (msg.obj instanceof List) {
//                            if (curPage == 1) {
//                                mList.clear();
//                                pullToRefreshListView.lastTotalItem = -1;
//                                pullToRefreshListView.lastSavedFirstVisibleItem = -1;
//                            }
//                            mList.addAll((Collection<? extends OrderEntity>) msg.obj);
//                            progressBar.setVisibility(View.GONE);
//                            pullToRefreshListView.onRefreshComplete();
//                            pullToRefreshListView.setLoadingTime();
//
//                            mAdapter.notifyDataSetChanged();
//                            mDialog.dismiss();
//                        }
//                    }
//                    break;
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        StatusBarChange.initWindow(this);
        setContentView(R.layout.activity_return_goods);
        iniView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (curPage == 1) {
            mDialog.show();
        }
        mAdapter.notifyDataSetChanged();
        orderListParser(status, curPage + "", size + "");
    }

    private void orderListParser(String status, String page, String size) {
        ClientDiscoverAPI.orderListNet(status, page, size, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                List<OrderEntity> list = new ArrayList<>();
                try {
                    JSONObject obj = new JSONObject(responseInfo.result);
                    JSONObject orderObj = obj.getJSONObject("data");
                    JSONArray orderArrs = orderObj.getJSONArray("rows");
                    for (int i = 0; i < orderArrs.length(); i++) {
                        JSONObject orderArr = orderArrs.getJSONObject(i);
                        OrderEntity orderEntity = new OrderEntity();
                        orderEntity.setRid(orderArr.optString("rid"));
                        orderEntity.setItems_count(orderArr.optString("items_count"));
                        orderEntity.setTotal_money(orderArr.optString("total_money"));
                        orderEntity.setPay_money(orderArr.optString("pay_money"));
                        orderEntity.setFreight(orderArr.optString("freight"));
                        orderEntity.setStatus_label(orderArr.optString("status_label"));
                        orderEntity.setCreated_at(orderArr.optString("created_at"));
                        orderEntity.setStatus(orderArr.optString("status"));
                        JSONArray array = orderArr.getJSONArray("items");
                        List<OrderItem> itemList = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            JSONObject arr = array.getJSONObject(j);
                            OrderItem item = new OrderItem();
                            item.setSku(arr.optString("sku"));
                            item.setProduct_id(arr.optString("product_id"));
                            item.setQuantity(arr.optString("quantity"));
                            item.setSale_price(arr.optString("sale_price"));
                            item.setName(arr.optString("name"));
                            item.setSku_name(arr.optString("sku_name"));
                            item.setCover_url(arr.optString("cover_url"));
                            itemList.add(item);
                        }
                        orderEntity.setOrderItem(itemList);
                        list.add(orderEntity);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (curPage == 1) {
                    mList.clear();
                    pullToRefreshListView.lastTotalItem = -1;
                    pullToRefreshListView.lastSavedFirstVisibleItem = -1;
                }
                mList.addAll(list);
                progressBar.setVisibility(View.GONE);
                pullToRefreshListView.onRefreshComplete();
                pullToRefreshListView.setLoadingTime();

                mAdapter.notifyDataSetChanged();
                mDialog.dismiss();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressBar.setVisibility(View.GONE);
                mDialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void iniView() {
        ActivityUtil.getInstance().addActivity(this);
        title = (MyGlobalTitleLayout) findViewById(R.id.title_return);
        title.setTitle("退款/售后");
        title.setBackgroundResource(R.color.white);
        title.setBackImg(R.mipmap.back_black);
        title.setRightSearchButton(false);
        title.setRightShopCartButton(false);
        title.setTitleColor(getResources().getColor(R.color.black333333));
        title.setBackButtonVisibility(true);
        mDialog = new WaittingDialog(this);
        if (curPage == 1) {
            mDialog.show();
        }
        pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView_return);
        progressBar = (ProgressBar) findViewById(R.id.return_progressBar);
        TextView textView_empty = (TextView) findViewById(R.id.return_textView_empty);
        ListView listView_show = pullToRefreshListView.getRefreshableView();
        listView_show.setEmptyView(textView_empty);
        listView_show.setCacheColorHint(0);
        //下拉监听
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //页码从新开始
                curPage = 1;
                //开始刷新
                orderListParser(status, curPage + "", size + "");

            }
        });
        // 设置上拉加载下一页
        pullToRefreshListView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                curPage++;
                orderListParser(status, curPage + "", size + "");
            }
        });

        mAdapter = new ShopOrderListAdapter(mList, this, status);
        listView_show.setAdapter(mAdapter);
        // 加载网络数据，刷新ListView
        progressBar.setVisibility(View.VISIBLE);
    }

}

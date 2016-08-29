package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SelectAddressListViewAdapter;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/2.
 */
public class SelectAddressActivity extends Activity implements View.OnClickListener {
    //控件
    private GlobalTitleLayout titleLayout;
    private LinearLayout addNewAddressTv;
    private LinearLayout emptyView;
    private PullToRefreshListView pullToRefresh;
    private ListView listView;
    private ProgressBar progressBar;
    private List<AddressListBean.AddressListItem> list = new ArrayList<>();
    private SelectAddressListViewAdapter listViewAdapter;
    //网络请求
    private int currentPage = 1;
    private WaittingDialog dialog;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initView();
        WindowUtils.chenjin(this);
        setData();
        if (!dialog.isShowing()) {
            dialog.show();
        }
        getAddressList(currentPage + "");
    }


    private void setData() {
        titleLayout.setTitle("收货地址");
        titleLayout.setContinueTvVisible(false);
        addNewAddressTv.setOnClickListener(this);
        pullToRefresh.setPullToRefreshEnabled(false);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listViewAdapter = new SelectAddressListViewAdapter(SelectAddressActivity.this, list, dm.widthPixels, SelectAddressActivity.this);
        listView.setAdapter(listViewAdapter);
        pullToRefresh.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0
                        && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
                    if (firstVisibleItem != lastSavedFirstVisibleItem && totalItemCount != lastTotalItem) {
                        lastSavedFirstVisibleItem = firstVisibleItem;
                        lastTotalItem = totalItemCount;
                        progressBar.setVisibility(View.VISIBLE);
                        currentPage++;
                        getAddressList(currentPage + "");
                    }
                }
            }
        });
    }

    private void initView() {
        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_select_address_title);
        addNewAddressTv = (LinearLayout) findViewById(R.id.activity_select_address_addnewaddresstv);
        emptyView = (LinearLayout) findViewById(R.id.activity_select_address_emptylinear);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.activity_select_address_listview);
        listView = pullToRefresh.getRefreshableView();
        progressBar = (ProgressBar) findViewById(R.id.activity_select_address_progress);
        //listivew侧滑删除效果的实现
        dialog = new WaittingDialog(SelectAddressActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_address_addnewaddresstv:
                //添加新地址
                Intent intent = new Intent(SelectAddressActivity.this, AddNewAddressActivity.class);
                startActivityForResult(intent, DataConstants.REQUESTCODE_ADDNEWADDRESS);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case DataConstants.RESULTCODE_ADDNEWADDRESS:
                int result = data.getIntExtra("address", 0);
                if (result == 1) {
                    list.clear();
                    currentPage = 1;
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    getAddressList(currentPage + "");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isSelect()) {
                intent.putExtra("addressBean", list.get(i));
                setResult(DataConstants.RESULTCODE_ADDRESS, intent);
                break;
            }
        }
        setResult(DataConstants.RESULTCODE_ADDRESS, intent);
        super.onBackPressed();
    }

    public void deleteAddress(String id) {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.deleteAddressNet(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (netBean.isSuccess()) {
                    ToastUtils.showSuccess("删除成功");
                    currentPage = 1;
                    getAddressList(currentPage + "");
                } else {
                    dialog.dismiss();
                    ToastUtils.showError(netBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    //获得收货地址列表
    private void getAddressList(String page) {
        ClientDiscoverAPI.getAddressList(page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                AddressListBean addressListBean = new AddressListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<AddressListBean>() {
                    }.getType();
                    addressListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<收货地址列表", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                AddressListBean netAddressList = addressListBean;
                if (netAddressList.isSuccess()) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(netAddressList.getData().getRows());
                    if (list.size() <= 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netAddressList.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }
}

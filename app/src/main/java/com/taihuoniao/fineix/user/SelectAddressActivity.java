package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.AddressListBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.fragments.AddressAPIChangeFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * created at 2016/10/25 17:57
 */
public class SelectAddressActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    private LinearLayout addNewAddressTv;
    private LinearLayout emptyView;
    private PullToRefreshListView pullToRefresh;
    private ListView listView;
    private ProgressBar progressBar;
    private List<AddressListBean.RowsEntity> list = new ArrayList<>();
    private SelectAddressListViewAdapter listViewAdapter;
    //网络请求
    private int currentPage = 1;
    private WaittingDialog dialog;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    public SelectAddressActivity() {
        super(R.layout.activity_select_address);
    }

    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.title_consignee_address);
        addNewAddressTv = (LinearLayout) findViewById(R.id.activity_select_address_addnewaddresstv);
        emptyView = (LinearLayout) findViewById(R.id.activity_select_address_emptylinear);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.activity_select_address_listview);
        listView = pullToRefresh.getRefreshableView();
        progressBar = (ProgressBar) findViewById(R.id.activity_select_address_progress);
        dialog = new WaittingDialog(this);
    }

    @Override
    protected void requestNet() {
        getAddressList(currentPage + "");
    }

    @Override
    protected void installListener() {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_address_addnewaddresstv:
                //添加新地址
                Intent intent = new Intent(SelectAddressActivity.this, AddAddressActivity.class);
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
            if (list.get(i).isSelected) {
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
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    //获得收货地址列表
    private void getAddressList(String page) {
        ClientDiscoverAPI.getAddressList(page, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                HttpResponse<AddressListBean> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<AddressListBean>>() {
                });
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (response.isSuccess()) {
                    List<AddressListBean.RowsEntity> rows = response.getData().rows;
                    try {
                        if (!SPUtil.readBool(TAG) && rows.size()== 0 && TextUtils.equals("1.1.2", Util.getVersionName())) {
                            SPUtil.write(TAG,true);
                            new AddressAPIChangeFragment().show(getFragmentManager(),AddressAPIChangeFragment.class.getSimpleName());
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(rows);
                    if (list.size() <= 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(response.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}

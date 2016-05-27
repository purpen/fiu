package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SelectAddressListViewAdapter;
import com.taihuoniao.fineix.beans.AddressBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.NetworkManager;
import com.taihuoniao.fineix.view.MyGlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/2/2.
 */
public class SelectAddressActivity extends Activity implements View.OnClickListener {
    //控件
    private MyGlobalTitleLayout titleLayout;
    private TextView addNewAddressTv;
    private LinearLayout emptyView;
    private PullToRefreshListView pullToRefresh;
    private ListView listView;
    private List<AddressBean> list = new ArrayList();
    private SelectAddressListViewAdapter listViewAdapter;
    //网络请求
    private int currentPage = 1;
    private SVProgressHUD dialog;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);
        initView();
        setData();
        dialog.show();
        DataPaser.getAddressList(currentPage + "", mHandler);
    }

    @Override
    protected void onDestroy() {
        cancelNet();
        super.onDestroy();
    }

    private void setData() {
        titleLayout.setTitle("选择收货地址");
        titleLayout.setTitleColor(getResources().getColor(R.color.black333333));
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setRightShopCartButton(false);
        titleLayout.setRightSearchButton(false);
        titleLayout.setBackButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()) {
                        intent.putExtra("addressBean", list.get(i));
                        setResult(DataConstants.RESULTCODE_ADDRESS, intent);
                        break;
                    }
                }
                setResult(DataConstants.RESULTCODE_ADDRESS, intent);
                finish();
            }
        });
        addNewAddressTv.setOnClickListener(this);
        pullToRefresh.setPullToRefreshEnabled(false);
//        listView.setEmptyView(emptyView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        listViewAdapter = new SelectAddressListViewAdapter(SelectAddressActivity.this, list, dm.widthPixels, SelectAddressActivity.this, mHandler);
        listView.setAdapter(listViewAdapter);
        pullToRefresh.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (visibleItemCount > 0
                        && (firstVisibleItem + visibleItemCount == totalItemCount)) {
                    if (firstVisibleItem != lastSavedFirstVisibleItem && totalItemCount != lastTotalItem) {
                        lastSavedFirstVisibleItem = firstVisibleItem;
                        lastTotalItem = totalItemCount;
                        currentPage++;
                        DataPaser.getAddressList(currentPage + "", mHandler);
                    }
                }
            }
        });
    }

    private void initView() {
        titleLayout = (MyGlobalTitleLayout) findViewById(R.id.activity_select_address_title);
        addNewAddressTv = (TextView) findViewById(R.id.activity_select_address_addnewaddresstv);
        emptyView = (LinearLayout) findViewById(R.id.activity_select_address_emptylinear);
        pullToRefresh = (PullToRefreshListView) findViewById(R.id.activity_select_address_listview);
        listView = pullToRefresh.getRefreshableView();
        //listivew侧滑删除效果的实现
        dialog = new SVProgressHUD(SelectAddressActivity.this);
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
                    dialog.show();
                    DataPaser.getAddressList(currentPage + "", mHandler);
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.DELETE_ADDRESS:
                    boolean success = (boolean) msg.obj;
                    if (success) {
                        Toast.makeText(SelectAddressActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        list.clear();
                        currentPage = 1;
                        dialog.show();
                        DataPaser.getAddressList(currentPage + "", mHandler);
                    }
                    break;
                case DataConstants.GET_ADDRESS_LIST:
                    dialog.dismiss();
                    List<AddressBean> list1 = (List<AddressBean>) msg.obj;
                    list.addAll(list1);
                    if(list1.size()<=0){
                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.GONE);
                    }
                    listViewAdapter.notifyDataSetChanged();
                    break;
                case DataConstants.NETWORK_FAILURE:
                    dialog.dismiss();
                    Toast.makeText(SelectAddressActivity.this, R.string.host_failure, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void cancelNet() {
        NetworkManager.getInstance().cancel("getAddressList");
    }

}

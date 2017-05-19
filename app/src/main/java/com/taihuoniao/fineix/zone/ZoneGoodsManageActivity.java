package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.adapter.ZoneManageGoodsAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;
import com.taihuoniao.fineix.zone.bean.ZoneManageGoodsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


/**
 * Created by lilin on 2017/5/17.
 */

public class ZoneGoodsManageActivity extends BaseActivity implements View.OnClickListener{
    public static final int REQUEST_NEW_PRODUCT = 100;
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    private ZoneDetailBean zoneDetailBean;
    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.emptyView)
    TextView emptyView;
    private ZoneManageGoodsAdapter adapter;
    private List<ZoneManageGoodsBean.RowsBean> mList;
    private WaittingDialog dialog;
    private int curPage = 1;
    private boolean isLoadMore;
    public ZoneGoodsManageActivity() {
        super(R.layout.activity_zone_goods_manage);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent!=null){
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        dialog=new WaittingDialog(activity);
        customHead.setHeadCenterTxtShow(true,R.string.zone_goods_manage);
        customHead.setHeadRightTxtShow(true,R.string.add_new_product);
    }

    @Override
    protected void initList() {
        mList = new ArrayList<>();
        adapter = new ZoneManageGoodsAdapter(activity,mList);
        listView.setAdapter(adapter);
    }





    @Override
    protected void installListener() {
        customHead.getHeadRightTV().setOnClickListener(this);
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore =true;
                requestNet();
            }
        });
    }

    private void refreshData(){
        curPage = 1;
        isLoadMore = true;
        requestNet();
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_head_right:
                intent = new Intent(activity, ZoneGoodsPopularizeActivity.class);
                intent.putExtra(ZoneGoodsPopularizeActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent,REQUEST_NEW_PRODUCT);
                break;
            default:
                break;
        }
    }



    @Override
    protected void requestNet() {
        if (null==zoneDetailBean) return;
        HashMap<String, String> params = ClientDiscoverAPI.getZoneManageProductsParams(zoneDetailBean._id,String.valueOf(curPage));
        HttpRequest.post(params, URL.ZONE_MANAGE_PRODUCTS_LIST_URL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                listView.onRefreshComplete();
                HttpResponse<ZoneManageGoodsBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneManageGoodsBean>>() {
                });
                if (curPage == 1 && mList.size()>0){
                    mList.clear();
                }
                if (response.isSuccess()){
                    mList.addAll(response.getData().rows);
                    refreshUI();
                    curPage++;
                }else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                ToastUtils.showInfo(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (adapter == null) {
            adapter = new ZoneManageGoodsAdapter(activity, mList);
        } else {
            adapter.notifyDataSetChanged();
        }
        listView.setEmptyView(emptyView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK) return;
        if (requestCode == REQUEST_NEW_PRODUCT){
            refreshData();
        }
    }
}

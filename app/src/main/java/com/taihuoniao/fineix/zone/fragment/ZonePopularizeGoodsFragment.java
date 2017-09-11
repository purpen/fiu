package com.taihuoniao.fineix.zone.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.adapter.ZonePopularizeGoodsClassifyAdapter;
import com.taihuoniao.fineix.zone.bean.ZonePopularizeProductsBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


/**
 * Created by lilin on 2017/5/18.
 */

public class ZonePopularizeGoodsFragment extends MyBaseFragment {
    @Bind(R.id.listView)
    PullToRefreshListView listView;
    @Bind(R.id.emptyView)
    TextView emptyView;
    private CategoryListBean.RowsEntity item;
    private int curPage = 1;
    private WaittingDialog dialog;
    private boolean isFirstLoad = true;
    private String currentZoneId;
    private List<ZonePopularizeProductsBean.RowsBean> mList;
    private ZonePopularizeGoodsClassifyAdapter adapter;

    public ZonePopularizeGoodsFragment() {

    }


    public static ZonePopularizeGoodsFragment newInstance(CategoryListBean.RowsEntity param,String currentZoneId) {
        ZonePopularizeGoodsFragment fragment = new ZonePopularizeGoodsFragment();
        fragment.item = param;
        fragment.currentZoneId = currentZoneId;
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint() && isFirstLoad) {
            requestNet();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_popularize_products);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //如果是全部id = 0
        if (TextUtils.equals(item.get_id(),"0")){
            requestNet();
        }

    }

    @Override
    protected void initParams() {
        dialog = new WaittingDialog(activity);
        mList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        adapter = new ZonePopularizeGoodsClassifyAdapter(activity, mList,currentZoneId);
        listView.setAdapter(adapter);
    }

    @Override
    protected void installListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage = 1;
                isFirstLoad = false;
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        listView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isFirstLoad = false;
                requestNet();
            }
        });
    }


    private void requestNet() {
        if (item ==null) return;
        HashMap<String, String> params = ClientDiscoverAPI.getPopularizeProductsByCategoryId(String.valueOf(curPage), item.get_id(), "1");
        HttpRequest.post(params, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (isFirstLoad && dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                listView.onRefreshComplete();
                HttpResponse<ZonePopularizeProductsBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZonePopularizeProductsBean>>() {
                });
                if (curPage == 1 && mList.size() > 0) {
                    mList.clear();
                }
                if (response.isSuccess()) {
                    isFirstLoad = false;
                    mList.addAll(response.getData().rows);
                    refreshUIAfterNet();
                    curPage++;
                } else {
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
    protected void refreshUIAfterNet() {
        if (null == adapter) {
            adapter = new ZonePopularizeGoodsClassifyAdapter(activity, mList,currentZoneId);
        } else {
            adapter.notifyDataSetChanged();
        }
        listView.setEmptyView(emptyView);
    }
}

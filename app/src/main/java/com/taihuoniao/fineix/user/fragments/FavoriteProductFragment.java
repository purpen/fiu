package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FavoriteProductGVAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DataProductCollect;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemProductCollect;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class FavoriteProductFragment extends MyBaseFragment {
    public static final String PAGE_TYPE = "1"; //1. 情境产品；11.地盘；12.情境；13.情境专题；
    public static final String PAGE_EVENT = "1";
    public int curPage = 1;
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pullGv;
    private ArrayList<ItemProductCollect> mList;
    private FavoriteProductGVAdapter adapter;
    private WaittingDialog dialog;
    private boolean isLoadMore = false;

    public static FavoriteProductFragment newInstance() {
        return new FavoriteProductFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_favorite_product);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        pullGv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mList = new ArrayList<>();
        dialog = new WaittingDialog(activity);
    }

    @Override
    protected void installListener() {
        pullGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                isLoadMore = true;
                curPage = 1;
                mList.clear();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });

        pullGv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        pullGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null) {
                    ItemProductCollect itemProductCollect = mList.get(position);
                    Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                    intent.putExtra("id", String.valueOf(itemProductCollect.product._id));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        HashMap<String, String> params = ClientDiscoverAPI.getgetCollectOrderedRequestParams(String.valueOf(curPage), Constants.PAGE_SIZE, PAGE_TYPE, PAGE_EVENT);
        HttpRequest.post(params, URL.FAVORITE_GET_NEW_LIST, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getCollectOrdered(String.valueOf(curPage), Constants.PAGE_SIZE, PAGE_TYPE, PAGE_EVENT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;

                HttpResponse<DataProductCollect> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DataProductCollect>>() {
                });

                if (response.isSuccess()) {
                    pullGv.onRefreshComplete();
                    ArrayList<ItemProductCollect> list = response.getData().rows;
                    refreshUIAfterNet(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
                Util.makeToast(error);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet(ArrayList list) {
        if (list == null || list.size() == 0) return;
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new FavoriteProductGVAdapter(mList, activity);
            pullGv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

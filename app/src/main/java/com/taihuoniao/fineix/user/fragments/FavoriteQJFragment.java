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
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FavoriteQJGVAdapter;
import com.taihuoniao.fineix.beans.DataQJCollect;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemQJCollect;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.Constants;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class FavoriteQJFragment extends MyBaseFragment {
    public static final String PAGE_TYPE = "12"; //10. 情境产品；11.地盘；12.情境；13.情境专题；
    public static final String PAGE_EVENT = "1";
    public int curPage = 1;
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pullGv;
    private ArrayList<ItemQJCollect> mList;
    private FavoriteQJGVAdapter adapter;
    private WaittingDialog dialog;
    private boolean isLoadMore = false;

    public static FavoriteQJFragment newInstance() {
        return new FavoriteQJFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_favorite_qj);
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
                    ItemQJCollect itemQJCollect = mList.get(position);
                    Intent intent = new Intent(activity, QJDetailActivity.class);
                    intent.putExtra("id", String.valueOf(itemQJCollect.sight._id));
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        ClientDiscoverAPI.getCollectOrdered(String.valueOf(curPage), Constants.PAGE_SIZE, PAGE_TYPE, PAGE_EVENT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) return;

                HttpResponse<DataQJCollect> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<DataQJCollect>>() {
                });

                if (response.isSuccess()) {
                    pullGv.onRefreshComplete();
                    ArrayList<ItemQJCollect> list = response.getData().rows;
                    refreshUIAfterNet(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                e.printStackTrace();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet(ArrayList list) {
        if (list == null || list.size() == 0) return;
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new FavoriteQJGVAdapter(mList, activity);
            pullGv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

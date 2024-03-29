package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.QJCateogryAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/26.
 */
public class QJFragment extends SearchFragment {
    @Bind(R.id.pull_refresh_view)
    PullToRefreshGridView pullRefreshView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private WaittingDialog dialog;

    private int pos;//判断位置 0，精选 1，最新
    private int page = 1;
    private String id;//情景分类id
    private List<SceneListBean2.RowsEntity> sceneList;
    private QJCateogryAdapter qjCateogryAdapter;

    public static QJFragment newInstance(int position, String id) {
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putString("id", id);
        QJFragment fragment = new QJFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos", 0);
        id = getArguments().getString("id");
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_qj_category, null);
        ButterKnife.bind(this, view);
        GridView gridView = pullRefreshView.getRefreshableView();
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
            }
        });
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                requestNet();
            }
        });
        gridView.setNumColumns(2);
        gridView.setSelector(R.color.nothing);
        gridView.setVerticalSpacing(DensityUtils.dp2px(getActivity(), 15));
        gridView.setHorizontalSpacing(DensityUtils.dp2px(getActivity(), 15));
        dialog = new WaittingDialog(getActivity());
        sceneList = new ArrayList<>();
        qjCateogryAdapter = new QJCateogryAdapter(getActivity(),sceneList);
//        subsListAdapter = new SubsListAdapter(getActivity(), sceneList);
        gridView.setAdapter(qjCateogryAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return view;
    }

    @Override
    protected void requestNet() {
        getSubsQJ();
    }

    @Override
    public void refreshData(String q) {
        page = 1;
        if (!dialog.isShowing()) {
            dialog.show();
        }
        requestNet();
    }
    private Call sceneListHandler;
    //获取订阅的情景
    private void getSubsQJ() {
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(page + "", 8 + "", null, id, pos == 0 ? "2" : "0", null, null, null);
        sceneListHandler =  HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                HttpResponse<SceneListBean2> sceneL = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneListBean2>>() {});
                if (sceneL.isSuccess()) {
                    pullRefreshView.setLoadingTime();
                    if (page == 1) {
                        sceneList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getData().getRows());
                    qjCateogryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onDestroyView() {
        if(sceneListHandler!=null){
            sceneListHandler.cancel();
        }
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

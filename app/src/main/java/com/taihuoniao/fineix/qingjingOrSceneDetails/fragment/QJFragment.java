package com.taihuoniao.fineix.qingjingOrSceneDetails.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.QJCateogryAdapter;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
    private List<SceneList.DataBean.RowsBean> sceneList;
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
    private HttpHandler<String> sceneListHandler;
    //获取订阅的情景
    private void getSubsQJ() {
     sceneListHandler =    ClientDiscoverAPI.getSceneList(page + "", 8 + "", null, id, pos == 0 ? "2" : "0", null, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<情景列表", responseInfo.result);
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "情景列表解析异常" + e.toString());
                }
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
            public void onFailure(HttpException error, String msg) {
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

package com.taihuoniao.fineix.main.tab3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.IndexQJListAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/3/3 23:00
 * Email: 895745843@qq.com
 */

public class WellGoodsFragment02 extends BaseFragment implements PullToRefreshBase.OnRefreshListener, AbsListView.OnScrollListener {

    @Bind(R.id.pull_refresh_view_001)
    PullToRefreshListView pullRefreshView001;

    // 相关情境
    private ListView mListView;
    private List<SceneListBean2.RowsEntity> sceneList;
    private IndexQJListAdapter indexQJListAdapter;

    private int currentPage = 1;

    @Override
    protected void requestNet() {
        requestData002();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods_02, null);
        ButterKnife.bind(this, view);

        initListView();
//        mListView.addHeaderView(getHeaderView());

        return view;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        WindowUtils.chenjin(getActivity());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initListView(){
        mListView = pullRefreshView001.getRefreshableView();
//        mListView.addHeaderView(getHeadView());
        pullRefreshView001.animLayout();
        mListView.setDividerHeight(0);
        pullRefreshView001.setOnRefreshListener(this);
        mListView.setOnScrollListener(this);

        sceneList = new ArrayList<>();
        ArrayList<User> userList = new ArrayList<>();
        indexQJListAdapter = new IndexQJListAdapter(activity, sceneList, userList);
        mListView.setAdapter(indexQJListAdapter);
    }

    /**
     * 相关情境
     */
    private void requestData002(){
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(currentPage + "", 10 + "", null, null, 1 + "", null, null, null);
        re.put("is_product", "1");
        HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullRefreshView001.onRefreshComplete();
                refreshListView(json);
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    private void refreshListView(String json) {
        SceneListBean2 bean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<SceneListBean2>>() { });
        if (bean != null) {
            if (currentPage == 1) {
                sceneList.clear();
                pullRefreshView001.lastTotalItem = -1;
                pullRefreshView001.lastSavedFirstVisibleItem = -1;
            }
            List<SceneListBean2.RowsEntity> rows = bean.getRows();
//            List<SceneList.DataBean.RowsBean> rowsBeen = Tools.newListConvertOldList(rows);
            sceneList.addAll(rows);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    indexQJListAdapter.notifyDataSetChanged();
                }
            }, 200);
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        requestNet();
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > mListView.getHeaderViewsCount()
                && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
            if (firstVisibleItem != pullRefreshView001.lastSavedFirstVisibleItem && pullRefreshView001.lastTotalItem != totalItemCount) {
                pullRefreshView001.lastSavedFirstVisibleItem = firstVisibleItem;
                pullRefreshView001.lastTotalItem = totalItemCount;
                currentPage++;
                requestData002();
            }
        }
    }
}

package com.taihuoniao.fineix.home;

import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Stephen on 2017/3/14 16:56
 * Email: 895745843@qq.com
 */

public class MoreWellGoodsActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.pullToRefreshListView_wellGoods_more)
    PullToRefreshListView pullToRefreshListViewWellGoodsMore;
    private ListView mListView;

    private List<SubjectListBean.RowsEntity> subjectList;
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;
    private int currentPage = 1;

    public MoreWellGoodsActivity() {
        super(R.layout.activity_wellgoods_more);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "好货合集");
        WindowUtils.chenjin(this);
        initListView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void requestNet() {
        subjectList();
    }

    //好货专题列表
    private void subjectList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", null, null, 5 + "", "2");
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                pullToRefreshListViewWellGoodsMore.onRefreshComplete();
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                if (subjectListBean.isSuccess()) {
                    if (currentPage == 1) {
                        pullToRefreshListViewWellGoodsMore.lastTotalItem = -1;
                        pullToRefreshListViewWellGoodsMore.lastSavedFirstVisibleItem = -1;
                        subjectList.clear();
                    }
                    subjectList.addAll(subjectListBean.getData().getRows());
                    wellgoodsSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                pullToRefreshListViewWellGoodsMore.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    private void initListView() {
        pullToRefreshListViewWellGoodsMore.animLayout();
        mListView = pullToRefreshListViewWellGoodsMore.getRefreshableView();
        mListView.setSelector(R.color.nothing);
        mListView.setDividerHeight(0);
        subjectList = new ArrayList<>();
        wellgoodsSubjectAdapter = new WellgoodsSubjectAdapter(this, subjectList);
        mListView.setAdapter(wellgoodsSubjectAdapter);
        pullToRefreshListViewWellGoodsMore.animLayout();
        mListView = pullToRefreshListViewWellGoodsMore.getRefreshableView();
        pullToRefreshListViewWellGoodsMore.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                subjectList();
            }
        });
        pullToRefreshListViewWellGoodsMore.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                subjectList();
            }
        });
    }
}

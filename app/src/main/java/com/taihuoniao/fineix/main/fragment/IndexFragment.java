package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.IndexQJListAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SubsCJListActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/9.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener, AbsListView.OnScrollListener {
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    private ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.emptyview)
    TextView emptyview;
    @Bind(R.id.search_img)
    ImageView searchImg;
    @Bind(R.id.subs_img)
    ImageView subsImg;

    private ScrollableView scrollableView;
    private ImageView moreThemeImg;
    private RecyclerView recyclerView;
    private ImageView moreQjImg;
    private WaittingDialog dialog;//网络请求对话框
    private int currentPage = 1;//网络请求页码
    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据
    private IndexQJListAdapter indexQJListAdapter;//情景列表适配器


    @Override
    protected View initView() {
        View fragmentView = View.inflate(getActivity(), R.layout.fragment_index, null);
        dialog = new WaittingDialog(getActivity());
        return fragmentView;
    }

    @Override
    protected void initList() {
        listView = pullRefreshView.getRefreshableView();
        View headerView = View.inflate(getActivity(), R.layout.header_index, null);
        scrollableView = (ScrollableView) headerView.findViewById(R.id.scrollableView);
        moreThemeImg = (ImageView) headerView.findViewById(R.id.more_theme_img);
        recyclerView = (RecyclerView) headerView.findViewById(R.id.recycler_view);
        moreQjImg = (ImageView) headerView.findViewById(R.id.more_qj_img);
        listView.addHeaderView(headerView);
        pullRefreshView.animLayout();
        listView.setDividerHeight(0);
        searchImg.setOnClickListener(this);
        subsImg.setOnClickListener(this);
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
            }
        });
        listView.setOnScrollListener(this);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);
        moreThemeImg.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //设置适配器
        moreQjImg.setOnClickListener(this);
        sceneList = new ArrayList<>();
        indexQJListAdapter = new IndexQJListAdapter(sceneList);
        listView.setAdapter(indexQJListAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    protected void requestNet() {
        sceneNet();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_qj_img:
                ToastUtils.showError("更多情景");
                break;
            case R.id.more_theme_img:
                ToastUtils.showError("更多主题");
                break;
            case R.id.search_img:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", 9 + "");
                startActivity(intent);
                break;
            case R.id.subs_img:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), SubsCJListActivity.class));
                break;
        }
    }

    //获取情景列表
    private void sceneNet() {
        ClientDiscoverAPI.getSceneList(currentPage + "", 8 + "", null, 2 + "", 1 + "", null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<情景列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "情景列表解析异常"+e.toString());
                }
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (sceneL.isSuccess()) {
//                    pullToRefreshLayout.setLoadingTime();
                    if (currentPage == 1) {
                        sceneList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getData().getRows());
                    indexQJListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    private int scrollState;
    private int lastFirst = -1;
    private int lastTotal = -1;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (scrollState == SCROLL_STATE_IDLE && visibleItemCount > listView.getHeaderViewsCount() &&
                (firstVisibleItem + visibleItemCount >= totalItemCount) && lastFirst != firstVisibleItem &&
                lastTotal != totalItemCount) {
            progressBar.setVisibility(View.VISIBLE);
            currentPage++;
            sceneNet();
        }
    }
}

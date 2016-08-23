package com.taihuoniao.fineix.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.IndexQJListAdapter;
import com.taihuoniao.fineix.adapters.IndexSubjectAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SubsCJListActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
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
public class IndexFragment extends BaseFragment<Banner> implements View.OnClickListener, PullToRefreshBase.OnRefreshListener, AbsListView.OnScrollListener, EditRecyclerAdapter.ItemClick {
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
    private ViewPagerAdapter viewPagerAdapter;//banner图适配器
    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据
    private IndexQJListAdapter indexQJListAdapter;//情景列表适配器
    private List<SubjectListBean.DataBean.RowsBean> subjectList;//主题列表数据
    private IndexSubjectAdapter indexSubjectAdapter;//主题列表适配器

    @Override
    protected View initView() {
        View fragmentView = View.inflate(getActivity(), R.layout.fragment_index, null);
        dialog = new WaittingDialog(getActivity());
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadIndex);
        getActivity().registerReceiver(indexReceiver, intentFilter);
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
        pullRefreshView.setOnRefreshListener(this);
        listView.setOnScrollListener(this);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);
        moreThemeImg.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        subjectList = new ArrayList<>();
        indexSubjectAdapter = new IndexSubjectAdapter(this, subjectList);
        recyclerView.setAdapter(indexSubjectAdapter);
        //设置适配器
        moreQjImg.setOnClickListener(this);
        sceneList = new ArrayList<>();
        indexQJListAdapter = new IndexQJListAdapter(getActivity(), sceneList);
        listView.setAdapter(indexQJListAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        if (!dialog.isShowing()) {
            dialog.show();
        }
        requestNet();
    }

    @Override
    protected void requestNet() {
        sceneNet();
        subjectList();
        getBanners();
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

    @Override
    public void onPause() {
        super.onPause();
        if (scrollableView != null) {
            scrollableView.stop();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (scrollableView != null) {
            scrollableView.start();
        }
    }


    @Override
    protected void refreshUI(ArrayList<Banner> list) {
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, list);
            scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
            scrollableView.setAutoScrollDurationFactor(8);
            scrollableView.setInterval(4000);
            scrollableView.showIndicators();
            scrollableView.start();
        } else {
            viewPagerAdapter.notifyDataSetChanged();
        }

    }

    private void getBanners() {
        ClientDiscoverAPI.getBanners("app_fiu_sight_index_slide", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<首页banner图", responseInfo.result);
                try {
                    BannerData bannerData = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<BannerData>>() {
                    });
                    if (bannerData == null) {
                        return;
                    }

                    if (bannerData.rows == null) {
                        return;
                    }

                    if (bannerData.rows.size() == 0) {
                        return;
                    }
                    refreshUI(bannerData.rows);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "轮播图，数据解析异常");
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //获取精选主题
    private void subjectList() {
        ClientDiscoverAPI.subjectList("1", "4", null, "1", null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<精选主题", responseInfo.result);
                SubjectListBean subjectListBean = new SubjectListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SubjectListBean>() {
                    }.getType();
                    subjectListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常=" + e.toString());
                }
                if (subjectListBean.isSuccess()) {
                    subjectList.clear();
                    subjectList.addAll(subjectListBean.getData().getRows());
                    indexSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
    }

    //获取情景列表
    private void sceneNet() {
        ClientDiscoverAPI.getSceneList(currentPage + "", 8 + "", null, 0 + "", null, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<情景列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "情景列表解析异常" + e.toString());
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
//                    //纪录浏览次数
//                    StringBuilder ids = new StringBuilder();
//                    for (SceneList.DataBean.RowsBean rowsBean : sceneList) {
//                        ids.append(",").append(rowsBean.get_id());
//                    }
//                    if (ids.length() > 0) {
//                        ids.deleteCharAt(0);
//                        ClientDiscoverAPI.viewCount(ids.toString());
//                    }
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
    public void onDestroyView() {
        getActivity().unregisterReceiver(indexReceiver);
        super.onDestroyView();
    }

    private BroadcastReceiver indexReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
            if (firstVisibleItem != pullRefreshView.lastSavedFirstVisibleItem && pullRefreshView.lastTotalItem != totalItemCount) {
                pullRefreshView.lastSavedFirstVisibleItem = firstVisibleItem;
                pullRefreshView.lastTotalItem = totalItemCount;
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                sceneNet();
            }
        }
    }

    @Override
    public void click(int postion) {
        ToastUtils.showError("跳转主题详情");
    }
}

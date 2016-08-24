package com.taihuoniao.fineix.main.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FindQJAdapter;
import com.taihuoniao.fineix.adapters.FindQJCategoryAdapter;
import com.taihuoniao.fineix.adapters.FindRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.blurview.RenderScriptBlur;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WriteJsonToSD;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/22.
 */
public class FindFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener, View.OnClickListener, EditRecyclerAdapter.ItemClick {
    @Bind(R.id.pull_to_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.blur_view)
    BlurView blurView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.gone_relative)
    RelativeLayout goneRelative;
    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_right)
    ImageView titleRight;
    @Bind(R.id.search_linear)
    LinearLayout searchLinear;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    //不能设置OnItemClickListener
    private ListView listView;
    //HeaderView中控件
    private GridViewForScrollView gridView;
    private List<CategoryListBean.CategoryListItem> categoryList;//分类列表数据
    private FindRecyclerAdapter findRecyclerAdapter;//分类小图列表适配器
    private FindQJCategoryAdapter findQJCategoryAdapter;//分类列表适配器
    private int sneceComplete;//判断情景是否加载完毕 0，情景主题都没加载 1,情景加载完毕等待主题加载 2，主题加载完毕等待情景加载
    private int currentPage = 1;//情景列表页面
    private WaittingDialog dialog;//耗时操作对话框
    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据
    private List<SubjectListBean.DataBean.RowsBean> subjectList;//主题列表数据
    private FindQJAdapter findQJAdapter;//情景列表适配器

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        ButterKnife.bind(this, view);
        listView = pullRefreshView.getRefreshableView();
        View header = View.inflate(getActivity(), R.layout.header_find_fragment, null);
        gridView = (GridViewForScrollView) header.findViewById(R.id.grid_view);
        listView.addHeaderView(header);
        pullRefreshView.animLayout();
        dialog = new WaittingDialog(getActivity());
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadFind);
        getActivity().registerReceiver(findReceiver, intentFilter);
        return view;
    }

    @Override
    protected void initList() {
        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
        setupBlurView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        categoryList = new ArrayList<>();
        findRecyclerAdapter = new FindRecyclerAdapter(categoryList, this);
        recyclerView.setAdapter(findRecyclerAdapter);
        findQJCategoryAdapter = new FindQJCategoryAdapter(categoryList);
        gridView.setAdapter(findQJCategoryAdapter);
        gridView.setOnItemClickListener(this);
        listView.setSelector(R.color.nothing);
        listView.setDividerHeight(0);
        pullRefreshView.setOnRefreshListener(this);
        listView.setOnScrollListener(this);
        sceneList = new ArrayList<>();
        subjectList = new ArrayList<>();
        findQJAdapter = new FindQJAdapter(getActivity(), subjectList, sceneList);
        listView.setAdapter(findQJAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void setupBlurView() {
        final float radius = 16f;
//
//        final View decorView = getActivity().getWindow().getDecorView();
//        //Activity's root View. Can also be root View of your layout
//        final View rootView = decorView.findViewById(android.R.id.content);
//        //set background, if your root layout doesn't have one
//        final Drawable windowBackground = decorView.getBackground();

        blurView.setupWith(listView)
                .windowBackground(listView.getBackground())
                .blurAlgorithm(new RenderScriptBlur(getActivity(), true)) //Preferable algorithm, needs RenderScript support mode enabled
                .blurRadius(radius);
    }

    @Override
    public void onRefresh() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        sneceComplete = 0;
        currentPage = 1;
        requestNet();
    }

    @Override
    protected void requestNet() {
        subjectList();
        sceneNet();
        sceneCategoryList();
    }

    //获取情景分类列表
    private void sceneCategoryList() {
        ClientDiscoverAPI.categoryList("1", "13", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                if (categoryListBean.isSuccess()) {
                    gridView.setVisibility(View.VISIBLE);
                    categoryList.clear();
                    categoryList.addAll(categoryListBean.getData().getRows());
                    findQJCategoryAdapter.notifyDataSetChanged();
                    findRecyclerAdapter.notifyDataSetChanged();
                } else {
                    gridView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                gridView.setVisibility(View.GONE);
            }
        });
    }

    //获取情景列表嵌入的主题列表
    private void subjectList() {
        ClientDiscoverAPI.subjectList("1", "2", "1", null, null, null, new RequestCallBack<String>() {
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
                    if (sneceComplete == 1) {
                        findQJAdapter.notifyDataSetChanged();
                        sneceComplete = 2;
                    }
                }
                if (sneceComplete == 1) {
                    findQJAdapter.notifyDataSetChanged();
                    sneceComplete = 0;
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                if (sneceComplete == 1) {
                    findQJAdapter.notifyDataSetChanged();
                    sneceComplete = 0;
                }
            }
        });
    }

    //获取情景列表
    private void sceneNet() {
        ClientDiscoverAPI.getSceneList(currentPage + "", 10 + "", null, 0 + "", null, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<情景列表", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                    if (currentPage == 1) {
                        sceneList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getData().getRows());
                    if (subjectList.size() <= 0 && sneceComplete == 0) {
                        sneceComplete = 1;
                        return;
                    }
                    findQJAdapter.notifyDataSetChanged();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                ToastUtils.showError("发现好友");
                break;
            case R.id.title_right:
                ToastUtils.showError("用户排行");
                break;
            case R.id.search_linear:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(FindFragment.class.getSimpleName(),false);
                intent.putExtra("t", 9 + "");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }
    private ObjectAnimator downAnimator,upAnimator;
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (firstVisibleItem != pullRefreshView.lastSavedFirstVisibleItem && pullRefreshView.lastTotalItem != totalItemCount) {
                pullRefreshView.lastSavedFirstVisibleItem = firstVisibleItem;
                pullRefreshView.lastTotalItem = totalItemCount;
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                sceneNet();
            }
        }
        if (firstVisibleItem >= 1 && goneRelative.getTranslationY() == -goneRelative.getMeasuredHeight()) {
            if(downAnimator==null){
                downAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", 0).setDuration(300);
            }
            downAnimator.start();
        } else if (firstVisibleItem < 1 && goneRelative.getTranslationY() == 0) {
            if(upAnimator==null){
                upAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", -goneRelative.getMeasuredHeight()).setDuration(300);
            }
            upAnimator.start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        click(position);
    }

    @Override
    public void click(int postion) {
        ToastUtils.showError("情景分类=" + postion);
    }

    private BroadcastReceiver findReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(findReceiver);
        ButterKnife.unbind(this);
    }


}

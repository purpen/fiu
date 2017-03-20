package com.taihuoniao.fineix.main.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FindQJAdapter;
import com.taihuoniao.fineix.adapters.FindQJCategoryAdapter;
import com.taihuoniao.fineix.adapters.FindRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.blurview.RenderScriptBlur;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.AllFiuerActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJCategoryActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.ShareActivity;
import com.taihuoniao.fineix.user.FindFriendsActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/22.
 */
public class FindFragment extends BaseFragment implements AbsListView.OnScrollListener, AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener, View.OnClickListener, EditRecyclerAdapter.ItemClick, View.OnTouchListener {
    @Bind(R.id.to_top_img)
    RoundedImageView toTopImg;
    private View fragmentView;
    @Bind(R.id.pull_to_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.blur_view)
    BlurView blurView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.gone_relative)
    RelativeLayout goneRelative;
    @Bind(R.id.relative)
    RelativeLayout titleRelative;
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
    private CustomGridViewForScrollView gridView;
    private List<CategoryListBean.CategoryListItem> categoryList;//分类列表数据
    private FindRecyclerAdapter findRecyclerAdapter;//分类小图列表适配器
    private FindQJCategoryAdapter findQJCategoryAdapter;//分类列表适配器
    private int sneceComplete;//判断情景是否加载完毕 0，情景主题都没加载 1,情景加载完毕等待主题加载 2，主题加载完毕等待情景加载
    private int currentPage = 1;//情景列表页面
    private WaittingDialog dialog;//耗时操作对话框

    private List<SceneList.DataBean.RowsBean> sceneList;//情景列表数据
    private List<SubjectListBean.DataBean.RowsBean> subjectList;//主题列表数据
    private FindQJAdapter findQJAdapter;//情景列表适配器

    //SharePop
    private PopupWindow sharePop;

    @Override
    protected View initView() {
        fragmentView = View.inflate(getActivity(), R.layout.fragment_find, null);
        ButterKnife.bind(this, fragmentView);
        listView = pullRefreshView.getRefreshableView();
        View header = View.inflate(getActivity(), R.layout.header_find_fragment, null);
        gridView = (CustomGridViewForScrollView ) header.findViewById(R.id.grid_view);
        listView.addHeaderView(header);
        pullRefreshView.animLayout();
        dialog = new WaittingDialog(getActivity());
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadFind);
//        intentFilter.addAction(DataConstants.BroadRefreshQJ);
        getActivity().registerReceiver(findReceiver, intentFilter);
        initSharePop();
        return fragmentView;
    }

    private void initSharePop() {
        View popup_view = View.inflate(getActivity(), R.layout.pop_share_scene_detail, null);
        ImageView cancelImg = (ImageView) popup_view.findViewById(R.id.pop_share_scene_detail_cancel);
        Button shareBtn = (Button) popup_view.findViewById(R.id.pop_share_scene_detail_share_btn);
        cancelImg.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        sharePop = new PopupWindow(popup_view, DensityUtils.dp2px(getActivity(), 300), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 设置动画效果
        sharePop.setAnimationStyle(R.style.popup_style);
        sharePop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        sharePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getActivity().getWindow().setAttributes(params);
            }
        });
        sharePop.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.corner_white_4dp));
        sharePop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
    }

    @Override
    protected void initList() {
        int goneTranslation = (int) -getResources().getDimension(R.dimen.gone_height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop() + App.getStatusBarHeight() / 2,
                    recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
            titleRelative.setPadding(0, App.getStatusBarHeight(), 0, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) goneRelative.getLayoutParams();
            layoutParams.height = (int) (getResources().getDimension(R.dimen.gone_height) + App.getStatusBarHeight());
            goneRelative.setLayoutParams(layoutParams);
            goneTranslation = (int) (-App.getStatusBarHeight() - getResources().getDimension(R.dimen.gone_height));
        }
        goneRelative.setTranslationY(goneTranslation);
        listView.setOnTouchListener(this);
        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
        toTopImg.setOnClickListener(this);
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
        HashMap<String, String> requestParams = ClientDiscoverAPI.getcategoryListRequestParams("1", "13", null);
        Call httpHandler = HttpRequest.post(requestParams, URL.CATEGORY_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
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
            public void onFailure(String error) {
                gridView.setVisibility(View.GONE);
            }
        });
        addNet(httpHandler);
    }

    //获取情景列表嵌入的主题列表
    private void subjectList() {
        HashMap<String, String> params = ClientDiscoverAPI.getsubjectListRequestParams("1", "2", null, "1", "1,2", null);
        Call httpHandler = HttpRequest.post(params, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                SubjectListBean subjectListBean = new SubjectListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SubjectListBean>() {
                    }.getType();
                    subjectListBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                }
                if (subjectListBean.isSuccess()) {
                    subjectList.clear();
                    subjectList.addAll(subjectListBean.getData().getRows());
                    if (sneceComplete == 1) {
                        findQJAdapter.notifyDataSetChanged();
                        sneceComplete = 2;
                    }
                    return;
                }
                if (sneceComplete == 1) {
                    findQJAdapter.notifyDataSetChanged();
                    sneceComplete = 0;
                }
            }

            @Override
            public void onFailure(String error) {
                if (sneceComplete == 1) {
                    findQJAdapter.notifyDataSetChanged();
                    sneceComplete = 0;
                }
            }
        });
        addNet(httpHandler);
    }

    //获取情景列表
    private void sceneNet() {
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(currentPage + "", 10 + "", null, null, 0 + "", null, null, null);
        Call httpHandler = HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                }
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (sceneL.isSuccess()) {
                    findQJAdapter.setPage(sceneL.getData().getCurrent_page());
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
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_top_img:
                listView.setSelection(0);
                break;
            case R.id.pop_share_scene_detail_share_btn:
                sharePop.dismiss();
                Intent intent1 = new Intent(getActivity(), ShareActivity.class);
                intent1.putExtra("id", qjId);
                startActivity(intent1);
                break;
            case R.id.pop_share_scene_detail_cancel:
                sharePop.dismiss();
                break;
            case R.id.title_left:
                startActivity(new Intent(getActivity(), FindFriendsActivity.class));
                break;
            case R.id.title_right:
                startActivity(new Intent(getActivity(), AllFiuerActivity.class));
                break;
            case R.id.search_linear:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(FindFragment.class.getSimpleName(), false);
                intent.putExtra("t", 9);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_FLING) {
            if (isUp) {
                downAnim();
            } else {
                upAnim();
            }
        }
    }

    private ObjectAnimator downAnimator, upAnimator;
    private int animFlag = 0;

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
        if (firstVisibleItem >= 1 && toTopImg.getVisibility() == View.GONE) {
            toTopImg.setVisibility(View.VISIBLE);
        } else if (firstVisibleItem < 1) {
            if (goneRelative.getTranslationY() == 0 && toTopImg.getVisibility() == View.VISIBLE) {
                upAnim();
            }
            toTopImg.setVisibility(View.GONE);
        }
    }

    private void downAnim() {
        if (animFlag != 0) {
            return;
        }
        if (downAnimator == null) {
            downAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", 0).setDuration(300);
            downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    titleRelative.setScaleY(1f - animation.getAnimatedFraction());
                }
            });
            downAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animFlag = 1;
                    MainActivity activity = (MainActivity) getActivity();
                    activity.hint();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animFlag = 2;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        downAnimator.start();
    }

    private void upAnim() {
        if (animFlag != 2) {
            return;
        }
        if (upAnimator == null) {
            upAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", -goneRelative.getMeasuredHeight()).setDuration(300);
            upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    titleRelative.setScaleY(animation.getAnimatedFraction());
                }
            });
            upAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animFlag = 1;
                    MainActivity activity = (MainActivity) getActivity();
                    activity.show();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    animFlag = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
        upAnimator.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), QJCategoryActivity.class);
        intent.putExtra("id", categoryList.get(position).get_id());
        intent.putExtra("name", categoryList.get(position).getTitle());
        startActivity(intent);
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent(getActivity(), QJCategoryActivity.class);
        intent.putExtra("id", categoryList.get(postion).get_id());
        intent.putExtra("name", categoryList.get(postion).getTitle());
        startActivity(intent);
//        ToastUtils.showError("情景分类=" + categoryList.get(postion).get_id());
    }

    private String qjId;
    private BroadcastReceiver findReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onRefresh();
            if (intent.hasExtra("id")) {
                qjId = intent.getStringExtra("id");
                if (!TextUtils.isEmpty(qjId)) {
                    WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                    params.alpha = 0.4f;
                    getActivity().getWindow().setAttributes(params);
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    sharePop.showAtLocation(fragmentView, Gravity.CENTER, 0, 0);
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().unregisterReceiver(findReceiver);
        ButterKnife.unbind(this);
    }

    private float lastY;
    private boolean isUp;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getRawY() != lastY) {
            isUp = event.getRawY() <= lastY;
            lastY = event.getRawY();
        }
        return false;
    }

}

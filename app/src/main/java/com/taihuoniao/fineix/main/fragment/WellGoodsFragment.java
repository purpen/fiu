package com.taihuoniao.fineix.main.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FirstProductAdapter;
import com.taihuoniao.fineix.adapters.WellGoodsProductCategoryAdapter;
import com.taihuoniao.fineix.adapters.WellGoodsRecyclerAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalCallBack;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.blurview.RenderScriptBlur;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.ShopMarginDecoration;
import com.taihuoniao.fineix.main.tab3.adapter.WellGoodsCategoryAdapter;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.product.ShopCartActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomGridViewForScrollView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivityZxing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class WellGoodsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, EditRecyclerAdapter.ItemClick, View.OnTouchListener {
    @Bind(R.id.relative)
    RelativeLayout titleRelative;
    @Bind(R.id.title_left)
    ImageView titleLeft;
    @Bind(R.id.title_right)
    RelativeLayout titleRight;
    @Bind(R.id.cart_number)
    TextView cartNumber;
    @Bind(R.id.search_linear)
    LinearLayout searchLinear;
    @Bind(R.id.blur_view)
    BlurView blurView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.gone_relative)
    RelativeLayout goneRelative;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.to_top_img)
    RoundedImageView toTopImg;

    private ListView listView;
    private CustomGridViewForScrollView gridView;//商品分类
    private RecyclerView productRecycler;
    private WaittingDialog dialog;
    private List<CategoryListBean.RowsEntity> categoryList;//产品分类数据
    private WellGoodsProductCategoryAdapter wellGoodsProductCategoryAdapter;//产品分类大图适配器
    private WellGoodsRecyclerAdapter wellGoodsRecyclerAdapter;//产品分类小图适配器
    private List<FirstProductBean.ItemsEntity> firstProductList;//最新好货推荐数据
    private FirstProductAdapter firstProductAdapter;//最新好货推荐适配器
    private int currentPage = 1;//产品列表页码
    private List<SubjectListBean.RowsEntity> subjectList;//好货页面专题及产品列表
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;//好货页面爪蹄及产品适配器

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        ButterKnife.bind(this, view);
        listView = pullRefreshView.getRefreshableView();
        pullRefreshView.animLayout();
        listView.addHeaderView(getHeaderView());
        dialog = new WaittingDialog(getActivity());
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadWellGoods);
        getActivity().registerReceiver(wellGoodsReceiver, intentFilter);
        return view;
    }

    @Override
    protected void initList() {
        int goneTranslation = (int) -getResources().getDimension(R.dimen.gone_height);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop() + App.getStatusBarHeight() / 2, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
            titleRelative.setPadding(0, App.getStatusBarHeight(), 0, 0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) goneRelative.getLayoutParams();
            layoutParams.height = (int) (getResources().getDimension(R.dimen.gone_height) + App.getStatusBarHeight());
            goneRelative.setLayoutParams(layoutParams);
            goneTranslation = (int) (-App.getStatusBarHeight() - getResources().getDimension(R.dimen.gone_height));
        }
        goneRelative.setTranslationY(goneTranslation);
        titleLeft.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
        titleRight.setVisibility(View.INVISIBLE);
        toTopImg.setOnClickListener(this);
        setupBlurView();
        categoryList = new ArrayList<>();
        wellGoodsProductCategoryAdapter = new WellGoodsProductCategoryAdapter(categoryList);
        gridView.setAdapter(wellGoodsProductCategoryAdapter);
        gridView.setOnItemClickListener(this);
        wellGoodsRecyclerAdapter = new WellGoodsRecyclerAdapter(categoryList, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(wellGoodsRecyclerAdapter);
        productRecycler.setHasFixedSize(true);
        productRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        firstProductList = new ArrayList<>();
        firstProductAdapter = new FirstProductAdapter(firstProductList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", firstProductList.get(postion).get_id());
                getActivity().startActivity(intent);
            }
        });
        productRecycler.setAdapter(firstProductAdapter);
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                currentPage = 1;
                requestNet();
            }
        });
        listView.setSelector(R.color.nothing);
        listView.setDividerHeight(0);
        listView.setOnScrollListener(this);
        listView.setOnTouchListener(this);
        subjectList = new ArrayList<>();
        wellgoodsSubjectAdapter = new WellgoodsSubjectAdapter(getActivity(), subjectList);
        listView.setAdapter(wellgoodsSubjectAdapter);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void setupBlurView() {
        final float radius = 25f;
        blurView.setupWith(listView)
                .windowBackground(listView.getBackground())
                .blurAlgorithm(new RenderScriptBlur(getActivity(), true))
                .blurRadius(radius);
    }

    @Override
    protected void requestNet() {
        productCategoryList();
        firstProducts();
        subjectList();
    }

    @Override
    public void onResume() {
        super.onResume();
        cartNumber();
    }

    //获取购物车数量
    public void cartNumber() {
        if (!LoginInfo.isUserLogin()) {
            cartNumber.setVisibility(View.GONE);
            return;
        }
       Call httpHandler=  HttpRequest.post(URL.CART_NUMBER, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<CartBean> netCartBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CartBean>>(){});
                if (netCartBean.isSuccess()) {
                    if (netCartBean.getData().getCount() > 0) {
                        cartNumber.setVisibility(View.VISIBLE);
                        cartNumber.setText(netCartBean.getData().getCount() + "");
                        return;
                    }
                }
                cartNumber.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String error) {
                cartNumber.setVisibility(View.GONE);
            }
        });
        addNet(httpHandler);
    }

    //好货专题列表
    private void subjectList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsubjectListRequestParams(currentPage + "", 8 + "", null, null, 5 + "", "2");
        Call httpHandler = HttpRequest.post(requestParams, URL.SCENE_SUBJECT_GETLIST, new GlobalDataCallBack(){

            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                pullRefreshView.onRefreshComplete();
                HttpResponse<SubjectListBean> subjectListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SubjectListBean>>() {});
                if (subjectListBean.isSuccess()) {
                    if (currentPage == 1) {
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                        subjectList.clear();
                    }
                    subjectList.addAll(subjectListBean.getData().getRows());
                    wellgoodsSubjectAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //最新好货推荐
    private void firstProducts() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getfirstProductsRequestParams();
       Call httpHandler =  HttpRequest.post(requestParams, URL.PRODUCCT_INDEX_NEW, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<FirstProductBean> firstProductBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<FirstProductBean>>() { });
                if (firstProductBean.isSuccess()) {
                    firstProductList.clear();
                    firstProductList.addAll(firstProductBean.getData().getItems());
                    firstProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
        addNet(httpHandler);
    }

    //获取产品分类列表
    private void productCategoryList() {
        HashMap<String, String> params = ClientDiscoverAPI.getcategoryListRequestParams("1", "1", null);
        Call  httpHandler  = HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<CategoryListBean> categoryListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<CategoryListBean>>() {});
                if (categoryListBean.isSuccess()) {
                    gridView.setVisibility(View.GONE);
                    categoryList.clear();
                    categoryList.addAll(categoryListBean.getData().getRows());
                    mWellGoodsCategoryAdapter.putList(categoryListBean.getData().getRows());
                    wellGoodsProductCategoryAdapter.notifyDataSetChanged();
                    wellGoodsRecyclerAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_top_img:
                listView.setSelection(0);
                break;
            case R.id.title_left:
                startActivity(new Intent(getActivity(), CaptureActivityZxing.class));
                break;
            case R.id.title_right:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.WellGoodsFragment;
                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), ShopCartActivity.class));
                break;
            case R.id.search_linear:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(WellGoodsFragment.class.getSimpleName(), false);
                intent.putExtra("t", 7);
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
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                currentPage++;
                subjectList();
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
        Intent intent = new Intent(getActivity(), GoodsListActivity.class);
        intent.putExtra("id", categoryList.get(position).get_id());
        intent.putExtra("name", categoryList.get(position).getTitle());
        startActivity(intent);
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent(getActivity(), GoodsListActivity.class);
        intent.putExtra("id", categoryList.get(postion).get_id());
        intent.putExtra("name", categoryList.get(postion).getTitle());
        startActivity(intent);
    }

    private BroadcastReceiver wellGoodsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!dialog.isShowing())
                dialog.show();
            currentPage = 1;
            requestNet();
        }
    };

    @Override
    public void onDestroyView() {
        getActivity().unregisterReceiver(wellGoodsReceiver);
        super.onDestroyView();
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

    /**
     * 顶部导航栏
     */
    private WellGoodsCategoryAdapter mWellGoodsCategoryAdapter;
    private View getHeaderView(){
        View headerView = View.inflate(getActivity(), R.layout.header1_wellgoods_fragment, null);
        gridView = (CustomGridViewForScrollView) headerView.findViewById(R.id.grid_view);
        gridView.setVisibility(View.GONE);
        productRecycler = (RecyclerView) headerView.findViewById(R.id.product_recycler);

        RecyclerView recyclerView001 = (RecyclerView) headerView.findViewById(R.id.recyclerView_wellGoods_headerView_category);
        recyclerView001.setHasFixedSize(true);
        recyclerView001.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView001.addItemDecoration(new ShopMarginDecoration(activity,R.dimen.dp5));
        mWellGoodsCategoryAdapter = new WellGoodsCategoryAdapter(getActivity(), new GlobalCallBack() {
            @Override
            public void callBack(Object object) {
                if (object instanceof CategoryListBean.RowsEntity) {
                    CategoryListBean.RowsEntity rowsEntity = (CategoryListBean.RowsEntity) object;
                    Toast.makeText(activity, rowsEntity.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView001.setAdapter(mWellGoodsCategoryAdapter);
        return headerView;
    }
}

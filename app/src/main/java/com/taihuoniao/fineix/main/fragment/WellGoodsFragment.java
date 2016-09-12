package com.taihuoniao.fineix.main.fragment;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FirstProductAdapter;
import com.taihuoniao.fineix.adapters.WellGoodsProductCategoryAdapter;
import com.taihuoniao.fineix.adapters.WellGoodsRecyclerAdapter;
import com.taihuoniao.fineix.adapters.WellgoodsSubjectAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.FirstProductBean;
import com.taihuoniao.fineix.beans.SubjectListBean;
import com.taihuoniao.fineix.blurview.BlurView;
import com.taihuoniao.fineix.blurview.RenderScriptBlur;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.product.GoodsListActivity;
import com.taihuoniao.fineix.product.ShopCarActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/23.
 */
public class WellGoodsFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener, EditRecyclerAdapter.ItemClick {
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
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private ListView listView;
    private GridViewForScrollView gridView;//商品分类
    private RecyclerView productRecycler;
    private WaittingDialog dialog;
    private List<CategoryListBean.CategoryListItem> categoryList;//产品分类数据
    private WellGoodsProductCategoryAdapter wellGoodsProductCategoryAdapter;//产品分类大图适配器
    private WellGoodsRecyclerAdapter wellGoodsRecyclerAdapter;//产品分类小图适配器
    private List<FirstProductBean.DataBean.ItemsBean> firstProductList;//最新好货推荐数据
    private FirstProductAdapter firstProductAdapter;//最新好货推荐适配器
    private int currentPage = 1;//产品列表页码
    private List<SubjectListBean.DataBean.RowsBean> subjectList;//好货页面专题及产品列表
    private WellgoodsSubjectAdapter wellgoodsSubjectAdapter;//好货页面爪蹄及产品适配器

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        ButterKnife.bind(this, view);
        listView = pullRefreshView.getRefreshableView();
        pullRefreshView.animLayout();
        View header1 = View.inflate(getActivity(), R.layout.header1_wellgoods_fragment, null);
        gridView = (GridViewForScrollView) header1.findViewById(R.id.grid_view);
        listView.addHeaderView(header1);
        productRecycler = (RecyclerView) header1.findViewById(R.id.product_recycler);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleRelative.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        titleLeft.setOnClickListener(this);
        searchLinear.setOnClickListener(this);
        titleRight.setOnClickListener(this);
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
        subjectList = new ArrayList<>();
        wellgoodsSubjectAdapter = new WellgoodsSubjectAdapter(getActivity(), subjectList);
        listView.setAdapter(wellgoodsSubjectAdapter);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    private void setupBlurView() {
        final float radius = 16f;
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
        HttpHandler<String> httpHandler = ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CartBean cartBean = new CartBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CartBean>() {
                    }.getType();
                    cartBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<>>>", "数据异常" + e.toString());
                }
                CartBean netCartBean = cartBean;
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
            public void onFailure(HttpException error, String msg) {
                cartNumber.setVisibility(View.GONE);
            }
        });
        addNet(httpHandler);
    }

    //好货专题列表
    private void subjectList() {
        HttpHandler<String> httpHandler = ClientDiscoverAPI.subjectList(currentPage + "", 8 + "", null, null, 5 + "", "2", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<好货专题列表", responseInfo.result);
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
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
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    //最新好货推荐
    private void firstProducts() {
        HttpHandler<String> httpHandler = ClientDiscoverAPI.firstProducts(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<最新好货推荐", responseInfo.result);
//                WriteJsonToSD.writeToSD("json",responseInfo.result);
                FirstProductBean firstProductBean = new FirstProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<FirstProductBean>() {
                    }.getType();
                    firstProductBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "解析异常=" + e.toString());
                }
                if (firstProductBean.isSuccess()) {
                    firstProductList.clear();
                    firstProductList.addAll(firstProductBean.getData().getItems());
                    firstProductAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
        addNet(httpHandler);
    }

    //获取产品分类列表
    private void productCategoryList() {
        HttpHandler<String> httpHandler = ClientDiscoverAPI.categoryList("1", "1", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<分类列表", responseInfo.result);
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
                    wellGoodsProductCategoryAdapter.notifyDataSetChanged();
                    wellGoodsRecyclerAdapter.notifyDataSetChanged();
                } else {
                    gridView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                gridView.setVisibility(View.GONE);
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
            case R.id.title_right:
                startActivity(new Intent(getActivity(), ShopCarActivity.class));
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

    }

    private ObjectAnimator downAnimator, upAnimator;

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (firstVisibleItem != pullRefreshView.lastSavedFirstVisibleItem && pullRefreshView.lastTotalItem != totalItemCount) {
                pullRefreshView.lastSavedFirstVisibleItem = firstVisibleItem;
                pullRefreshView.lastTotalItem = totalItemCount;
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                //产品列表
                subjectList();
            }
        }
        if (firstVisibleItem >= 1 && goneRelative.getTranslationY() == -goneRelative.getMeasuredHeight()) {
            if (downAnimator == null) {
                downAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", 0).setDuration(300);
            }
            downAnimator.start();
        } else if (firstVisibleItem < 1 && goneRelative.getTranslationY() == 0) {
            if (upAnimator == null) {
                upAnimator = ObjectAnimator.ofFloat(goneRelative, "translationY", -goneRelative.getMeasuredHeight()).setDuration(300);
            }
            upAnimator.start();
        }
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
//        ToastUtils.showError("产品分类=" + postion);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}

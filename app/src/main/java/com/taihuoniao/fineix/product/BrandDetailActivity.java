package com.taihuoniao.fineix.product;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.lidroid.xutils.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.BrandProductAdapter;
import com.taihuoniao.fineix.adapters.BrandQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BrandDetailBean;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/29.
 */
public class BrandDetailActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {
    //上个界面传递过来的品牌id
    private String id;
    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.list_view)
    ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private ViewHolder holder;//headerView中的控件
    private int productPage = 1;
    private int qjPage = 1;
    private WaittingDialog dialog;
    private boolean isQJ;//当前显示的是情景还是产品
    private int qjLastTotalItem = -1;
    private int qjLastSavedFirstVisibleItem = -1;
    private int productLastTotal = -1;
    private int productLastFirst = -1;
    private List<ProductBean.ProductListItem> productList;//商品列表
    private BrandProductAdapter brandProductAdapter;//品牌下的产品列表
    private List<ProductAndSceneListBean.ProductAndSceneItem> qjList;//情景列表
    private BrandQJAdapter brandQJAdapter;//品牌下的情景列表
    private Call productHandler;
    private Call qjHandler;
    private Call brandHandler;

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            ToastUtils.showError("品牌不存在或已删除");
            finish();
        }
    }

    public BrandDetailActivity() {
        super(R.layout.activity_brand_details);
    }

    @Override
    protected void initView() {
        back.setOnClickListener(this);
        View header = View.inflate(this, R.layout.header_brand_detail, null);
        holder = new ViewHolder(header);
        listView.addHeaderView(header);
        dialog = new WaittingDialog(this);
        WindowUtils.showStatusBar(this);
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadBrandDetails);
        registerReceiver(brandReceiver, intentFilter);
    }

    @Override
    protected void initList() {
        ViewGroup.LayoutParams lp = holder.backgroundContainer.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        holder.backgroundContainer.setLayoutParams(lp);
        holder.productTv.setOnClickListener(this);
        holder.qjTv.setOnClickListener(this);
        holder.lineContainer.setPadding(0, 0, MainApplication.getContext().getScreenWidth() / 2, 0);
        productList = new ArrayList<>();
        brandProductAdapter = new BrandProductAdapter(this, productList);
        listView.setAdapter(brandProductAdapter);
        qjList = new ArrayList<>();
        brandQJAdapter = new BrandQJAdapter(this, qjList);
        listView.setOnScrollListener(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        brandDetails();
        getProductList();
        getQJList();
    }

    //获取品牌下的情景
    private void getQJList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getproductAndSceneRequestParams(qjPage + "", 8 + "", null, null, id);
        HttpRequest.post(requestParams, URL.PRODUCT_AND_SCENELIST, new GlobalDataCallBack(){
//        qjHandler = ClientDiscoverAPI.productAndScene(qjPage + "", 8 + "", null, null, id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("<<<品牌下的情景", json);
                ProductAndSceneListBean productAndSceneListBean = new ProductAndSceneListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductAndSceneListBean>() {
                    }.getType();
                    productAndSceneListBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<品牌下的情景", "解析异常=" + e.toString());
                }
                if (productAndSceneListBean.isSuccess()) {
                    if (qjPage == 1) {
                        qjList.clear();
                        qjLastSavedFirstVisibleItem = -1;
                        qjLastTotalItem = -1;
                    }
                    qjList.addAll(productAndSceneListBean.getData().getRows());
                    if (!isQJ) {
                        return;
                    }
                    brandQJAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(productAndSceneListBean.getMessage());
                }
//                WriteJsonToSD.writeToSD("json",json);
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //品牌下产品列表
    private void getProductList() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, id, null, productPage + "", 8 + "", null, null, null, null, "9,16");
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
//        productHandler = ClientDiscoverAPI.getProductList(null, null, null, id, null, productPage + "", 8 + "", null, null, null, null, "9,16", new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (productBean.isSuccess()) {
                    if (productPage == 1) {
                        productList.clear();
                        productLastFirst = -1;
                        productLastTotal = -1;
                    }
                    productList.addAll(productBean.getData().getRows());
//                    if (!isQJ) {
                    if (isQJ) {
                        return;
                    }
                    brandProductAdapter.notifyDataSetChanged();
//                    }
//                    addProductGridAdapter.notifyDataSetChanged();
                    return;
                }
                ToastUtils.showError(productBean.getMessage());
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //品牌详情
    private void brandDetails() {
        HashMap<String, String> requestParams = ClientDiscoverAPI.getbrandDetailRequestParams(id);
        HttpRequest.post(requestParams, URL.BRAND_DETAIL, new GlobalDataCallBack(){
//        brandHandler = ClientDiscoverAPI.brandDetail(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                BrandDetailBean brandDetailBean = new BrandDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandDetailBean>() {
                    }.getType();
                    brandDetailBean = gson.fromJson(json, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                if (brandDetailBean.isSuccess()) {
                    titleName.setText(brandDetailBean.getData().getTitle());
                    ImageLoader.getInstance().displayImage(brandDetailBean.getData().getCover_url(), holder.titleImg);
                    holder.des.setText(brandDetailBean.getData().getDes());
                    ImageLoader.getInstance().displayImage(brandDetailBean.getData().getBanner_url(), holder.backgroundImg);
                } else {
                    ToastUtils.showError(brandDetailBean.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qj_tv:
                if (isQJ) {
                    return;
                }
//                page = 1;
//                qjPage = 1;
                isQJ = true;
                brandQJAdapter = new BrandQJAdapter(BrandDetailActivity.this, qjList);
                listView.setAdapter(brandQJAdapter);
                ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(0, MainApplication.getContext().getScreenWidth() / 2);
                valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float f = (float) animation.getAnimatedValue();
                        holder.lineContainer.setPadding((int) f, 0,
                                (int) (MainApplication.getContext().getScreenWidth() / 2 - f), 0);
                    }
                });
                valueAnimator1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.qjTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));
                        holder.productTv.setTextColor(getResources().getColor(R.color.color_666));

//                        if (!dialog.isShowing()) {
//                            dialog.show();
//                        }
//                        getQJList();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator1.start();
                break;
            case R.id.product_tv:
                if (!isQJ) {
                    return;
                }
//                productPage = 1;
                isQJ = false;
                brandProductAdapter = new BrandProductAdapter(BrandDetailActivity.this, productList);
                listView.setAdapter(brandProductAdapter);
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, MainApplication.getContext().getScreenWidth() / 2);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float f = (float) animation.getAnimatedValue();
                        holder.lineContainer.setPadding(MainApplication.getContext().getScreenWidth() / 2 - (int) f, 0, (int) f, 0);
                    }
                });
                valueAnimator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        holder.qjTv.setTextColor(getResources().getColor(R.color.color_666));
                        holder.productTv.setTextColor(getResources().getColor(R.color.yellow_bd8913));

//                        if (!dialog.isShowing()) {
//                            dialog.show();
//                        }
//                        getProductList();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                valueAnimator.start();
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (brandHandler != null)
            brandHandler.cancel();
        if (productHandler != null)
            productHandler.cancel();
        if (qjHandler != null)
            qjHandler.cancel();
        unregisterReceiver(brandReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver brandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestNet();
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && (firstVisibleItem + visibleItemCount >= totalItemCount)) {
            if (isQJ) {
                if (firstVisibleItem != qjLastSavedFirstVisibleItem && qjLastTotalItem != totalItemCount) {
                    qjLastSavedFirstVisibleItem = firstVisibleItem;
                    qjLastTotalItem = totalItemCount;
                    progressBar.setVisibility(View.VISIBLE);
                    qjPage++;
                    getQJList();
                }
            } else {
                if (firstVisibleItem != productLastFirst && productLastTotal != totalItemCount) {
                    productLastFirst = firstVisibleItem;
                    productLastTotal = totalItemCount;
                    progressBar.setVisibility(View.VISIBLE);
                    productPage++;
                    getProductList();
                }
            }
        }
    }

    static class ViewHolder {
        @Bind(R.id.background_img)
        ImageView backgroundImg;
        @Bind(R.id.background_container)
        RelativeLayout backgroundContainer;
        @Bind(R.id.des)
        TextView des;
        @Bind(R.id.title_img)
        RoundedImageView titleImg;
        @Bind(R.id.product_tv)
        TextView productTv;
        @Bind(R.id.qj_tv)
        TextView qjTv;
        @Bind(R.id.line_container)
        RelativeLayout lineContainer;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

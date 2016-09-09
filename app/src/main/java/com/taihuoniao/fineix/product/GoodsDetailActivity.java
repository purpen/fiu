//package com.taihuoniao.fineix.product;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.GoodDetailSceneListAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.CartBean;
//import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
//import com.taihuoniao.fineix.beans.TempGoodsBean;
//import com.taihuoniao.fineix.main.MainApplication;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.network.DataConstants;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.utils.WindowUtils;
//import com.taihuoniao.fineix.view.GlobalTitleLayout;
//import com.taihuoniao.fineix.view.WaittingDialog;
//import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
///**
// * Created by taihuoniao on 2016/4/26.
// */
//public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {
//    //上个界面传递过来的产品id
//    private String id;
//    @Bind(R.id.title_layout)
//    GlobalTitleLayout titleLayout;
//    @Bind(R.id.list_view)
//    ListView listView;
//    @Bind(R.id.progress_bar)
//    ProgressBar progressBar;
//    private ViewHolder holder;//headerview中的控件
//    private WaittingDialog dialog;
//    //    private BuyGoodDetailsBean buyGoodDetailsBean;//网络请求返回值
//    private int page = 1;//相关情景列表页码
//    private List<ProductAndSceneListBean.ProductAndSceneItem> sceneList;
//    private GoodDetailSceneListAdapter goodDetailsSceneListAdapter;
//    private int lastSavedFirstVisibleItem = -1;
//    private int lastTotalItem = -1;
//
//    public GoodsDetailActivity() {
//        super(R.layout.activity_goods_detail);
//    }
//
//    @Override
//    protected void getIntentData() {
//        id = getIntent().getStringExtra("id");
//        if (id == null) {
//            ToastUtils.showError("好货不存在或已删除");
//            finish();
//        }
//    }
//
//    @Override
//    protected void initView() {
//        titleLayout.setTitle(R.string.goods_details);
//        titleLayout.setCartListener(this);
//        View header = View.inflate(this, R.layout.header_gooddetails, null);
//        holder = new ViewHolder(header);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.topContainer.getLayoutParams();
//        layoutParams.width = MainApplication.getContext().getScreenWidth();
//        layoutParams.height = layoutParams.width * 422 / 750;
//        holder.topContainer.setLayoutParams(layoutParams);
//        listView.addHeaderView(header);
//        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadGoodDetails);
//        registerReceiver(goodReceiver, intentFilter);
//        dialog = new WaittingDialog(this);
//        WindowUtils.chenjin(this);
//    }
//
//    @Override
//    protected void initList() {
//        sceneList = new ArrayList<>();
//        goodDetailsSceneListAdapter = new GoodDetailSceneListAdapter(this, sceneList);
//        listView.setAdapter(goodDetailsSceneListAdapter);
//        listView.setOnScrollListener(this);
//    }
//
//    @Override
//    protected void requestNet() {
//        if (!dialog.isShowing()) {
//            dialog.show();
//        }
//        goodDetails();
//        getSceneList();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        cartNumber();
//    }
//
//    //获取商品相关的情境列表
//    private void getSceneList() {
//        ClientDiscoverAPI.productAndScene(page + "", 8 + "", null, id, null, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                dialog.dismiss();
//                Log.e("<<<关联列表", responseInfo.result);
////                WriteJsonToSD.writeToSD("json", responseInfo.result);
//                ProductAndSceneListBean productAndSceneListBean = new ProductAndSceneListBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<ProductAndSceneListBean>() {
//                    }.getType();
//                    productAndSceneListBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<关联列表", "解析异常=" + e.toString());
//                }
//                if (productAndSceneListBean.isSuccess()) {
//                    if (page == 1) {
//                        sceneList.clear();
//                        lastSavedFirstVisibleItem = -1;
//                        lastTotalItem = -1;
//                    }
//                    sceneList.addAll(productAndSceneListBean.getData().getRows());
//                    //刷新适配器
//                    goodDetailsSceneListAdapter.notifyDataSetChanged();
//                }
//                if (progressBar == null) {
//                    return;
//                }
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                progressBar.setVisibility(View.GONE);
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
//    }
//
//    //获取商品详情
//    private void goodDetails() {
//        ClientDiscoverAPI.getTempGoods(id, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<临时产品库", responseInfo.result);
//                TempGoodsBean tempGoodsBean = new TempGoodsBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<TempGoodsBean>() {
//                    }.getType();
//                    tempGoodsBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<临时产品库", "解析异常=" + e.toString());
//                }
//                if (tempGoodsBean.isSuccess()) {
//                    holder.title.setText(tempGoodsBean.getData().getTitle());
//                    holder.brandName.setText(tempGoodsBean.getData().getBrand_name());
////                    ImageLoader.getInstance().displayImage(tempGoodsBean.getData().getBrand().getCover_url(), holder.brandImg);
//                    return;
//                }
//                dialog.dismiss();
//                ToastUtils.showError(tempGoodsBean.getMessage());
//                finish();
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                ToastUtils.showError(R.string.net_fail);
//            }
//        });
////        ClientDiscoverAPI.goodsDetails(id, new RequestCallBack<String>() {
////            @Override
////            public void onSuccess(ResponseInfo<String> responseInfo) {
////                Log.e("<<<商品详情", responseInfo.result);
////                try {
////                    Gson gson = new Gson();
////                    Type type = new TypeToken<BuyGoodDetailsBean>() {
////                    }.getType();
////                    buyGoodDetailsBean = gson.fromJson(responseInfo.result, type);
////                } catch (JsonSyntaxException e) {
////                    Log.e("<<<商品详情", "解析异常=" + e.toString());
////                }
////                if (buyGoodDetailsBean.isSuccess()) {
////                    holder.title.setText(buyGoodDetailsBean.getData().getTitle());
////                    try {
////                        holder.brandName.setText(buyGoodDetailsBean.getData().getBrand().getTitle());
////                        holder.brandContainer.setOnClickListener(new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Intent intent = new Intent(GoodsDetailActivity.this, BrandDetailActivity.class);
////                                intent.putExtra("id", buyGoodDetailsBean.getData().getBrand().get_id());
////                                startActivity(intent);
////                            }
////                        });
////                        ImageLoader.getInstance().displayImage(buyGoodDetailsBean.getData().getBrand().getCover_url(), holder.brandImg);
////                    } catch (Exception e) {
////
////                    }
////                    return;
////                }
////                dialog.dismiss();
////                ToastUtils.showError(buyGoodDetailsBean.getMessage());
////                finish();
////            }
////
////            @Override
////            public void onFailure(HttpException error, String msg) {
////                dialog.dismiss();
////                ToastUtils.showError(R.string.net_fail);
////            }
////        });
//    }
//
//    //获取购物车数量
//    public void cartNumber() {
//        ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                CartBean cartBean = new CartBean();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<CartBean>() {
//                    }.getType();
//                    cartBean = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<>>>", "数据异常" + e.toString());
//                }
//                if (cartBean.isSuccess()) {
//                    titleLayout.setCartNum(cartBean.getData().getCount());
//                    return;
//                }
//                titleLayout.setCartNum(0);
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                titleLayout.setCartNum(0);
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.title_cart:
//                startActivity(new Intent(this, ShopCarActivity.class));
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(goodReceiver);
//        super.onDestroy();
//    }
//
//    private BroadcastReceiver goodReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            requestNet();
//        }
//    };
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        if (visibleItemCount > listView.getHeaderViewsCount()
//                && firstVisibleItem + visibleItemCount >= totalItemCount) {
//            if (firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount) {
//                lastSavedFirstVisibleItem = firstVisibleItem;
//                lastTotalItem = totalItemCount;
//                progressBar.setVisibility(View.VISIBLE);
//                page++;
//                //更多情景
//                getSceneList();
//            }
//        }
//    }
//
//
//    static class ViewHolder {
//        @Bind(R.id.top_container)
//        RelativeLayout topContainer;
//        @Bind(R.id.title)
//        TextView title;
//        @Bind(R.id.brand_img)
//        RoundedImageView brandImg;
//        @Bind(R.id.brand_name)
//        TextView brandName;
//        @Bind(R.id.brand_container)
//        RelativeLayout brandContainer;
//
//        ViewHolder(View view) {
//            ButterKnife.bind(this, view);
//        }
//    }
//}

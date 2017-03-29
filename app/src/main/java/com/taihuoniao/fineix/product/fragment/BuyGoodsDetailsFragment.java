package com.taihuoniao.fineix.product.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodDetailsSceneListAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.BuyGoodDetailsBean;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BrandDetailActivity;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class BuyGoodsDetailsFragment extends SearchFragment implements AbsListView.OnScrollListener, View.OnClickListener {
    //商品id
    private String id;
    @Bind(R.id.pull_refresh_view)
    ListView listView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    private ViewHolder holder;//headerview控件
    private ViewPagerAdapter<String> viewPagerAdapter;
    private BuyGoodDetailsBean mbuyGoodDetailsBean;
    private int page = 1;//相关情景列表页码
    private List<ProductAndSceneListBean.RowsEntity> sceneList;
    private GoodDetailsSceneListAdapter goodDetailsSceneListAdapter;

    public static BuyGoodsDetailsFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        BuyGoodsDetailsFragment fragment = new BuyGoodsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getString("id");
    }

    @Override
    protected View initView() {
        View fragmentView = View.inflate(getActivity(), R.layout.fragment_buy_goods_details, null);
        ButterKnife.bind(this, fragmentView);
        View header = View.inflate(getActivity(), R.layout.header_fragment_buy_gooddetail, null);
        holder = new ViewHolder(header);
        listView.addHeaderView(header);
        return fragmentView;
    }

    @Override
    protected void initList() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.scrollableView.getLayoutParams();
        layoutParams.width = MainApplication.getContext().getScreenWidth();
        layoutParams.height = layoutParams.width * 422 / 750;
        holder.scrollableView.setLayoutParams(layoutParams);
        listView.setOnScrollListener(this);
        sceneList = new ArrayList<>();
        goodDetailsSceneListAdapter = new GoodDetailsSceneListAdapter(getActivity(), sceneList);
        listView.setAdapter(goodDetailsSceneListAdapter);
        holder.detailContainer.setOnClickListener(this);
    }

    @Override
    protected void requestNet() {
        getSceneList();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (holder.scrollableView != null) {
            holder.scrollableView.start();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (holder.scrollableView != null) {
            holder.scrollableView.stop();
        }
    }

    //用来刷新页面
    @Override
    public void refreshData(BuyGoodDetailsBean dataBean) {
        this.mbuyGoodDetailsBean = dataBean;
        if (dataBean == null) {
            return;
        }
        if (dataBean.getStage() != 9) {
            holder.detailContainer.setVisibility(View.GONE);
            holder.price.setVisibility(View.GONE);
            holder.liangdianContainer.setVisibility(View.GONE);
        } else {
            holder.marketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter<>(activity,mbuyGoodDetailsBean.getAsset());
            holder.scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
            holder.scrollableView.setAutoScrollDurationFactor(8);
            holder.scrollableView.setInterval(4000);
            holder.scrollableView.showIndicators();
            holder.scrollableView.start();
        } else {
            viewPagerAdapter.notifyDataSetChanged();
        }
        holder.title.setText(dataBean.getTitle());
        holder.price.setText("¥" + dataBean.getSale_price());
        if (dataBean.getMarket_price() > dataBean.getSale_price()) {
            holder.marketPrice.setVisibility(View.VISIBLE);
            holder.marketPrice.setText("¥" + dataBean.getMarket_price());
        } else {
            holder.marketPrice.setVisibility(View.INVISIBLE);
        }

        if (TextUtils.isEmpty(dataBean.getAdvantage())) {
            holder.liangdianContainer.setVisibility(View.GONE);
        } else {
            holder.liangdian.setText(dataBean.getAdvantage());
        }
        try {
            holder.brandName.setText(dataBean.getBrand().getTitle());
            ImageLoader.getInstance().displayImage(dataBean.getBrand().getCover_url(), holder.brandImg);
            holder.brandContainer.setOnClickListener(this);
        } catch (Exception e) {
            holder.brandContainer.setVisibility(View.GONE);
        }
        holder.marketPrice2.setVisibility("1".equals(dataBean.getActive_summary().getOrder_reduce()) ? View.VISIBLE : View.GONE);
        holder.marketPrice2.setVisibility("0".equals(dataBean.getExtra().getDisabled_app_reduce()) ? View.VISIBLE : View.GONE);

        if (dataBean.getStage() != 9) {
            holder.marketPrice.setText("此产品为用户标记，暂未销售。浮游正在努力上架产品中ing...");
            holder.marketPrice.setVisibility(View.VISIBLE);
            holder.marketPrice2.setVisibility(View.GONE);
        }
    }

    //获取商品相关的情境列表
    private void getSceneList() {
        HashMap<String, String> params =ClientDiscoverAPI. getproductAndSceneRequestParams(page + "", 8 + "", null, id, null);
       Call httpHandler = HttpRequest.post(params, URL.PRODUCT_AND_SCENELIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<ProductAndSceneListBean> productAndSceneListBean = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ProductAndSceneListBean>>() {});
                if (productAndSceneListBean.isSuccess()) {
                    if (page == 1) {
                        sceneList.clear();
                        lastSavedFirstVisibleItem = -1;
                        lastTotalItem = -1;
                    }
                    sceneList.addAll(productAndSceneListBean.getData().getRows());
                    //刷新适配器
                    goodDetailsSceneListAdapter.notifyDataSetChanged();
                }
                if (progressBar == null) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > listView.getHeaderViewsCount()
                && firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount) {
                lastSavedFirstVisibleItem = firstVisibleItem;
                lastTotalItem = totalItemCount;
                progressBar.setVisibility(View.VISIBLE);
                page++;
                //更多情景
                getSceneList();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.detail_container:
                BuyGoodsDetailsActivity activity = (BuyGoodsDetailsActivity) getActivity();
                activity.viewPager.setCurrentItem(1);
                break;
            case R.id.brand_container:
                if (mbuyGoodDetailsBean == null) {
                    getActivity().sendBroadcast(new Intent(DataConstants.BroadBuyGoodDetails));
                    return;
                }
                Intent intent = new Intent(getActivity(), BrandDetailActivity.class);
                intent.putExtra("id", mbuyGoodDetailsBean.getBrand().get_id());
                getActivity().startActivity(intent);
                break;
        }
    }

    static class ViewHolder {
        @Bind(R.id.scrollableView)
        ScrollableView scrollableView;
        @Bind(R.id.detail_container)
        LinearLayout detailContainer;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.market_price)
        TextView marketPrice;
        @Bind(R.id.liangdian_container)
        LinearLayout liangdianContainer;
        @Bind(R.id.liangdian)
        TextView liangdian;
        @Bind(R.id.brand_container)
        RelativeLayout brandContainer;
        @Bind(R.id.brand_img)
        RoundedImageView brandImg;
        @Bind(R.id.brand_name)
        TextView brandName;
        @Bind(R.id.market_price2)
        TextView marketPrice2;

        ViewHolder(View view) {
            super();
            ButterKnife.bind(this, view);
        }
    }
}

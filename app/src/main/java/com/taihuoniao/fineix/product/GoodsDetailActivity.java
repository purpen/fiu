package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailRecommendRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailSceneRecyclerAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/26.
 */
public class GoodsDetailActivity extends BaseActivity<String> implements View.OnClickListener, EditRecyclerAdapter.ItemClick {
    //上个界面传递过来的商品id
    private String id;
    //界面下的控件
    @Bind(R.id.activity_goods_detail_back)
    ImageView backImg;
    //    @Bind(R.id.activity_goods_detail_cart_relative)
//    RelativeLayout cartRelative;
//    @Bind(R.id.activity_goods_detail_cart_num)
//    TextView cartNum;
    @Bind(R.id.activity_goods_detail_scrollableView)
    ScrollableView scrollableView;
    private ViewPagerAdapter<String> viewPagerAdapter;
    @Bind(R.id.activity_goods_detail_goods_title)
    TextView name;
    @Bind(R.id.activity_goods_detail_goods_price)
    TextView price;
    @Bind(R.id.activity_goods_detail_brand_relative)
    RelativeLayout brandRelative;
    @Bind(R.id.activity_goods_detail_brand_img)
    ImageView brandImg;
    @Bind(R.id.activity_goods_detail_brand_title)
    TextView brandTitle;
    @Bind(R.id.activity_goods_detail_product_des)
    TextView productDes;
    @Bind(R.id.activity_goods_detail_suoshuchangjing_recycler)
    RecyclerView changjingRecycler;
    @Bind(R.id.activity_goods_detail_recommend_recycler)
    RecyclerView recommendRecycler;
    @Bind(R.id.activity_goods_detail_buy_now)
    Button buyNowBtn;
    @Bind(R.id.activity_goods_detail_webview)
    WebView webView;
    //判断产品是自营的还是第三方商城的标识
    private String attrbute = "0";// 1.官网；2.淘宝；3.天猫；4.京东
    private String url = null;
    //网络请求对话框
    private WaittingDialog dialog;
    private GoodsDetailBean netGood;//网络请求返回值
    //所属场景列表
    private int page = 1;
    private List<ProductAndSceneListBean.ProductAndSceneItem> changjingList;
    private GoodsDetailSceneRecyclerAdapter changjingAdaper;
    //推荐列表
    private int recommendPage = 1;
    private List<ProductListBean> recommendList;
    private GoodsDetailRecommendRecyclerAdapter recommendRecyclerAdapter;
    //由于需要请求两次数据，所以做个标记
    private int currentTime = 1;
    //圆图
    private DisplayImageOptions option;


    public GoodsDetailActivity() {
        super(R.layout.activity_goods_detail);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
//        Log.e("<<<", "商品id=" + id);
        if (id == null) {
            Toast.makeText(GoodsDetailActivity.this, "产品不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initView() {
        WindowUtils.chenjin(GoodsDetailActivity.this);
        dialog = new WaittingDialog(GoodsDetailActivity.this);
        backImg.setOnClickListener(this);
//        cartRelative.setOnClickListener(this);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);
        changjingRecycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GoodsDetailActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        changjingRecycler.setLayoutManager(layoutManager);
        changjingList = new ArrayList<>();
        changjingAdaper = new GoodsDetailSceneRecyclerAdapter(GoodsDetailActivity.this, changjingList, GoodsDetailActivity.this);
        changjingRecycler.setAdapter(changjingAdaper);
        recommendRecycler.setHasFixedSize(true);
        LinearLayoutManager recommendLayoutManager = new LinearLayoutManager(GoodsDetailActivity.this);
        recommendLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recommendRecycler.setLayoutManager(recommendLayoutManager);
        recommendList = new ArrayList<>();
        recommendRecyclerAdapter = new GoodsDetailRecommendRecyclerAdapter(GoodsDetailActivity.this, recommendList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(GoodsDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", recommendList.get(postion).get_id());
                startActivity(intent);
            }
        });
        recommendRecycler.setAdapter(recommendRecyclerAdapter);
        buyNowBtn.setOnClickListener(this);
        brandRelative.setOnClickListener(this);
        option = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default750_422)
//                .showImageForEmptyUri(R.mipmap.default750_422)
//                .showImageOnFail(R.mipmap.default750_422)
//                .displayer(new FadeInBitmapDisplayer(300))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360))
                .build();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.goodsDetail(id, handler);
        DataPaser.productAndScene(page + "", 8 + "", null, id, handler);
        DataPaser.getProductList(null, null, null, recommendPage + "", 4 + "", id, null, null, null, handler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_goods_detail_brand_relative:
                if (netGood == null) {
                    dialog.show();
                    DataPaser.goodsDetail(id, handler);
                    return;
                }
                if (netGood.getData().getBrand() == null) {
                    return;
                }
                Intent intent1 = new Intent(GoodsDetailActivity.this, BrandDetailActivity.class);
                intent1.putExtra("id", netGood.getData().getBrand().get_id());
                startActivity(intent1);
                break;
            case R.id.activity_goods_detail_buy_now:
                switch (attrbute) {
                    case "0":
                        dialog.show();
                        DataPaser.goodsDetail(id, handler);
                        break;
                    case "1":
                        Intent intent2 = new Intent(GoodsDetailActivity.this, MyGoodsDetailsActivity.class);
                        intent2.putExtra("id", id);
                        startActivity(intent2);
                        break;
                    default:
                        if (url == null) {
                            dialog.show();
                            DataPaser.goodsDetail(id, handler);
                            return;
                        }
                        ClientDiscoverAPI.wantBuy(id);
                        webView.getSettings().setJavaScriptEnabled(true);
                        Log.e("<<<商品url", "url=" + url);
                        webView.loadUrl(url);
                        Toast.makeText(GoodsDetailActivity.this, "正在跳转，请稍等", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(GoodsDetailActivity.this, WebActivity.class);
//                        intent.putExtra("url", url);
//                        startActivity(intent);
                        break;
                }
                break;
//            case R.id.activity_goods_detail_cart_relative:
//                startActivity(new Intent(GoodsDetailActivity.this, ShopCarActivity.class));
//                break;
            case R.id.activity_goods_detail_back:
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
                    ProductBean netProductBean = (ProductBean) msg.obj;
                    if (netProductBean.isSuccess() && currentTime == 1) {
                        if (netProductBean.getList().size() <= 0) {
                            return;
                        }
                        StringBuilder tags = new StringBuilder();
                        for (int i = 0; i < netProductBean.getList().size(); i++) {
                            List<String> categoryList = netProductBean.getList().get(i).getCategory_tags();
                            if (categoryList != null && categoryList.size() > 0) {
                                for (int j = 0; j < categoryList.size(); j++) {
                                    tags.append(",").append(netProductBean.getList().get(i).getCategory_tags().get(j));
                                }
                                if (tags.length() > 0) {
                                    tags.deleteCharAt(0);
                                }
                            }
                        }
                        currentTime++;
                        DataPaser.getProductList(null, null, null, recommendPage + "", 8 + "", null, tags.toString(), null, null, handler);
                    } else if (netProductBean.isSuccess() && currentTime == 2) {
                        dialog.dismiss();
                        if (recommendPage == 1) {
                            recommendList.clear();
                        }
                        recommendList.addAll(netProductBean.getList());
                        recommendRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(GoodsDetailActivity.this, netProductBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.PRODUCT_AND_SCENE:
                    dialog.dismiss();
                    ProductAndSceneListBean netProductSceneBean = (ProductAndSceneListBean) msg.obj;
                    if (netProductSceneBean.isSuccess()) {
                        if (page == 1) {
                            changjingList.clear();
                        }
                        changjingList.addAll(netProductSceneBean.getData().getRows());
                        changjingAdaper.notifyDataSetChanged();
                    } else {
                        Toast.makeText(GoodsDetailActivity.this, netProductSceneBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.GOODS_DETAIL:
                    dialog.dismiss();
                    GoodsDetailBean netGoodsDetailBean = (GoodsDetailBean) msg.obj;
                    if (netGoodsDetailBean.isSuccess()) {
                        netGood = netGoodsDetailBean;
                        ArrayList<String> banner = (ArrayList<String>) netGoodsDetailBean.getData().getBanner_asset();
                        name.setText(netGoodsDetailBean.getData().getTitle());
                        price.setText(String.format("¥ %s", netGoodsDetailBean.getData().getSale_price()));
                        if (netGoodsDetailBean.getData().getBrand() != null) {
                            ImageLoader.getInstance().displayImage(netGoodsDetailBean.getData().getBrand().getCover_url(), brandImg, option);
                            brandTitle.setText(netGoodsDetailBean.getData().getBrand().getTitle());
                        }
                        productDes.setText(netGoodsDetailBean.getData().getSummary());
                        attrbute = netGoodsDetailBean.getData().getAttrbute();
                        url = netGoodsDetailBean.getData().getLink();
                        refreshUI(banner);
                    } else {
                        Toast.makeText(GoodsDetailActivity.this, netGoodsDetailBean.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    break;
//                case DataConstants.CART_NUM:
//                    CartBean netCartBean = (CartBean) msg.obj;
//                    if (netCartBean.isSuccess()) {
//                        cartNum.setVisibility(View.VISIBLE);
//                        cartNum.setText(String.format("%d", netCartBean.getData().getCount()));
//                    } else {
//                        cartNum.setVisibility(View.GONE);
//                    }
//                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void refreshUI(List<String> list) {
        if (list == null || list.size() == 0) {
            return;
        }

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
//        DataPaser.cartNum(handler);
        if (scrollableView != null) {
            scrollableView.start();
        }
    }

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent(GoodsDetailActivity.this, SceneDetailActivity.class);
        Log.e("<<<场景详情", "id=" + changjingList.get(postion).getSight().get_id() + ",title=" + changjingList.get(postion).getSight().getTitle());
        intent.putExtra("id", changjingList.get(postion).getSight().get_id());
        startActivity(intent);
    }
}

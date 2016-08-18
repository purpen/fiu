package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailRecommendRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodsDetailSceneRecyclerAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.GoodsDetailBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.ProductAndSceneListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GridViewForScrollView;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
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
    @Bind(R.id.activity_goods_detail_cart_relative)
    RelativeLayout cartRelative;
    @Bind(R.id.activity_goods_detail_cart_num)
    TextView cartNum;
    @Bind(R.id.activity_goods_detail_scrollableView)
    ScrollableView scrollableView;
    private ViewPagerAdapter<String> viewPagerAdapter;
    @Bind(R.id.activity_goods_detail_goods_title)
    TextView name;
    @Bind(R.id.activity_goods_detail_goods_price)
    TextView price;
    @Bind(R.id.activity_goods_detail_tags_linear)
    LinearLayout tagsLinear;
    @Bind(R.id.activity_gooddetails_brandlinear)
    LinearLayout brandLinear;
    @Bind(R.id.activity_goods_detail_brand_relative)
    RelativeLayout brandRelative;
    @Bind(R.id.activity_goods_detail_brand_img)
    RoundedImageView brandImg;
    @Bind(R.id.activity_goods_detail_brand_title)
    TextView brandTitle;
    @Bind(R.id.activity_goods_detail_product_des)
    TextView productDes;
    @Bind(R.id.activity_goods_detail_suoshuchangjing_linear)
    LinearLayout changjingLinear;
    @Bind(R.id.activity_goods_detail_suoshuchangjing_recycler)
    RecyclerView changjingRecycler;
    @Bind(R.id.activity_goods_detail_recommend_recycler)
    GridViewForScrollView recommendRecycler;
    @Bind(R.id.activity_goods_detail_shoucang)
    LinearLayout shoucangLinear;
    @Bind(R.id.activity_goods_detail_shoucang_img)
    ImageView shoucangImg;
    @Bind(R.id.activity_goods_detail_buy_now)
    Button buyNowBtn;
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
    private List<ProductBean.ProductListItem> recommendList;
    private GoodsDetailRecommendRecyclerAdapter recommendRecyclerAdapter;
    //由于需要请求两次数据，所以做个标记
    private int currentTime = 1;
    //圆图
    private DisplayImageOptions option;
    private DecimalFormat df;
    private boolean isFavourate = false;//是否已收藏

    public GoodsDetailActivity() {
        super(R.layout.activity_goods_detail);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
//        Log.e("<<<", "商品id=" + id);
        if (id == null) {
            ToastUtils.showError("好货不存在");
//            dialog.showErrorWithStatus("产品不存在");
//            Toast.makeText(GoodsDetailActivity.this, "产品不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void initView() {
        df = new DecimalFormat("######0.00");
        WindowUtils.chenjin(GoodsDetailActivity.this);
        dialog = new WaittingDialog(GoodsDetailActivity.this);
        backImg.setOnClickListener(this);
        cartRelative.setOnClickListener(this);
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
//        recommendRecycler.setHasFixedSize(true);
        LinearLayoutManager recommendLayoutManager = new LinearLayoutManager(GoodsDetailActivity.this);
        recommendLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recommendRecycler.setLayoutManager(recommendLayoutManager);
        recommendList = new ArrayList<>();
        recommendRecyclerAdapter = new GoodsDetailRecommendRecyclerAdapter(GoodsDetailActivity.this, recommendList);
        recommendRecycler.setAdapter(recommendRecyclerAdapter);
        recommendRecycler.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GoodsDetailActivity.this, GoodsDetailActivity.class);
                intent.putExtra("id", recommendList.get(position).get_id());
                startActivity(intent);
            }
        });
        buyNowBtn.setOnClickListener(this);
        brandRelative.setOnClickListener(this);
        option = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
//                .displayer(new FadeInBitmapDisplayer(300))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        scrollableView.setFocusable(true);
        scrollableView.setFocusableInTouchMode(true);
        scrollableView.requestFocus();
        shoucangLinear.setOnClickListener(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        goodsDetails(id);
        productAndScene(page + "", 20 + "", null, id);

//        DataPaser.getProductList(null, null, null, 1 + "", 3 + "", null, ids.toString(), null, null, handler);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_goods_detail_shoucang:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    Intent in = new Intent(GoodsDetailActivity.this, OptRegisterLoginActivity.class);
                    startActivity(in);
                    return;
                }
                if (isFavourate) {
                    cancelFavorite();
                } else {
                    favorite();
                }
                break;
            case R.id.activity_goods_detail_brand_relative:
                if (netGood == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    goodsDetails(id);
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
                        goodsDetails(id);
                        break;
                    case "1":
                        if (netGood == null) {
                            if (!dialog.isShowing()) {
                                dialog.show();
                            }
                            goodsDetails(id);
                            return;
                        }
                        Intent intent2 = new Intent(GoodsDetailActivity.this, MyGoodsDetailsActivity.class);
                        intent2.putExtra("id", netGood.getData().getOid());
                        startActivity(intent2);
                        break;
                    default:
                        if (url == null) {
                            dialog.show();
                            goodsDetails(id);
                            return;
                        }
                        ClientDiscoverAPI.wantBuy(id);
                        ToastUtils.showInfo("正在跳转，请稍等");
                        Uri uri1 = Uri.parse(url);
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, uri1);
                        startActivity(intent3);
                        break;
                }
                break;
            case R.id.activity_goods_detail_cart_relative:
                startActivity(new Intent(GoodsDetailActivity.this, ShopCarActivity.class));
                break;
            case R.id.activity_goods_detail_back:
                onBackPressed();
                break;
        }
    }

    private void addLabelToLinear(final List<String> tagsTitleList) {
        for (int i = 0; i < tagsTitleList.size(); i++) {
            View view = View.inflate(GoodsDetailActivity.this, R.layout.view_horizontal_label_item, null);
            TextView textView = (TextView) view.findViewById(R.id.view_horizontal_label_item_tv);
            textView.setText(tagsTitleList.get(i));
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
            lp.rightMargin = DensityUtils.dp2px(GoodsDetailActivity.this, 10);
            view.setLayoutParams(lp);
//            view.setTag(tagsList.get(i));
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GoodsDetailActivity.this, SearchActivity.class);
                    intent.putExtra("q", tagsTitleList.get(finalI));
                    intent.putExtra("t", "10");
                    startActivity(intent);
                }
            });
            tagsLinear.addView(view);
        }
    }

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
        ClientDiscoverAPI.cartNum(new RequestCallBack<String>() {
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
                if (netCartBean.isSuccess() && netCartBean.getData().getCount() > 0) {
                    cartNum.setVisibility(View.VISIBLE);
                    cartNum.setText(String.format("%d", netCartBean.getData().getCount()));
                } else {
                    cartNum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {

            }
        });
        if (scrollableView != null) {
            scrollableView.start();
        }
    }


    @Override
    public void click(int postion) {
        Intent intent = new Intent(GoodsDetailActivity.this, SceneDetailActivity.class);
//        Log.e("<<<场景详情", "id=" + changjingList.get(postion).getSight().get_id() + ",title=" + changjingList.get(postion).getSight().getTitle());
        intent.putExtra("id", changjingList.get(postion).getSight().get_id());
        startActivity(intent);
    }


    private void getProducts(String category_id, String brand_id, String category_tag_ids, String page, String size, String ids, String ignore_ids,
                             String stick, String fine) {
        ClientDiscoverAPI.getProductList(null,null, category_id, brand_id, category_tag_ids, page, size, ids, ignore_ids, stick, fine, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<推荐商品", responseInfo.result);
                ProductBean productBean = new ProductBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ProductBean>() {
                    }.getType();
                    productBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                ProductBean netProductBean = productBean;
                if (netProductBean.isSuccess()) {
                    if (recommendPage == 1) {
                        recommendList.clear();
                    }
                    recommendList.addAll(netProductBean.getData().getRows());
//                    Log.e("<<<推荐数量", "size=" + recommendList.size());
                    recommendRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netProductBean.getMessage());
//                       dialog.showErrorWithStatus(netProductBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void productAndScene(String p, String size, String sight_id, String product_id) {
        ClientDiscoverAPI.productAndScene(p, size, sight_id, product_id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<关联列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                ProductAndSceneListBean productAndSceneListBean = new ProductAndSceneListBean();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    productAndSceneListBean.setSuccess(jsonObject.optBoolean("success"));
                    productAndSceneListBean.setMessage(jsonObject.optString("message"));
                    productAndSceneListBean.setStatus(jsonObject.optString("status"));
                    productAndSceneListBean.setCurrent_user_id(jsonObject.optString("current_user_id"));
                    ProductAndSceneListBean.Data data = new ProductAndSceneListBean.Data();
                    JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                    List<ProductAndSceneListBean.ProductAndSceneItem> rows = new ArrayList<ProductAndSceneListBean.ProductAndSceneItem>();
                    JSONArray jsonArray = jsonObject1.optJSONArray("rows");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                        ProductAndSceneListBean.ProductAndSceneItem productAndSceneItem = new ProductAndSceneListBean.ProductAndSceneItem();
                        ProductAndSceneListBean.Product product = new ProductAndSceneListBean.Product();
                        JSONObject jsonObject3 = jsonObject2.optJSONObject("product");
                        product.set_id(jsonObject3.optString("_id"));
                        product.setTitle(jsonObject3.optString("title"));
                        product.setOid(jsonObject3.optString("oid"));
                        product.setSale_price(jsonObject3.optDouble("sale_price"));
                        product.setMarket_price(jsonObject3.optDouble("market_price"));
                        product.setLink(jsonObject3.optString("link"));
                        product.setAttrbute(jsonObject3.optString("attrbute"));
                        product.setCover_url(jsonObject3.optString("cover_url"));
                        List<String> banner_asset = new ArrayList<String>();
                        JSONArray jsonArray1 = jsonObject3.optJSONArray("banner_asset");
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            banner_asset.add(jsonArray1.optString(j));
                        }
                        product.setBanner_asset(banner_asset);
                        productAndSceneItem.setProduct(product);
                        ProductAndSceneListBean.Sight sight = new ProductAndSceneListBean.Sight();
                        JSONObject jsonObject4 = jsonObject2.optJSONObject("sight");
                        if (jsonObject4 != null) {
                            sight.set_id(jsonObject4.optString("_id"));
                            sight.setTitle(jsonObject4.optString("title"));
                            sight.setAddress(jsonObject4.optString("address"));
                            sight.setCover_url(jsonObject4.optString("cover_url"));
                        } else {
                            continue;
                        }
                        productAndSceneItem.setSight(sight);
                        rows.add(productAndSceneItem);
                    }
                    data.setRows(rows);
                    productAndSceneListBean.setData(data);
                } catch (JSONException e) {
                    Log.e("<<<场景商品关联列表>>>", "数据异常" + e.toString());
                }
                dialog.dismiss();
                if (productAndSceneListBean.isSuccess()) {
                    if (page == 1) {
                        changjingList.clear();
                    }
                    changjingList.addAll(productAndSceneListBean.getData().getRows());
                    if (changjingList.size() <= 0) {
                        changjingLinear.setVisibility(View.GONE);
                    } else {
                        changjingLinear.setVisibility(View.VISIBLE);
                    }
                    changjingAdaper.notifyDataSetChanged();
                } else {
                    changjingLinear.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void goodsDetails(String id) {
        ClientDiscoverAPI.goodsDetails(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<商品详情", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                GoodsDetailBean goodsDetailBean = new GoodsDetailBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<GoodsDetailBean>() {
                    }.getType();
                    goodsDetailBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<>>>", "数据异常" + e.toString());
                }
                dialog.dismiss();
                GoodsDetailBean netGoodsDetailBean = goodsDetailBean;
                if (netGoodsDetailBean.isSuccess()) {
                    netGood = netGoodsDetailBean;
                    getProducts(netGood.getData().getCategory_id(), null, null, 1 + "", 4 + "", null, netGoodsDetailBean.getData().get_id(), null, null);
                    ArrayList<String> banner = (ArrayList<String>) netGoodsDetailBean.getData().getBanner_asset();
                    name.setText(netGoodsDetailBean.getData().getTitle());
                    price.setText("¥ " + netGoodsDetailBean.getData().getSale_price());
                    if (netGoodsDetailBean.getData().getBrand() != null) {
                        ImageLoader.getInstance().displayImage(netGoodsDetailBean.getData().getBrand().getCover_url(), brandImg, option);
                        brandTitle.setText(netGoodsDetailBean.getData().getBrand().getTitle());
                    } else {
                        brandLinear.setVisibility(View.GONE);
                        brandRelative.setVisibility(View.GONE);
                    }
                    addLabelToLinear(netGoodsDetailBean.getData().getTags());
                    productDes.setText(netGoodsDetailBean.getData().getSummary());
                    attrbute = netGoodsDetailBean.getData().getAttrbute();
                    // 1.官网；2.淘宝；3.天猫；4.京东
                    switch (attrbute) {
                        case "1":
                            buyNowBtn.setText("去购买");
                            break;
                        case "2":
                            buyNowBtn.setText("去淘宝购买");
                            break;
                        case "3":
                            buyNowBtn.setText("去天猫购买");
                            break;
                        case "4":
                            buyNowBtn.setText("去京东购买");
                            break;
                    }
                    url = netGoodsDetailBean.getData().getLink();
                    if (netGoodsDetailBean.getData().getIs_favorite() == 1) {
                        isFavourate = true;
                        shoucangImg.setImageResource(R.mipmap.star_yellow_width_42px);
                    } else {
                        isFavourate = false;
                        shoucangImg.setImageResource(R.mipmap.star_gray_width_42px);
                    }
                    refreshUI(banner);
                } else {
                    ToastUtils.showError(netGoodsDetailBean.getMessage());
                    finish();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
            }
        });
    }

    //收藏和取消收藏
    private void favorite() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.favorite(id, "10", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<收藏产品", responseInfo.result);
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<收藏", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    isFavourate = true;
                    shoucangImg.setImageResource(R.mipmap.star_yellow_width_42px);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    private void cancelFavorite() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.cancelFavorite(id, "10", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<收藏产品", responseInfo.result);
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<收藏", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                if (netBean.isSuccess()) {
                    isFavourate = false;
                    shoucangImg.setImageResource(R.mipmap.star_gray_width_42px);
                } else {
                    ToastUtils.showError(netBean.getMessage());
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }
}

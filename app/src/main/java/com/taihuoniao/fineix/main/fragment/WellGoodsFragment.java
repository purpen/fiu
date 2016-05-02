package com.taihuoniao.fineix.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SlidingFocusAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.beans.ProductListData;
import com.taihuoniao.fineix.beans.RandomImg;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.SlidingFocusImageView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WellGoodsFragment extends BaseFragment<Banner> implements EditRecyclerAdapter.ItemClick, View.OnClickListener {
    private ImageView searchImg;
    private ImageView cartImg;
    private AbsoluteLayout absoluteLayout;
    private RecyclerView labelRecycler;
    private RecyclerView recyclerView;
    private WaittingDialog dialog;
    private static final String PAGE_NAME = "app_fiu_product_index_slide";
    //标签列表
    private List<HotLabel.HotLabelBean> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //分类列表
    private List<CategoryListBean> list;
    private PinRecyclerAdapter pinRecyclerAdapter;
    private DisplayImageOptions options;
    private ScrollableView scrollableView;
    private SlidingFocusImageView sfiv;
    private ViewPagerAdapter viewPagerAdapter;
    private SlidingFocusAdapter sfAdapter = null;
    private int page = 1;
    private static final String PRODUCT_STATE = "1"; //表示正常在线
    private TextView tv_name;
    private TextView tv_price;
    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        searchImg = (ImageView) view.findViewById(R.id.fragment_wellgoods_search);
        cartImg = (ImageView) view.findViewById(R.id.fragment_wellgoods_cart);
        scrollableView = (ScrollableView) view.findViewById(R.id.scrollableView);
        sfiv = (SlidingFocusImageView) view.findViewById(R.id.sfiv);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_price = (TextView) view.findViewById(R.id.tv_price);
        sfiv.setAnimationDuration(500);
        sfiv.setFadingEdgeLength(200);
        sfiv.setSpacing(10);
        sfiv.setGravity(Gravity.CENTER_VERTICAL);
        absoluteLayout = (AbsoluteLayout) view.findViewById(R.id.fragment_wellgoods_absolute);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MainApplication.getContext().getScreenWidth(), DensityUtils.dp2px(getActivity(), 157));
        absoluteLayout.setLayoutParams(lp);
        labelRecycler = (RecyclerView) view.findViewById(R.id.fragment_wellgoods_label_recycler);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_wellgoods_recycler);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void installListener() {
        sfiv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("onItemSelected", "" + position);
//                ((SlidingFocusAdapter) sfiv.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sfiv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 处理点击事件
            }
        });
    }

    @Override
    protected void requestNet() {
        dialog.show();
        ClientDiscoverAPI.getBanners(PAGE_NAME, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

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
                    Util.makeToast(activity, "对不起,数据异常");
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast(s);
            }
        });
//        DataPaser.hotLabelList(labelPage + "", handler);
        DataPaser.categoryList(1 + "", 1 + "", handler);
        ClientDiscoverAPI.getProductList(String.valueOf(page), new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                ProductListData data = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ProductListData>>() {
                });
                if (data.rows == null) {
                    return;
                }

                if (data.rows.size() == 0) {
                    return;
                }
                setSlidingFocusImageViewData(data.rows);
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
        //热门标签
        ClientDiscoverAPI.labelList(null, 1, null, 5, 1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.HOT_LABEL_LIST;
                msg.obj = new HotLabel();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HotLabel>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<", "请求失败" + error.toString() + ",msg=" + msg);
            }
        });
        //品牌列表
        DataPaser.brandList(1, 40, handler);
    }

    private void setSlidingFocusImageViewData(ArrayList<ProductListBean> list) {
        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            return;
        }

        if (sfAdapter == null) {//获得产品列表的一项  list.get(0)
            sfAdapter = new SlidingFocusAdapter(sfiv, list.get(0).banner_asset, activity);
            tv_name.setText(list.get(0).getTitle());
            tv_price.setText("￥"+list.get(0).getSale_price());
            sfiv.setAdapter(sfAdapter);
            sfiv.setSelection(Integer.MAX_VALUE/2);
        } else {
            sfAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initList() {
        searchImg.setOnClickListener(this);
        cartImg.setOnClickListener(this);
        hotLabelList = new ArrayList<>();
        labelRecycler.setHasFixedSize(true);
        labelRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        pinLabelRecyclerAdapter = new PinLabelRecyclerAdapter(getActivity(), hotLabelList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        pinRecyclerAdapter = new PinRecyclerAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pinRecyclerAdapter);
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.BRAND_LIST:
                    BrandListBean netBrandListBean = (BrandListBean) msg.obj;
                    if(netBrandListBean.isSuccess()){
                        addImgToAbsolute();
                    }
                    break;
                case DataConstants.HOT_LABEL_LIST:
                    dialog.dismiss();
                    HotLabel netHotLabel = (HotLabel) msg.obj;
                    if (netHotLabel.isSuccess()) {
                        hotLabelList.addAll(netHotLabel.getData().getRows());
                        pinLabelRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        list.addAll(netCategoryBean.getList());
                        pinRecyclerAdapter.notifyDataSetChanged();
                    }

                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected void refreshUI(ArrayList<Banner> list) {
        ArrayList<String> urlList = new ArrayList<String>();
        for (Banner banner : list) {
            urlList.add(banner.cover_url);
        }
        if (urlList.size() == 0) {
            return;
        }

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, urlList);
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
        if (scrollableView != null) {
            scrollableView.start();
        }
    }

    @Override
    public void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void click(int postion) {
        Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据里应有图片地址，类型，id，
     * http://frbird.qiniudn.com/scene_product/160413/570de6b63ffca2e3108bc4b3-1-p500x500.jpg
     * http://frbird.qiniudn.com/scene_product/160412/570cbbb93ffca27d078bb969-1-p500x500.jpg
     * http://frbird.qiniudn.com/scene_product/160412/570cb2593ffca268098c293a-1-p500x500.jpg
     * 大 51dp 中 33dp 小 21dp
     */
    private void addImgToAbsolute() {
        Random random = new Random();
        List<RandomImg> randomImgs = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RandomImg randomImg = new RandomImg();
//            if (i % 3 == 0) {
            randomImg.url = "http://frbird.qiniudn.com/scene_product/160413/570de6b63ffca2e3108bc4b3-1-p500x500.jpg";
//            } else if (i % 3 == 1) {
//                randomImg.url = "http://frbird.qiniudn.com/scene_product/160412/570cbbb93ffca27d078bb969-1-p500x500.jpg";
//            } else {
//                randomImg.url = "http://frbird.qiniudn.com/scene_product/160412/570cb2593ffca268098c293a-1-p500x500.jpg";
//            }
            randomImg.type = random.nextInt(3) + 1;
            if (randomImg.type == 1) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 21) / 2;
            } else if (randomImg.type == 2) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 33) / 2;
            } else {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 51) / 2;
            }
            randomImgs.add(randomImg);
        }
        int top = absoluteLayout.getTop();
//        Log.e("<<<", "top = " + top);
        int bottom = absoluteLayout.getBottom();
//        Log.e("<<<", "bottom = " + bottom);
        for (int i = 0; i < randomImgs.size(); i++) {
//            boolean isStop = false;
            RandomImg randomImg = randomImgs.get(i);

            whi:
//            while (!isStop) {
            for (int k = 0; k < 200; k++) {
                int x = random.nextInt(MainApplication.getContext().getScreenWidth());
//                Log.e("<<<", "x = " + x);
                int y = random.nextInt(bottom - top) + top;
//                Log.e("<<<", "y = " + y);
                if (MainApplication.getContext().getScreenWidth() - x < randomImg.radius || x < randomImg.radius ||
                        bottom - y < randomImg.radius || y - top < randomImg.radius) {
                    continue;
                }
//                Log.e("<<<", "radius = " + randomImg.radius);
//                Log.e("<<<", "右边距 = " + (MainApplication.getContext().getScreenWidth() - x) + ",下边距 = "
//                        + (bottom - y) + ",上边距 = " + (y - top));
                for (int j = 0; j < absoluteLayout.getChildCount(); j++) {
                    ImageView img1 = (ImageView) absoluteLayout.getChildAt(j);
                    RandomImg randomImg1 = (RandomImg) img1.getTag();
                    if (randomImg1 == null) {
                        continue;
                    }
                    if (Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg1.y - y) * (randomImg1.y - y)) < randomImg1.radius + randomImg.radius) {
                        continue whi;
                    }
//                    Log.e("<<<", "点距离=" + Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg.y - y) * (randomImg.y - y))
//                            + ",半径和=" + (randomImg1.radius + randomImg.radius));
                }
//                Log.e("<<<", "x = " + x + ",y = " + y);
                randomImg.x = x;
                randomImg.y = y;
//                Log.e("<<<", "img.x=" + randomImg.x);
//                Log.e("<<<", "img.y=" + randomImg.y);
                break;
            }
//            }
            if (randomImg.x == 0 && randomImg.y == 0) {
                continue;
            }
//            Log.e("<<<", "url =" + randomImgs.get(i).url + ",radius=" + randomImg.radius + ",imgx=" + randomImg.x + ",imgy=" + randomImg.y);
            ImageView img = new ImageView(getActivity());
            ImageLoader.getInstance().displayImage(randomImgs.get(i).url, img, options);
            img.setLayoutParams(new AbsoluteLayout.LayoutParams(randomImg.radius * 2, randomImg.radius * 2,
                    randomImg.x - randomImg.radius, randomImg.y - top - randomImg.radius));
//            Log.e("<<<", "图片参数=宽高：" + (randomImg.radius * 2) + ",x:" + randomImg.x + ",y:" + (randomImg.y - top));
            img.setTag(randomImg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RandomImg randomImg1 = (RandomImg) v.getTag();
                    Toast.makeText(getActivity(), "又点击了", Toast.LENGTH_SHORT).show();
                }
            });
            absoluteLayout.addView(img);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_wellgoods_search:
                Toast.makeText(getActivity(), "跳转到搜索界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fragment_wellgoods_cart:
                Toast.makeText(getActivity(), "跳转到购物车", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

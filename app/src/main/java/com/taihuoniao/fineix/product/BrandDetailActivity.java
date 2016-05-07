package com.taihuoniao.fineix.product;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BrandDetailBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/5.
 */
public class BrandDetailActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener {
    //上个界面传递过来的名牌详情id
    private String id;
    //界面下的控件
    private ImageView backImg;
    private TextView titleTv;
    private ListView listView;
    private ProgressBar progressBar;
    private ImageView backgroundImg;
    private ImageView brandImg;
    private TextView desTv;
    //加载圆图
    private DisplayImageOptions option;
    //网络请求对话框
    private WaittingDialog dialog;
    //商品列表
    private int page = 1;
    private List<ProductListBean> productList;
    private GoodListAdapter goodListAdapter;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;

    public BrandDetailActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_brand_detail);
        backImg = (ImageView) findViewById(R.id.activity_brand_detail_back);
        titleTv = (TextView) findViewById(R.id.activity_brand_detail_title_name);
        listView = (ListView) findViewById(R.id.activity_brand_detail_listview);
        progressBar = (ProgressBar) findViewById(R.id.activity_brand_detail_progress);
        View header = View.inflate(BrandDetailActivity.this, R.layout.header_brand_detail, null);
        backgroundImg = (ImageView) header.findViewById(R.id.header_brand_detail_backgroundimg);
        brandImg = (ImageView) header.findViewById(R.id.header_brand_detail_titleimg);
        desTv = (TextView) header.findViewById(R.id.header_brand_detail_des);
        listView.addHeaderView(header);
        dialog = new WaittingDialog(BrandDetailActivity.this);
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
    protected void initList() {
        id = getIntent().getStringExtra("id");
        if (id == null) {
            Toast.makeText(BrandDetailActivity.this, "暂无此品牌详细信息", Toast.LENGTH_SHORT).show();
            finish();
        }
        backImg.setOnClickListener(this);
        ViewGroup.LayoutParams lp = backgroundImg.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        backgroundImg.setLayoutParams(lp);

        productList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(BrandDetailActivity.this, productList,null);
        listView.setAdapter(goodListAdapter);
        listView.setOnScrollListener(this);
    }


    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.brandDetail(id, handler);
        DataPaser.getProductList(null, id, null, page + "", 8 + "", null, null, null, null, handler);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    ProductBean netProductBean = (ProductBean) msg.obj;
                    if (netProductBean.isSuccess()) {
                        if (page == 1) {
                            productList.clear();
                            lastSavedFirstVisibleItem = -1;
                            lastTotalItem = -1;
                            if (netProductBean.getList().size() > 0) {
                                ImageLoader.getInstance().loadImage(netProductBean.getList().get(0).banner_asset.get(0), new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                        backgroundImg.setImageBitmap(blurImageAmeliorate(loadedImage));
                                    }

                                    @Override
                                    public void onLoadingCancelled(String imageUri, View view) {

                                    }
                                });
                            }
                        }
                        productList.addAll(netProductBean.getList());
                        goodListAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(BrandDetailActivity.this, netProductBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.BRAND_DETAIL:
                    dialog.dismiss();
                    BrandDetailBean netBrandDetail = (BrandDetailBean) msg.obj;
                    if (netBrandDetail.isSuccess()) {
                        titleTv.setText(netBrandDetail.getData().getTitle());
                        ImageLoader.getInstance().displayImage(netBrandDetail.getData().getCover_url(), brandImg, option);
                        desTv.setText(netBrandDetail.getData().getDes());
                    } else {
                        Toast.makeText(BrandDetailActivity.this, netBrandDetail.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_brand_detail_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //由于添加了headerview的原因，所以visibleitemcount要大于1，正常只需要大于0就可以
        if (visibleItemCount > 1 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                ) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            page++;
            progressBar.setVisibility(View.VISIBLE);
            DataPaser.getProductList(null, id, null, page + "", 8 + "", null, null, null, null, handler);
        }
    }

    /**
     * 柔化效果(高斯模糊)
     */
    private Bitmap blurImageAmeliorate(Bitmap bmp) {
        long start = System.currentTimeMillis();
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;

        int delta = 16; // 值越小图片会越亮，越大则越暗

        int idx = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);

                        newR = newR + (int) (pixR * gauss[idx]);
                        newG = newG + (int) (pixG * gauss[idx]);
                        newB = newB + (int) (pixB * gauss[idx]);
                        idx++;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[i * width + k] = Color.argb(255, newR, newG, newB);

                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        long end = System.currentTimeMillis();
        Log.e("<<<mohu", "used time=" + (end - start));
        return bitmap;
    }
}

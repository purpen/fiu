package com.taihuoniao.fineix.product;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.WrapContentViewPager;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/26.
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的商品id
    private String id;
    //界面下的控件
    @Bind(R.id.activity_goods_detail_back)
    ImageView backImg;
    @Bind(R.id.activity_goods_detail_cart_relative)
    RelativeLayout cartRelative;
    @Bind(R.id.activity_goods_detail_cart_num)
    TextView cartNum;
    //banner略过
    @Bind(R.id.activity_goods_detail_goods_title)
    TextView name;
    @Bind(R.id.activity_goods_detail_goods_price)
    TextView price;
    @Bind(R.id.activity_goods_detail_brand_img)
    ImageView brandImg;
    @Bind(R.id.activity_goods_detail_brand_title)
    TextView brandTitle;
    @Bind(R.id.activity_goods_detail_brand_des)
    TextView brandDes;
    @Bind(R.id.activity_goods_detail_product_lightdot)
    TextView lightDot;
    @Bind(R.id.activity_goods_detail_product_des)
    TextView productDes;
    @Bind(R.id.activity_goods_detail_suoshuchangjing_recycler)
    RecyclerView changjingRecucler;
    @Bind(R.id.activity_goods_detail_recommend_grid)
    GridView recommendGrid;
    @Bind(R.id.activity_goods_detail_add_to_cart)
    Button addToCartBtn;
    @Bind(R.id.activity_goods_detail_buy_now)
    Button buyNowBtn;
    @Bind(R.id.activity_goods_detail_goodsdetail_relative)
    RelativeLayout productDetailRelative;
    @Bind(R.id.activity_goods_detail_comment_relative)
    RelativeLayout commentRelative;
    @Bind(R.id.activity_goods_detail_goodsdetail_tv)
    TextView productDetailTv;
    @Bind(R.id.activity_goods_detail_comment_tv)
    TextView commentTv;
    @Bind(R.id.activity_goods_detail_goodsdetail_line)
    TextView productLine;
    @Bind(R.id.activity_goods_detail_comment_line)
    TextView commentLine;
    @Bind(R.id.activity_goods_detail_viewpager)
    WrapContentViewPager viewPager;


    public GoodsDetailActivity() {
        super(R.layout.activity_goods_detail);
    }

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        Log.e("<<<", "商品id=" + id);
    }

    @Override
    protected void initView() {
        backImg.setOnClickListener(this);
        cartRelative.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataPaser.cartNum(handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_goods_detail_cart_relative:
                Toast.makeText(GoodsDetailActivity.this, "跳转到购物车", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_goods_detail_back:
                onBackPressed();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CART_NUM:
                    CartBean netCartBean = (CartBean) msg.obj;
                    if (netCartBean.isSuccess()) {
                        cartNum.setVisibility(View.VISIBLE);
                        cartNum.setText(netCartBean.getData().getCount());
                    } else {
                        cartNum.setVisibility(View.GONE);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    break;
            }
        }
    };
}

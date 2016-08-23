package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodListCategoryAdapter;
import com.taihuoniao.fineix.adapters.GoodListFirtViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CartBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.utils.WriteJsonToSD;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/3.
 */
public class GoodsListActivity extends BaseActivity implements View.OnClickListener, EditRecyclerAdapter.ItemClick {
    //上个界面传递过来的点击哪个跳转
    private int position = 0;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.cart_number)
    TextView cartNumber;
    @Bind(R.id.cart_relative)
    RelativeLayout cartRelative;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    //网络请求对话框
    private WaittingDialog dialog;
    private List<CategoryListBean.CategoryListItem> categoryList;//分类列表数据
    private GoodListCategoryAdapter goodListCategoryAdapter;//分类列表适配器

    @Override
    protected void getIntentData() {
        position = getIntent().getIntExtra("position", 0);
    }

    public GoodsListActivity() {
        super(R.layout.activity_good_list);
    }

    @Override
    protected void initView() {
        back.setOnClickListener(this);
        cartRelative.setOnClickListener(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //设置适配器
        categoryList = new ArrayList<>();
        goodListCategoryAdapter = new GoodListCategoryAdapter(categoryList,this);
        dialog = new WaittingDialog(GoodsListActivity.this);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        ClientDiscoverAPI.categoryList(1 + "", 10 + "", 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<分类列表",responseInfo.result);
                WriteJsonToSD.writeToSD("json",responseInfo.result);
                CategoryListBean categoryListBean = new CategoryListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CategoryListBean>() {
                    }.getType();
                    categoryListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<分类列表", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                if (categoryListBean.isSuccess()) {
                    categoryList.clear();
                    categoryList.addAll(categoryListBean.getData().getRows());
                    goodListCategoryAdapter.notifyDataSetChanged();
                    GoodListFirtViewPagerAdapter goodListFirtViewPagerAdapter = new GoodListFirtViewPagerAdapter(getSupportFragmentManager(), categoryListBean);
//                    firstViewPager.setAdapter(goodListFirtViewPagerAdapter);
//                    firstSliding.setViewPager(firstViewPager);
//                    firstViewPager.setCurrentItem(position, true);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart_relative:
                Intent intent1 = new Intent(GoodsListActivity.this, ShopCarActivity.class);
                startActivity(intent1);
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
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
                    cartNumber.setVisibility(View.VISIBLE);
                    cartNumber.setText(String.format("%d", netCartBean.getData().getCount()));
                } else {
                    cartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                cartNumber.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void click(int postion) {
        ToastUtils.showError("点击="+postion);
    }
}

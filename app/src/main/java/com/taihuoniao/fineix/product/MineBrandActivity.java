package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.MineBrandGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.BrandListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/12.
 */
public class MineBrandActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;
    @Bind(R.id.grid_view)
    GridView gridView;
    private WaittingDialog dialog;
    private List<BrandListBean.DataBean.RowsBean> mineBrandList;
    private MineBrandGridAdapter mineBrandGridAdapter;

    public MineBrandActivity() {
        super(R.layout.activity_mine_brand);
    }

    @Override
    protected void initView() {
        titlelayout.setBackgroundResource(R.color.white);
        dialog = new WaittingDialog(this);
        titlelayout.setBackImg(R.mipmap.back_black);
        titlelayout.setContinueTvVisible(false);
        titlelayout.setTitle(R.string.mine_brand, getResources().getColor(R.color.black333333));
        mineBrandList = new ArrayList<>();
        mineBrandGridAdapter = new MineBrandGridAdapter(this, mineBrandList, false, gridView);
        gridView.setAdapter(mineBrandGridAdapter);
        gridView.setOnItemClickListener(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        //品牌列表
        ClientDiscoverAPI.brandList(1, Integer.MAX_VALUE, null, 1 + "", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<品牌列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
                BrandListBean brandListBean = new BrandListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<BrandListBean>() {
                    }.getType();
                    brandListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常" + e.toString());
                }
                dialog.dismiss();
                if (brandListBean.isSuccess()) {
                    mineBrandList.addAll(brandListBean.getData().getRows());
                    mineBrandGridAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(brandListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BrandListBean.DataBean.RowsBean rowsBean = (BrandListBean.DataBean.RowsBean) gridView.getAdapter().getItem(position);
        Intent intent = new Intent(this, BrandDetailActivity.class);
        intent.putExtra("id", rowsBean.get_id());
        startActivity(intent);
    }
}

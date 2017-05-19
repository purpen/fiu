package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommonBaseAdapter;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.zone.bean.ShareH5Url;
import com.taihuoniao.fineix.zone.bean.ZonePopularizeProductsBean;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/5/18.
 */

public class ZonePopularizeGoodsClassifyAdapter extends CommonBaseAdapter<ZonePopularizeProductsBean.RowsBean> {
    private String currentZoneId;
    public ZonePopularizeGoodsClassifyAdapter(Activity activity, List<ZonePopularizeProductsBean.RowsBean> list,String currentZoneId) {
        super(list, activity);
        this.currentZoneId = currentZoneId;
    }


    @Override
    public ZonePopularizeProductsBean.RowsBean getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ZonePopularizeProductsBean.RowsBean item = list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_zone_popularize_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, activity.getResources().getDimensionPixelSize(R.dimen.dp120)));
        GlideUtils.displayImage(item.cover_url, holder.iv);
        holder.tvTitle.setText(item.title);
        holder.tvCost.setText(String.format("销售价：¥%s", item.sale_price));
        holder.tvCommission.setText(String.format("佣金：%s%s", item.commision_percent * 100, "%"));
        setClickListener(holder.btnPopularize, item);
        setClickListener(holder.btnAdd, item);
        setClickListener(holder.iv, item);
        return convertView;
    }

    private void setClickListener(View v, final ZonePopularizeProductsBean.RowsBean item) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.btn_popularize: //1代表产品
                        HttpRequest.post(ClientDiscoverAPI.getH5ShareParams(item._id, "1", ""), URL.SHARE_H5_URL, new GlobalDataCallBack() {
                            @Override
                            public void onStart() {
                                v.setEnabled(false);
                            }

                            @Override
                            public void onSuccess(String json) {
                                v.setEnabled(true);
                                HttpResponse<ShareH5Url> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ShareH5Url>>() {
                                });
                                if (response.isSuccess()) {
                                    Util.copy2ClipBoard(response.getData().url);
                                } else {
                                    ToastUtils.showError(response.getMessage());
                                }
                            }

                            @Override
                            public void onFailure(String error) {
                                v.setEnabled(true);
                                ToastUtils.showError(R.string.network_err);
                            }
                        });
                        break;
                    case R.id.iv:
                        Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                        intent.putExtra("id", item._id);
                        intent.putExtra("storage_id", "");
                        activity.startActivity(intent);
                        break;
                    case R.id.btn_add:
                        addGoods(v,item);
                        break;
                    default:

                        break;
                }
            }
        });
    }

    private void addGoods(final View v, ZonePopularizeProductsBean.RowsBean item) {
        HashMap<String,String> params = new HashMap<>();
        params.put("scene_id",currentZoneId);
        params.put("product_id",item._id);
        HttpRequest.post(params, URL.ZONE_MANAGE_PRODUCTS_ADD_ONE, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                v.setEnabled(false);
            }

            @Override
            public void onSuccess(String json) {
                v.setEnabled(true);
                HttpResponse response = JsonUtil.fromJson(json,HttpResponse.class);
                if (response.isSuccess()) {
                    ((Button)v).setText("已添加");
                    ((Button)v).setBackgroundResource(R.drawable.bg_ccc_radius4);
                    ToastUtils.showSuccess(response.getMessage());
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                v.setEnabled(true);
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    static class ViewHolder {
        @Bind(R.id.iv)
        ImageView iv;
        @Bind(R.id.tv_title)
        TextView tvTitle;
        @Bind(R.id.tv_cost)
        TextView tvCost;
        @Bind(R.id.tv_commission)
        TextView tvCommission;
        @Bind(R.id.btn_popularize)
        Button btnPopularize;
        @Bind(R.id.btn_add)
        Button btnAdd;

        @Bind(R.id.content_layout)
        LinearLayout contentLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

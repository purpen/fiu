package com.taihuoniao.fineix.zone.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CommonBaseAdapter;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.DPUtil;
import com.taihuoniao.fineix.utils.GlideUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.dialog.DefaultDialog;
import com.taihuoniao.fineix.view.dialog.IDialogListener;
import com.taihuoniao.fineix.zone.bean.ShareH5Url;
import com.taihuoniao.fineix.zone.bean.ZoneManageGoodsBean;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/5/17.
 */

public class ZoneManageGoodsAdapter extends CommonBaseAdapter<ZoneManageGoodsBean.RowsBean> {
    private float lastXOffset = 0;
    private float downX = 0;
    private boolean isRight = false;
    private HorizontalScrollView lastView;
    public ZoneManageGoodsAdapter(Activity activity, List<ZoneManageGoodsBean.RowsBean> list) {
        super(list, activity);
    }

    @Override
    public ZoneManageGoodsBean.RowsBean getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ZoneManageGoodsBean.RowsBean item = list.get(position);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = Util.inflateView(R.layout.item_zone_manage_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, activity.getResources().getDimensionPixelSize(R.dimen.dp120));
        params.width = activity.getResources().getDisplayMetrics().widthPixels;
        holder.contentLayout.setLayoutParams(params);

//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.contentLayout.getLayoutParams();
        GlideUtils.displayImage(item.product.cover_url, holder.iv);
        holder.tvTitle.setText(item.product.title);
        holder.tvCost.setText(String.format("销售价：¥%s", item.product.sale_price));
        holder.tvCommission.setText(String.format("佣金：%s%s", item.product.commision_percent * 100, "%"));
        holder.horizontalScrollView.scrollTo(0,0);
        setClickListener(holder.btnPopularize, item);
        setClickListener(holder.tvDelete,item);
        setClickListener(holder.iv,item);

        holder.horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final HorizontalScrollView view = (HorizontalScrollView)v;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (lastView!=null){
                            if(lastView.getTag(R.id.horizontalScrollview)==view.getTag(R.id.horizontalScrollview)){
                                view.smoothScrollTo(0,0);
                            }else {
                                lastView.smoothScrollTo(0,0);
                            }
                        }
                        view.setTag(R.id.horizontalScrollview,position);
                        lastView = view;
                        downX = event.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (event.getX() > lastXOffset) {
                            isRight = true;
                        } else {
                            isRight = false;
                        }
                        lastXOffset = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float distance = Math.abs(event.getX() - downX);
                        if (distance == 0.0) {
                            if (lastView!=null){
                                if(lastView.getTag(R.id.horizontalScrollview)==view.getTag(R.id.horizontalScrollview)){
                                    view.smoothScrollTo(0,0);
                                }else {
                                    lastView.smoothScrollTo(0,0);
                                }
                            }

                        } else if (distance > 0 && distance < DPUtil.dip2px(MainApplication.getContext(),35)) {
                            v.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isRight) {
                                        view.fullScroll(View.FOCUS_RIGHT);
                                    } else {
                                        view.fullScroll(View.FOCUS_LEFT);
                                    }
                                }
                            });
                        } else { //大于指定的值
                            v.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (isRight) {
                                        view.fullScroll(View.FOCUS_LEFT);
                                    } else {
                                        view.fullScroll(View.FOCUS_RIGHT);
                                    }
                                }
                            });
                        }
                        break;
                    default:
                        break;
                }

                return false;
            }
        });


        return convertView;
    }


    private void setClickListener(View v, final ZoneManageGoodsBean.RowsBean item) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.btn_popularize:
                        HttpRequest.post(ClientDiscoverAPI.getH5ShareParams(item._id, "1",item.scene_id), URL.SHARE_H5_URL, new GlobalDataCallBack() {
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
                    case R.id.tv_delete:
                        deleteGoods(v,item._id);
                        break;
                    case R.id.iv:
                        Intent intent = new Intent(activity, BuyGoodsDetailsActivity.class);
                        intent.putExtra("id",item.product_id);
                        intent.putExtra("storage_id",item.scene_id);
                        activity.startActivity(intent);
                        break;
                    default:

                        break;
                }
            }
        });
    }

    private void deleteGoods(final View v, final String id) {
        if (TextUtils.isEmpty(id)) return;
        String[] operateText = {"取消","确认"};
        new DefaultDialog(activity,"删除此商品?",operateText,new IDialogListener(){
            @Override
            public void click(View view, int index) {
                switch (view.getId()){
                    case R.id.button_dialog_confirm:
                        HashMap<String, String> params = new HashMap<>();
                        params.put("id", id);
                        HttpRequest.post(params, URL.ZONE_MANAGE_PRODUCTS_DELETE_ONE, new GlobalDataCallBack() {
                            @Override
                            public void onStart() {
                                v.setEnabled(false);
                            }

                            @Override
                            public void onSuccess(String json) {
                                v.setEnabled(true);
                                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                                if (response.isSuccess()) {
                                    for (ZoneManageGoodsBean.RowsBean item : list) {
                                        if (TextUtils.equals(id, item._id)) {
                                            list.remove(item);
                                            notifyDataSetChanged();
                                            break;
                                        }
                                    }
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
                }
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
        @Bind(R.id.tv_delete)
        TextView tvDelete;
        @Bind(R.id.btn_popularize)
        Button btnPopularize;
        @Bind(R.id.btn_add)
        Button btnAdd;
        @Bind(R.id.horizontalScrollview)
        HorizontalScrollView horizontalScrollView;
        @Bind(R.id.content_layout)
        LinearLayout contentLayout;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

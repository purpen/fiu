package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.adapter.ZoneClassifyAdapter;
import com.taihuoniao.fineix.zone.bean.ZoneClassifyBean;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 地盘分类
 */
public class ZoneClassifyActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private ZoneDetailBean zoneDetailBean;
    private ZoneClassifyAdapter adapter;
    private WaittingDialog dialog;
    private List<ZoneClassifyBean.RowsBean> list;
    private ZoneClassifyBean.RowsBean selectedItem;

    public ZoneClassifyActivity() {
        super(R.layout.activity_zone_classify);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(activity);
        customHead.setHeadCenterTxtShow(true, R.string.title_zone_classify);
        customHead.setHeadRightTxtShow(true, R.string.save);
        WindowUtils.chenjin(this);
        list = new ArrayList<>();
        adapter = new ZoneClassifyAdapter(activity, list);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new ZoneClassifyMarginDecoration(activity, R.dimen.dp15));
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void requestNet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("domain", "12");
        params.put("show_sub", "1");
        HttpRequest.post(params, URL.CATEGORY_LIST, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                ZoneClassifyBean zoneClassifyBean = JsonUtil.fromJson(json, new TypeToken<HttpResponse<ZoneClassifyBean>>() {
                });
                list.addAll(zoneClassifyBean.rows);
                refreshUI();
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                ToastUtils.showInfo(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        for (ZoneClassifyBean.RowsBean rowsBean:list){
            rowsBean.isSelected = TextUtils.equals(rowsBean._id, zoneDetailBean.category._id);
        }
        if (adapter == null) {
            adapter = new ZoneClassifyAdapter(activity, list);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void installListener() {
        adapter.setOnItemClickListener(new ZoneClassifyAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                String clickId = list.get(position)._id;
                for (ZoneClassifyBean.RowsBean row:list){
                    if (TextUtils.equals(row._id,clickId)){
                        selectedItem = row;
                        row.isSelected = true;
                    }else {
                        row.isSelected = false;
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    @OnClick({R.id.tv_head_right})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                submitData();
                break;
            default:
                break;
        }
    }

    private void submitData() {
        if (selectedItem ==null) {
            ToastUtils.showInfo("请选择地盘分类");
            return;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", zoneDetailBean._id);
        params.put("category_id",selectedItem._id);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    Intent intent = new Intent();
                    zoneDetailBean.category._id = selectedItem._id;
                    zoneDetailBean.category.title = selectedItem.title;
                    intent.putExtra(TAG,zoneDetailBean);
                    setResult(RESULT_OK,intent);
                    finish();
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showInfo(R.string.network_err);
            }
        });
    }

}

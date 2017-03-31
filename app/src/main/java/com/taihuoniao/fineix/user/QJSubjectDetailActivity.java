package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.QJSubjectDetailAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.QJSubjectDetailBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lilin on 2017/3/1.
 * 情境专题详情
 */

public class QJSubjectDetailActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private QJSubjectDetailAdapter adapter;
    private String id; //情境专题的id
    private WaittingDialog dialog;

    public QJSubjectDetailActivity() {
        super(R.layout.activity_qj_subject_detail);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(activity);
        View view = View.inflate(this, R.layout.header_qj_subject_detail, null);
        ImageView imageView = ButterKnife.findById(view, R.id.iv_cover);
//        GlideUtils.displayImageFadein(discoverBean.stick.cover_url, imageView);
        TextView tvTitle = ButterKnife.findById(view, R.id.tv_title);
        TextView tvSubtitle = ButterKnife.findById(view, R.id.tv_subtitle);
//        list = new ArrayList();
//        adapter = new ZoneRelateSceneAdapter(activity,);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(id)) return;
        final HashMap<String, String> params = ClientDiscoverAPI.getgetSubjectDataRequestParams(id);
        HttpRequest.post(params, URL.SCENE_SUBJECT_VIEW, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                HttpResponse<QJSubjectDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<QJSubjectDetailBean>>() {});
                QJSubjectDetailBean data = response.getData();
                if (response.isSuccess()) {
                    customHead.setHeadCenterTxtShow(true,data.title);
                    if (adapter == null) {
                        QJSubjectDetailAdapter adapter = new QJSubjectDetailAdapter(activity,data);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}

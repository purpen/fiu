package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.common.bean.BannerBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by Stephen on 2017/03/01.
 */
public class QJDetailActivity2 extends BaseActivity {

    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;

    private String situationId;

    public QJDetailActivity2() {
        super(R.layout.activity_qjdetail2);
    }

    @Override
    protected void getIntentData() {
        situationId = getIntent().getStringExtra("id");
        if (situationId == null) {
            ToastUtils.showError("访问的情境不存在或已删除");
            finish();
        }
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(R.string.qj_detail);
        titleLayout.setContinueTvVisible(false);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void requestNet() {
        requestData001(situationId);
    }

    /**
     * 情境详情
     */
    private void requestData001(String situationId){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getsceneDetailsRequestParams(situationId);
        HttpRequest.post(requestParams, URL.SCENE_DETAILS, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    /**
     * 猜你喜欢
     */
    private void requestData002(String categoryIDs){
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(1 + "", 10 + "", null, null, 0 + "", null, null, null);
        re.put("category_ids", categoryIDs);
        re.put("stick","1");
        re.put("sort","1");
        re.put("is_product", "1");
        HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
            }

            @Override
            public void onFailure(String error) {}
        });
    }

    /**
     * 相关情境
     */
    private void requestData003(String cagegoryId){
        HashMap<String, String> requestParams = ClientDiscoverAPI.getgetProductListRequestParams(null, null, null, null, null, null, null, null, null, null, null, null);
        requestParams.put("category_id", cagegoryId);
        requestParams.put("stick", "1");
        requestParams.put("sort", "4");
        HttpRequest.post(requestParams, URL.URLSTRING_PRODUCTSLIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
            }

            @Override
            public void onFailure(String error) {}
        });
    }
}

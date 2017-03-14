package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.HashMap;

import butterknife.OnClick;

/**
 * Created by lilin on 2017/02/22.
 */

public class ZoneEditCoverDialogActivity extends BaseActivity {
    private ZoneDetailBean zoneDetailBean;
    private int currentItem;
    public ZoneEditCoverDialogActivity() {
        super(R.layout.activity_zone_edit_cover_dialog);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            zoneDetailBean = intent.getParcelableExtra(TAG);
            currentItem = intent.getIntExtra(ZoneEditCoverDialogActivity.class.getName(),0);
        }
    }

    @Override
    protected void initView() {
        setFinishOnTouchOutside(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(lp);
    }

    @OnClick({R.id.ibtn_close,R.id.btn_set_default,R.id.btn_delete})
    void onClick(final View v){
        switch (v.getId()){
            case R.id.ibtn_close:
                finish();
                break;
            case R.id.btn_set_default:  //设为默认
                if (null==zoneDetailBean) return;
                if (null == zoneDetailBean.n_covers) return;
                HashMap<String, String> param = new HashMap<>();
                param.put("id",zoneDetailBean._id);
                param.put("asset_id",zoneDetailBean.n_covers.get(currentItem).id);
                param.put("type","1");
                HttpRequest.post(param, URL.ZONE_SET_DEFAULT_COVER, new GlobalDataCallBack() {
                    @Override
                    public void onSuccess(String json) {
                        HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                        if (response.isSuccess()){
                            finish();
                            ToastUtils.showSuccess(response.getMessage());
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(String error) {
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                break;
            case R.id.btn_delete:
                if (zoneDetailBean==null||zoneDetailBean.n_covers==null || zoneDetailBean.n_covers.size()==0) return;
                HashMap<String, String> params = new HashMap<>();
                params.put("id",zoneDetailBean.n_covers.get(currentItem).id);
                HttpRequest.post(params,URL.ZONE_COVER_DELETE, new GlobalDataCallBack() {
                    @Override
                    public void onStart() {
                        v.setEnabled(false);
                    }

                    @Override
                    public void onSuccess(String json) {
                        v.setEnabled(true);
                        HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                        if (response.isSuccess()) {
                            Intent intent = new Intent();
                            intent.putExtra(TAG,currentItem);
                            setResult(RESULT_OK,intent);
                            ToastUtils.showSuccess(response.getMessage());
                            finish();
                            return;
                        }
                        ToastUtils.showError(response.getMessage());
                    }

                    @Override
                    public void onFailure(String error) {
                        v.setEnabled(true);
                        ToastUtils.showError(R.string.network_err);
                    }
                });
                break;
            default:
                break;
        }
    }
}

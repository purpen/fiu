package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
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
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改地盘地址
 */
public class ZoneEditAddressActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    private static final int REQUEST_ADDRESS_CODE = 100;
    private ZoneDetailBean zoneDetailBean;
    public ZoneEditAddressActivity() {
        super(R.layout.activity_zone_edit_address);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            zoneDetailBean=intent.getParcelableExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        WindowUtils.chenjin(this);
        customHead.setHeadCenterTxtShow(true,R.string.title_modify_address);
        customHead.setHeadRightTxtShow(true, R.string.save);
        if (zoneDetailBean!=null) tvAddress.setText(zoneDetailBean.address);
    }

    @OnClick({R.id.tv_head_right,R.id.tv_address})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                submitData();
                break;
            case R.id.tv_address:
                Intent intent = new Intent(activity, ZoneMapSelectAddressActivity.class);
                intent.putExtra(ZoneMapSelectAddressActivity.class.getSimpleName(), zoneDetailBean);
                startActivityForResult(intent,REQUEST_ADDRESS_CODE);
                break;
            default:
                break;
        }
    }

    protected void submitData() {
        final String address = tvAddress.getText().toString();
        if (TextUtils.isEmpty(address)) {
            ToastUtils.showInfo("请先填选择地盘地址");
            return;
        }

        HashMap<String,String> params = new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("address",address);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode!=RESULT_OK) return;
        switch (requestCode){
            case REQUEST_ADDRESS_CODE:
                PoiInfo poiInfo = data.getParcelableExtra(PoiInfo.class.getSimpleName());
                if (poiInfo==null) return;
                tvAddress.setText(poiInfo.address+poiInfo.name);
                break;
            default:
                break;
        }
    }
}

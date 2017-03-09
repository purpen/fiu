package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.CustomItemLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 地盘管理
 */
public class ZoneManagementActivity extends BaseActivity {
    private static final int REQUEST_MODIFY_BRIEF = 101;
    @Bind(R.id.custom_head)
    CustomHeadView customHeadView;
    @Bind(R.id.item_zone_base_info)
    CustomItemLayout itemZoneBaseInfo;
    @Bind(R.id.item_zone_brief)
    CustomItemLayout itemZoneBrief;
    @Bind(R.id.item_light_spot)
    CustomItemLayout itemLightSpot;
    @Bind(R.id.item_zone_address)
    CustomItemLayout itemZoneAddress;
    @Bind(R.id.item_zone_phone)
    CustomItemLayout itemZonePhone;
    @Bind(R.id.item_zone_auth)
    CustomItemLayout itemZoneAuth;
    @Bind(R.id.item_zone_business)
    CustomItemLayout itemZoneBusiness;
    private String zoneId = "";
    private ZoneDetailBean zoneDetailBean;
    private WaittingDialog dialog;
    public ZoneManagementActivity() {
        super(R.layout.activity_zone_management);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent !=null){
            zoneId = intent.getStringExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(activity);
        customHeadView.setHeadCenterTxtShow(true, R.string.title_zone_manage);
        itemZoneBaseInfo.setTVStyle(0,R.string.zone_base_info,R.color.color_666);
        itemZoneBrief.setTVStyle(0, R.string.zone_brief, R.color.color_666);
        itemLightSpot.setTVStyle(0, R.string.zone_light_spot, R.color.color_666);
        itemZoneAddress.setTVStyle(0, R.string.zone_address, R.color.color_666);
        itemZonePhone.setTVStyle(0, R.string.zone_phone, R.color.color_666);
        itemZoneBusiness.setTVStyle(0, R.string.zone_business_times, R.color.color_666);
        itemZoneAuth.setTVStyle(0, R.string.zone_auth, R.color.color_666);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestNet();
    }

    @Override
    protected void requestNet() {
        HashMap params = ClientDiscoverAPI.getZoneDetailParams(zoneId);
        HttpRequest.post(params, URL.ZONE_DETAIL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog!=null&& !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog!=null&& !activity.isFinishing()) dialog.dismiss();
                HttpResponse<ZoneDetailBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ZoneDetailBean>>() {
                });
                if (response.isSuccess()) {
                    zoneDetailBean = response.getData();
                    refreshUI();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                if (dialog!=null&& !activity.isFinishing()) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        itemZoneAddress.setTvArrowLeftStyle(true,zoneDetailBean.address,R.color.color_333);
        itemZonePhone.setTvArrowLeftStyle(true,zoneDetailBean.extra.tel,R.color.color_333);
        itemZoneBusiness.setTvArrowLeftStyle(true,zoneDetailBean.extra.shop_hours,R.color.color_333);
    }

    @OnClick({R.id.item_zone_base_info,R.id.item_zone_brief, R.id.item_zone_phone, R.id.item_zone_auth})
    void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.item_zone_base_info: //地盘基本信息
                intent= new Intent(activity,ZoneBaseInfoActivity.class);
                intent.putExtra(ZoneBaseInfoActivity.class.getSimpleName(),zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_brief://地盘简介
                intent= new Intent(activity,ZoneEditBriefActivity.class);
                intent.putExtra(ZoneEditBriefActivity.class.getSimpleName(),zoneDetailBean);
                startActivity(intent);
                break;
            case R.id.item_zone_phone://地盘电话

                break;
            case R.id.item_zone_auth: //地盘认证

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_MODIFY_BRIEF: //修改简介

                break;
            default:
                break;
        }
    }

}

package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改地盘简介
 */
public class ZoneEditBriefActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.tv_num)
    TextView tvNum;
    private ZoneDetailBean zoneDetailBean;
    public ZoneEditBriefActivity() {
        super(R.layout.activity_edit_zone_brief);
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
        customHead.setHeadCenterTxtShow(true,R.string.title_zone_brief);
        customHead.setHeadRightTxtShow(true, R.string.save);
        if (!TextUtils.isEmpty(zoneDetailBean.des)){
            etName.setText(zoneDetailBean.des);
            tvNum.setText((140-zoneDetailBean.des.length())+"");
            etName.setSelection(zoneDetailBean.des.length());
        }
    }

    @OnClick({R.id.tv_head_right})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                submitData();
                break;
            default:
                break;
        }
    }

    @Override
    protected void installListener() {
        etName.addTextChangedListener(tw);
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String str = cs.toString().trim();
            tvNum.setText((140-str.length())+"");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    protected void submitData() {
        final String zoneName = etName.getText().toString();
        if (TextUtils.isEmpty(zoneName)) {
            ToastUtils.showInfo("请输入地盘简介");
            return;
        }
        HashMap<String,String> params = new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("des",zoneName);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    zoneDetailBean.des = zoneName;
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

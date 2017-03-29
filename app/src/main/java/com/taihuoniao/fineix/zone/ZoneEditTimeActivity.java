package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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
 * 修改地盘营业时间
 */
public class ZoneEditTimeActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    private ZoneDetailBean zoneDetailBean;
    public ZoneEditTimeActivity() {
        super(R.layout.activity_edit_zone_time);
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
        customHead.setHeadCenterTxtShow(true,R.string.title_business_times);
        customHead.setHeadRightTxtShow(true, R.string.save);
        if (zoneDetailBean!=null && zoneDetailBean.extra!=null) etName.setText(zoneDetailBean.extra.shop_hours);

    }

    @OnClick({R.id.tv_head_right, R.id.ibtn})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                submitData();
                break;
            case R.id.ibtn:
                etName.getText().clear();
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
            String keyWord = cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                ibtn.setVisibility(View.VISIBLE);
            } else {
                ibtn.setVisibility(View.GONE);
            }
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
            ToastUtils.showInfo("请先填写地盘营业时间");
            return;
        }

//        if (zoneName.length()<3){
//            ToastUtils.showInfo("标题长度应为3~15个字符");
//            return;
//        }

        HashMap<String,String> params = new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("extra_shop_hours",zoneName);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    zoneDetailBean.extra.shop_hours = zoneName;
                    Intent intent = new Intent();
                    intent.putExtra(TAG,zoneDetailBean);
                    setResult(RESULT_OK, intent);
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

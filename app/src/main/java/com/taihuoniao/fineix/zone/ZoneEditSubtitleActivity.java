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
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.zone.bean.ZoneDetailBean;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改地盘副标题
 */
public class ZoneEditSubtitleActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    private ZoneDetailBean zoneDetailBean;
    public ZoneEditSubtitleActivity() {
        super(R.layout.activity_edit_zone_subtitle);
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
        customHead.setHeadCenterTxtShow(true,R.string.zone_subtitle);
        customHead.setHeadRightTxtShow(true, R.string.save);
        etName.setText(zoneDetailBean.sub_title);
    }

    @OnClick({R.id.tv_head_right, R.id.ibtn})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                if (!TextUtils.isEmpty(etName.getText().toString().trim())) {
                    submitData();
                } else {
                    Util.makeToast("请先填写地盘副标题");
                }
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
            ToastUtils.showInfo("请输入副标题");
            return;
        }

        if (zoneName.length()<3){
            ToastUtils.showInfo("副标题长度应为3~20个字符");
            return;
        }

        HashMap<String,String> params = new HashMap<>();
        params.put("id",zoneDetailBean._id);
        params.put("sub_title",zoneName);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    zoneDetailBean.sub_title = zoneName;
                    Intent intent = new Intent();
                    intent.putExtra(TAG,zoneDetailBean);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                ToastUtils.showError(R.string.network_err);
            }
        });
    }
}

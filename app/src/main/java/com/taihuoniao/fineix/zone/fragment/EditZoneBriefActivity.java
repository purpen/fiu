package com.taihuoniao.fineix.zone.fragment;
import android.content.Intent;
import android.text.TextUtils;
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
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑店铺简介
 */
public class EditZoneBriefActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    private String id = "";
    public EditZoneBriefActivity() {
        super(R.layout.activity_edit_zone_title);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {

        }
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true,R.string.modify_zone_name);
        customHead.setHeadRightTxtShow(true, R.string.save);
    }

    @OnClick({R.id.tv_head_right, R.id.ibtn})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                if (!TextUtils.isEmpty(etName.getText().toString().trim())) {
                    submitData();
                } else {
                    Util.makeToast("请先填写地盘名称");
                }
                break;
            case R.id.ibtn:
                etName.getText().clear();
                break;
        }
    }

    protected void submitData() {
        final String zoneName = etName.getText().toString();
        HashMap<String,String> params = new HashMap<>();
        params.put("id",id);
        params.put("title",zoneName);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    Intent intent = new Intent();
//                    user.nickname = nickName;
//                    intent.putExtra(User.class.getSimpleName(), user);
//                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e("error"+error);
            }
        });
    }
}

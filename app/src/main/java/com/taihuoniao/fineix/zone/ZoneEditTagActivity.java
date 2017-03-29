package com.taihuoniao.fineix.zone;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 修改地盘标签
 */
public class ZoneEditTagActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.et_name)
    EditText etName;

    private ZoneDetailBean zoneDetailBean;

    private List<String> tags;

    public ZoneEditTagActivity() {
        super(R.layout.activity_edit_zone_tag);
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
        WindowUtils.chenjin(this);
        customHead.setHeadCenterTxtShow(true, R.string.zone_tags);
        customHead.setHeadRightTxtShow(true, R.string.save);
        StringBuffer stringBuffer = new StringBuffer();
        tags = zoneDetailBean.tags;
        int size = tags.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                stringBuffer.append(tags.get(i) );
            } else {
                stringBuffer.append(tags.get(i) + ",");
            }
        }
        etName.setText(stringBuffer.toString());
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

    protected void submitData() {
        final String zoneName = etName.getText().toString();

        if (TextUtils.isEmpty(zoneName)) {
            ToastUtils.showInfo("请输入标签");
            return;
        }
        tags.clear();
        final String tag;
        if (zoneName.contains(",")){
            String[] split = zoneName.split(",");
            final StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) {
                    buffer.append(split[i]);
                } else {
                    buffer.append(split[i] + ",");
                }
                tags.add(split[i]);
            }
            tag = buffer.toString();
        }else {
            tags.add(zoneName);
            tag = zoneName;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("id", zoneDetailBean._id);
        params.put("tags",tag);
        HttpRequest.post(params, URL.SCENE_SCENE_SAVE_URL, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    zoneDetailBean.tags = tags;
                    Intent intent = new Intent();
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

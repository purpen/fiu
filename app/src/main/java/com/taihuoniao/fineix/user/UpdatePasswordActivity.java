package com.taihuoniao.fineix.user;


import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/9 10:36
 */
public class UpdatePasswordActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et_old)
    EditText et_old;
    @Bind(R.id.et_new)
    EditText et_new;
    private WaittingDialog dialog;

    public UpdatePasswordActivity() {
        super(R.layout.activity_update_password);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        custom_head.setHeadCenterTxtShow(true, "修改密码");
        custom_head.setHeadRightTxtShow(true, R.string.complete);
        WindowUtils.chenjin(this);
    }

    @OnClick(R.id.tv_head_right)
    void performClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                submitData(v);
                break;
        }
    }

    private void submitData(final View v) {
        String originPsd = et_old.getText().toString().trim();
        String newPsd = et_new.getText().toString().trim();
        if (TextUtils.isEmpty(originPsd)) {
            Util.makeToast("请填写原密码");
            return;
        }

        if (TextUtils.isEmpty(newPsd)) {
            Util.makeToast("请填写新密码");
            return;
        }

        if (TextUtils.equals(originPsd, newPsd)) {
            Util.makeToast("原密码不能和新密码相同");
            return;
        }
        HashMap<String, String> params =ClientDiscoverAPI. getupdatePasswordRequestParams(originPsd, newPsd);
        HttpRequest.post(params,  URL.MY_MODIFY_PASSWORD, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                v.setEnabled(false);
                if (dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                v.setEnabled(true);
                dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                v.setEnabled(true);
                dialog.dismiss();
               if (!TextUtils.isEmpty(error)) Util.makeToast("请先检查网络连接");

            }
        });

    }
}

package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/9 14:42
 */
public class CompleteUserInfoActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    @Bind(R.id.et_sign)
    EditText et_sign;
    public CompleteUserInfoActivity(){
        super(R.layout.activity_complete_user_info);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"完善个人资料");
    }

    @OnClick(R.id.btn)
    void performClick(View v){
        switch (v.getId()){
            case R.id.btn:
                submitData(v);
                break;
        }
    }

    private void submitData(View v) {
        String nickname = et_nickname.getText().toString().trim();
        String sign = et_sign.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)){
            Util.makeToast("请填写昵称");
            return;
        }

        if (TextUtils.isEmpty(sign)){
            Util.makeToast("请填写个性签名");
            return;
        }

//        ClientDiscoverAPI.updateUserInfo();

        //TODO 上传用户信息
    }
}

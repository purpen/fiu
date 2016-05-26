package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/4/27 13:11
 */
public class UserEditNameActivity extends BaseActivity {
    @Bind(R.id.head_view)
    CustomHeadView head_view;
    private HashMap hashMap;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    private User user;

    public UserEditNameActivity() {
        super(R.layout.activity_modify_nickname);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(User.class.getSimpleName())) {
                user = (User) bundle.getSerializable(User.class.getSimpleName());
            }
        }
    }

    @Override
    protected void initView() {
        head_view.setHeadCenterTxtShow(true, R.string.title_modify_nickname);
        head_view.setHeadRightTxtShow(true, R.string.save);
        if (user != null) {
            et_nickname.setText(user.nickname);
        }
    }

    @OnClick({R.id.tv_head_right, R.id.ibtn})
    protected void submit(View v) {
        switch (v.getId()) {
            case R.id.tv_head_right:
                if (!TextUtils.isEmpty(et_nickname.getText().toString().trim())) {
                    submitData();
                } else {
                    Util.makeToast("请先填写昵称");
                }
                break;
            case R.id.ibtn:
                et_nickname.getText().clear();
                break;
        }
    }


    protected void submitData() {
        final String nickName = et_nickname.getText().toString();
        EditUserInfoActivity.isSubmitAddress = false;
        ClientDiscoverAPI.updateUserInfo("nickname", nickName, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()) {
                    Util.makeToast(response.getMessage());
                    Intent intent = new Intent();
                    user.nickname = nickName;
                    intent.putExtra(User.class.getSimpleName(), user);
                    setResult(RESULT_OK, intent);
                    finish();
                    return;
                }

                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast(s);
            }
        });
    }
}

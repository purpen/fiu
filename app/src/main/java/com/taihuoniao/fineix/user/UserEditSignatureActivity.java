package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.taihuoniao.fineix.view.labelview.AutoLabelUI;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/28 16:36
 */
public class UserEditSignatureActivity extends BaseActivity{
    @Bind(R.id.head_view)
    CustomHeadView head_view;
    private HashMap hashMap;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    private User user;
    @Bind(R.id.label_view)
    AutoLabelUI label_view;

    public UserEditSignatureActivity(){
        super(R.layout.activity_edit_signatrue);
    }

    @Override
    protected void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            if (bundle.containsKey(User.class.getSimpleName())) {
                user = (User)bundle.getSerializable(User.class.getSimpleName());
            }
        }
    }

    @Override
    protected void initView() {
        head_view.setHeadCenterTxtShow(true,"个性签名");
        head_view.setHeadRightTxtShow(true, R.string.save);
        if (user!=null){
            et_nickname.setText(user.summary);
        }
    }

    @OnClick(R.id.tv_head_right)
    protected void submit(){
        if (!TextUtils.isEmpty(et_nickname.getText().toString().trim())){
            submitData();
        }else {
            Util.makeToast("请先填写个性签名");
        }
    }


    protected void submitData() {
        final String summary=et_nickname.getText().toString();
        EditUserInfoActivity.isSubmitAddress=false;
        ClientDiscoverAPI.updateUserInfo("summary",summary, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null){
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)){
                    return;
                }

                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()){
                    Util.makeToast(response.getMessage());
                    Intent intent = new Intent();
                    user.summary=summary;
                    intent.putExtra(User.class.getSimpleName(),user);
                    setResult(RESULT_OK,intent);
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

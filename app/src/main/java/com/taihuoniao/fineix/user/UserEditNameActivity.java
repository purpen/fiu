package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

//import com.zcjcn.beans.HttpResponse;
//import com.zcjcn.beans.UserLogin;
//import com.zcjcn.http.HttpRequestData;
//import com.zcjcn.interfaces.ICallback4Http;
//import com.zcjcn.utils.LoginUtil;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/27 13:11
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
        if (bundle!=null){
            if (bundle.containsKey(User.class.getSimpleName())) {
                user = (User)bundle.getSerializable(User.class.getSimpleName());
            }
        }
    }

    @Override
    protected void initView() {
        ButterKnife.bind(this);
        head_view.setHeadCenterTxtShow(true, R.string.title_modify_nickname);
        head_view.setHeadRightTxtShow(true, R.string.save);
        if (user!=null){
            et_nickname.setText(user.nickname);
        }
    }

    @OnClick(R.id.tv_head_right)
    protected void submit(){
        if (!TextUtils.isEmpty(et_nickname.getText())){
            submitData();
        }else {
            Util.makeToast("请先填写昵称");
        }
    }


    protected void submitData() {
        final String nickName=et_nickname.getText().toString();
//        hashMap.clear();
//        hashMap.put("Action", "EditUserInfo");
//        hashMap.put("uid", LoginUtil.getUserId());
//        hashMap.put("key","nickname");
//        hashMap.put("data",nickName);
//        HttpRequestData.sendPostRequest(Constants.APP_URI, hashMap, new ICallback4Http() {
//            @Override
//            public void onResponse(String response) {
//                LogUtil.e(tag, response);
//                HttpResponse httpResponse = JsonUtil.fromJson(response, HttpResponse.class);
//                Util.makeToast(activity, httpResponse.getMessage());
//                Intent intent=new Intent();
//                userLogin.nickname=nickName;
//                intent.putExtra(UserLogin.class.getSimpleName(),userLogin);
//                setResult(RESULT_OK,intent);
//                finish();
//            }
//
//            @Override
//            public void onFailure(String errorMessage) {
//
//            }
//        });
    }
}

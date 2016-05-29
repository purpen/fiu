package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

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
import com.taihuoniao.fineix.view.labelview.Label;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/28 16:36
 */
public class UserEditSignatureActivity extends BaseActivity{
    @Bind(R.id.head_view)
    CustomHeadView head_view;
    @Bind(R.id.et_nickname)
    EditText et_nickname;
    private User user;
    @Bind(R.id.label_view)
    AutoLabelUI label_view;
    @Bind(R.id.tv_tag)
    TextView tv_tag;
    @Bind(R.id.tv_hint)
    TextView tv_hint;
    @Bind(R.id.iv_clear)
    ImageButton iv_clear;
    public UserEditSignatureActivity() {
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
        String[] stringArray = getResources().getStringArray(R.array.user_tags);
        for (int i = 0; i < stringArray.length; i++) {
            label_view.addLabel(stringArray[i]);
        }
        if (user!=null){
            et_nickname.setText(user.summary);
            if (!TextUtils.isEmpty(user.label)){
                tv_tag.setText(user.label);
                tv_tag.setVisibility(View.VISIBLE);
                tv_hint.setVisibility(View.GONE);
                iv_clear.setVisibility(View.VISIBLE);
            }else {
                tv_hint.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void installListener() {
        label_view.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                tv_tag.setVisibility(View.VISIBLE);
                tv_tag.setText(labelClicked.getText());
                tv_hint.setVisibility(View.GONE);
                iv_clear.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.tv_head_right,R.id.iv_clear})
    void performClick(View view){
        switch (view.getId()){
            case R.id.tv_head_right:
                submitData();
                break;
            case R.id.iv_clear:
                tv_tag.setVisibility(View.GONE);
                tv_tag.setText("");
                iv_clear.setVisibility(View.GONE);
                break;
        }
    }


    protected void submitData() {
        final String label=tv_tag.getText().toString().trim();
        final String summary=et_nickname.getText().toString().trim();
        ClientDiscoverAPI.updateSignatrueLabel(label,summary, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);

                if (response.isSuccess()){
                    Util.makeToast(response.getMessage());
                    Intent intent = new Intent();
                    user.summary=summary;
                    user.label=label;
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

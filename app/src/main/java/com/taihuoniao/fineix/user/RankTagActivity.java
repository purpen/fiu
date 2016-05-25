package com.taihuoniao.fineix.user;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.labelview.AutoLabelUI;
import com.taihuoniao.fineix.view.labelview.Label;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/22 15:49
 */
public class RankTagActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.label_view)
    AutoLabelUI label_view;
    @Bind(R.id.tv_tag)
    TextView tv_tag;
    @Bind(R.id.iv_clear)
    ImageButton iv_clear;
    @Bind(R.id.ll)
    LinearLayout ll;
    public static Activity instance;
    public RankTagActivity(){
        super(R.layout.activity_rank_tag);
    }

    @Override
    protected void initView() {
        RankTagActivity.instance=activity;
        custom_head.setHeadCenterTxtShow(true,"身份标签");
        String[] stringArray = getResources().getStringArray(R.array.user_tags);
        for (int i=0;i<stringArray.length;i++){
            label_view.addLabel(stringArray[i]);
        }
    }

    @Override
    protected void installListener() {
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ll.setOnTouchListener(new View.OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        ll.requestFocus();
                        return false;
                    }
                });
                return false;
            }
        });
        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                LogUtil.e("onFocusChange",b+"");
                if (b){
                    iv_clear.setVisibility(View.VISIBLE);
                }else {
                    if (TextUtils.isEmpty(et.getText()) && TextUtils.isEmpty(tv_tag.getText())){
                        iv_clear.setVisibility(View.GONE);
                    }else {
                        iv_clear.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        label_view.setOnLabelClickListener(new AutoLabelUI.OnLabelClickListener() {
            @Override
            public void onClickLabel(Label labelClicked) {
                tv_tag.setVisibility(View.VISIBLE);
                et.requestFocus();
                et.setHint("");
                et.setEnabled(false);
                tv_tag.setText(labelClicked.getText());
            }
        });

    }



    @OnClick({R.id.btn,R.id.iv_clear,R.id.et})
    void performClick(View view){
        switch (view.getId()){
            case R.id.btn: //申请官方认证
                startActivity(new Intent(activity,OfficialCertificateActivity.class));
                break;
            case R.id.iv_clear:
                tv_tag.setText("");
                tv_tag.setVisibility(View.GONE);
                et.requestFocus();
                et.setEnabled(true);
                et.getText().clear();
                et.setHint(R.string.select_tag_tip);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        updateUserInfo();
        super.onDestroy();
    }

    private void updateUserInfo(){
        String key="label";
        String value=et.getText().toString().trim();
        if (TextUtils.isEmpty(value)){
            value=tv_tag.getText().toString().trim();
        }

        if (TextUtils.isEmpty(value)) return;
        ClientDiscoverAPI.updateUserInfo(key, value, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()){
                    LogUtil.e(TAG,"非官方认证提交成功！");
                    return;
                }
                LogUtil.e(TAG,response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG,s);
            }
        });
    }
}

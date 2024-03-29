package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;

public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et_suggestion)
    EditText et_suggestion;
    @Bind(R.id.et_contact)
    EditText et_contact;

    public FeedbackActivity() {
        super(R.layout.activity_feedback);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "意见反馈");
        WindowUtils.chenjin(this);
    }

    @OnClick({R.id.bt_commit})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:
                if (!isUserInputLegal()) {
                    return;
                }
                HashMap<String, String> params = ClientDiscoverAPI.getcommitSuggestionRequestParams(et_suggestion.getText().toString(), et_contact.getText().toString());
                HttpRequest.post(params,  URL.SUGGESTION_URL, new GlobalDataCallBack(){
                    @Override
                    public void onSuccess(String json) {

                        if (TextUtils.isEmpty(json)) {
                            return;
                        }

                        try {
                            JSONObject response = new JSONObject(json);
                            if (response.optBoolean("success")){
                                Util.makeToast(response.optString("message"));
                                activity.finish();
                            }else {
                                Util.makeToast(response.optString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(String error) {
                        Util.makeToast(error);
                    }
                });
                break;
        }
    }

    private boolean isUserInputLegal() {
        if (TextUtils.isEmpty(et_suggestion.getText().toString().trim())) {
            Util.makeToast("您的建议不能为空，请重新输入！");
            return false;
        }

        if (et_suggestion.getText().length() > 500) {
            Util.makeToast("反馈内容过长！");
            return false;
        }
        return true;
    }

}

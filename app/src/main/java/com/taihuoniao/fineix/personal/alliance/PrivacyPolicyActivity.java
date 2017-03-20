package com.taihuoniao.fineix.personal.alliance;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.alliance.bean.PrivacyPolicyDescriptionBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/1/16 14:36
 * Email: 895745843@qq.com
 */

public class PrivacyPolicyActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView customHead;
    @Bind(R.id.textView1)
    TextView textView1;

    public PrivacyPolicyActivity() {
        super(R.layout.activity_alliance_privacy_policy);
    }

    @Override
    protected void initView() {
        customHead.setHeadCenterTxtShow(true, "结算规则");
        WindowUtils.chenjin(this);
    }


    @Override
    protected void requestNet() {
//        super.requestNet();
        requestData();
    }

    private void requestData() {
        HashMap<String, String> hashMap = ClientDiscoverAPI.getallianceAccount();
        HttpRequest.post(hashMap, URL.ALLIANCE_BALANCE_PRIVACY_POLICY, new GlobalDataCallBack() {
            @Override
            public void onSuccess(String json) {
                HttpResponse<PrivacyPolicyDescriptionBean> httpResponse = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<PrivacyPolicyDescriptionBean>>() {
                });
                if (httpResponse.isSuccess()) {
                    dealUI(httpResponse.getData());
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    private void dealUI(PrivacyPolicyDescriptionBean myAccountBean) {
        if (myAccountBean == null) {
            return;
        }
        textView1.setText(myAccountBean.getInfo());
    }
}

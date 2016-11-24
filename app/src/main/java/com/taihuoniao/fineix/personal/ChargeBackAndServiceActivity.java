package com.taihuoniao.fineix.personal;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

public class ChargeBackAndServiceActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    private static final String TITLE = "退款/售后";

    public ChargeBackAndServiceActivity() {
        super(R.layout.activity_chargebackservice);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
        ReturnGoodsFragment shopOrderFragment = new ReturnGoodsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.linearLayout_container, shopOrderFragment, "").commit();

        requestService();
    }

    private void requestService(){
        ClientDiscoverAPI.getRefundList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }
}

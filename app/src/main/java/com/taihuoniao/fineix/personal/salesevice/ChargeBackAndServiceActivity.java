package com.taihuoniao.fineix.personal.salesevice;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackListBean;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

public class ChargeBackAndServiceActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    public ChargeBackAndServiceActivity() {
        super(R.layout.activity_chargebackservice);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, App.getString(R.string.title_salesAfterList));
        WindowUtils.chenjin(this);
        ReturnGoodsFragment shopOrderFragment = new ReturnGoodsFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.linearLayout_container, shopOrderFragment, "").commit();
    }
}

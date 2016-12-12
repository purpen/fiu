package com.taihuoniao.fineix.personal.salesevice;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.App;
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

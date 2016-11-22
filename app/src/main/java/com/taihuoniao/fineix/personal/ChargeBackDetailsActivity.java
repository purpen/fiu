package com.taihuoniao.fineix.personal;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.user.returnGoods.ReturnGoodsFragment;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;

public class ChargeBackDetailsActivity extends BaseActivity {

    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    private static final String TITLE = "退款详情";

    public ChargeBackDetailsActivity() {
        super(R.layout.activity_chargeback_details);
    }

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, TITLE);
        WindowUtils.chenjin(this);
    }
}

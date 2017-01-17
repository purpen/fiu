package com.taihuoniao.fineix.personal.alliance;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

/**
 * Created by Stephen on 2017/1/16 17:08
 * Email: 895745843@qq.com
 */

public class WithdrawRecordActivity extends BaseActivity {

    public WithdrawRecordActivity() {
        super(R.layout.activity_alliance_withdraw_record);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.custom_head);
        customHead.setHeadCenterTxtShow(true, "提现记录");
        WindowUtils.chenjin(this);
    }
}

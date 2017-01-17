package com.taihuoniao.fineix.personal.alliance;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

/**
 * Created by Stephen on 2017/1/16 17:10
 * Email: 895745843@qq.com
 */

public class SettlementRecordActivity extends BaseActivity {


    public SettlementRecordActivity() {
        super(R.layout.activity_alliance_settlement_record);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.custom_head);
        customHead.setHeadCenterTxtShow(true, "结算记录");
        WindowUtils.chenjin(this);
    }
}

package com.taihuoniao.fineix.personal.alliance;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;

/**
 * Created by Stephen on 2017/1/16 17:11
 * Email: 895745843@qq.com
 */

public class SettlementRecordDetailsActivity extends BaseActivity {

    public SettlementRecordDetailsActivity() {
        super(R.layout.activity_alliance_settlement_record_details);
    }

    @Override
    protected void initView() {
        CustomHeadView customHead = (CustomHeadView) findViewById(R.id.custom_head);
        customHead.setHeadCenterTxtShow(true, "结算明细");
        WindowUtils.chenjin(this);
    }
}

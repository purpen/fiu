package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/22 15:49
 */
public class RankTagActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    public RankTagActivity(){
        super(R.layout.activity_rank_tag);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"身份标签");

    }
    @OnClick({R.id.btn})
    void performClick(View view){
        switch (view.getId()){
            case R.id.btn: //申请官方认证
                startActivity(new Intent(activity,OfficialCertificateActivity.class));
                break;
        }
    }

}

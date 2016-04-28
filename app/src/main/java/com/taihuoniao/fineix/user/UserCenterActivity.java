package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity{
    @Bind(R.id.fl_box)
    FrameLayout fl_box;
    public UserCenterActivity(){
        super(R.layout.activity_user_center);
    }

    @Override
    protected void initView() {
        
    }

    @OnClick({R.id.iv_right,R.id.iv_detail})
    void onClick(View v){
        switch (v.getId()){
            case R.id.iv_detail:
                finish();
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity,EditUserInfoActivity.class));
                break;
        }
    }
}

package com.taihuoniao.fineix.user;

import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.CustomHeadView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/5/22 18:47
 */
public class OfficialCertificateActivity extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;

    public OfficialCertificateActivity(){
        super(R.layout.activity_official_certificate);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"官方认证");

    }

    @OnClick({R.id.btn})
    void performClick(View view){
        switch (view.getId()){
            case R.id.btn: //提交

                break;
            case R.id.rl_card: //上传名片 相册/拍照

                break;
            case R.id.rl_id: //上传身份证 相册/拍照

                break;
        }
    }
}

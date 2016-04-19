package com.taihuoniao.fineix.user;
import android.view.WindowManager;
import android.widget.EditText;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;


public class ToFindPassWordActivity extends BaseActivity {
    private EditText editPhoneNum;

    public ToFindPassWordActivity(){
        super(R.layout.activity_to_find_pass_word);
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

}

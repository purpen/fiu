package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;

public class OptRegisterLoginActivity extends BaseActivity implements View.OnClickListener {
    private SystemBarTintManager tintManager;
    private View mDecorView;
    public static OptRegisterLoginActivity instance;
    private ImageView mClose;
    private TextView mToRegister;
    private TextView mToLogin;

    public OptRegisterLoginActivity() {
        super(R.layout.activity_opt_register_login);
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        overridePendingTransition(0, R.anim.down_register);
    }

    @Override
    protected void initView() {
        instance = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        overridePendingTransition(R.anim.up_register, 0);
        mClose = (ImageView) findViewById(R.id.image_close_reg_login);
        mClose.setOnClickListener(this);
        mToRegister = (TextView) findViewById(R.id.tv_click_register);
        mToRegister.setOnClickListener(this);
        mToLogin = (TextView) findViewById(R.id.tv_click_login);
        mToLogin.setOnClickListener(this);
        mToLogin.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        mToLogin.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_close_reg_login:
                finish();
                break;
            case R.id.tv_click_register:
                startActivity(new Intent(this, ToRegisterActivity.class));
                break;
            case R.id.tv_click_login:
                Intent intentLogin = new Intent(this, ToLoginActivity.class);
//                intentLogin.putExtra("flag", getIntent().getIntExtra("flag", 0));
                startActivity(intentLogin);
                break;
        }
    }
}

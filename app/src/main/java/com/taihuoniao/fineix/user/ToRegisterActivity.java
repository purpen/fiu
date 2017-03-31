package com.taihuoniao.fineix.user;

import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FragmentViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.RegisterInfo;
import com.taihuoniao.fineix.user.fragments.SetPasswordFragment;
import com.taihuoniao.fineix.user.fragments.SubmitCheckCodeFragment;
import com.taihuoniao.fineix.view.CustomViewPager;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

public class ToRegisterActivity extends BaseActivity {
    @Bind(R.id.ibtn_back)
    ImageButton ibtnBack;
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;
    private RegisterInfo registerInfo;
    private Boolean mFinish = false;//结束当前activity时是以左右动画方式退出,改为false则以上下动画退出
    public static ToRegisterActivity instance = null;

    public ToRegisterActivity() {
        super(R.layout.activity_to_register);
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ShareSDK.initSDK(this);//必须先在程序入口处初始化SDK
        instance = this;
        viewPager.setPagingEnabled(false);
        registerInfo = new RegisterInfo();
        Fragment[] fragments = {SendCheckCodeFragment.newInstance(), SubmitCheckCodeFragment.newInstance(), SetPasswordFragment.newInstance()};
        viewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
    }

    public RegisterInfo getRegisterInfo() {
        return null == registerInfo ? null : registerInfo;
    }

    public CustomViewPager getViewPager() {
        return null == viewPager ? null : viewPager;
    }

    public void finish() {
        super.finish();
        if (mFinish) {
            //关闭窗体动画显示
            overridePendingTransition(0, R.anim.down_register);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.ibtn_back)
    public void onClick() {
        if (viewPager == null) return;
        int currentItem = viewPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                mFinish = true;
                finish();
                break;
            case 1:
                viewPager.setCurrentItem(0, true);
                break;
            case 2:
                viewPager.setCurrentItem(1, true);
                break;
            default:
                break;
        }
    }
}

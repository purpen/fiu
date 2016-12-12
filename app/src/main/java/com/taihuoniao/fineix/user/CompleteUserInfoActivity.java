package com.taihuoniao.fineix.user;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FragmentViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.UserCompleteData;
import com.taihuoniao.fineix.user.fragments.CompleteAvatarNickNameFragment;
import com.taihuoniao.fineix.user.fragments.DecadeSelectFragment;
import com.taihuoniao.fineix.user.fragments.FocusUserFragment;
import com.taihuoniao.fineix.user.fragments.SubscribeThemeFragment;
import com.taihuoniao.fineix.view.CustomViewPager;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/9 14:42
 */
public class CompleteUserInfoActivity extends BaseActivity {
    @Bind(R.id.ibtn_back)
    ImageButton ibtn_back;
    @Bind(R.id.viewPager)
    CustomViewPager viewPager;

    public CompleteUserInfoActivity() {
        super(R.layout.activity_complete_user_info);
    }

    @Override
    protected void initView() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        viewPager.setPagingEnabled(false);
        UserCompleteData data = new UserCompleteData();
        Fragment[] fragments = {CompleteAvatarNickNameFragment.newInstance(data), DecadeSelectFragment.newInstance(data), SubscribeThemeFragment.newInstance(data), FocusUserFragment.newInstance(data)};
        viewPager.setAdapter(new FragmentViewPagerAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    ibtn_back.setVisibility(View.VISIBLE);
                } else {
                    ibtn_back.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public CustomViewPager getViewPager() {
        return null == viewPager ? null : viewPager;
    }

    @OnClick(R.id.ibtn_back)
    public void onClick() {
        if (viewPager == null) return;
        int currentItem = viewPager.getCurrentItem();
        switch (currentItem) {
            case 1:
                viewPager.setCurrentItem(0, true);
                break;
            case 2:
                viewPager.setCurrentItem(1, true);
                break;
            case 3:
                viewPager.setCurrentItem(2, true);
                break;
            default:
                break;
        }
    }
}

package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.view.ScrollableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/4/18 16:10
 */
public class UserGuideActivity extends BaseActivity {
    @Bind(R.id.scrollableView)
    ScrollableView scrollableView;
    @Bind(R.id.iv_welcome)
    ImageView iv_welcome;
    private Intent intent;
    private boolean isGuide=false;
    private List<Integer> list;
    public static String fromPage;
    public UserGuideActivity() {
        super(R.layout.activity_user_guide_layout);
    }

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()){
            intent=getIntent();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)&&TextUtils.equals(Intent.ACTION_MAIN,intent.getAction())){
                finish();
                return;
            }
        }
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
        intent = getIntent();
        if (intent.hasExtra(SystemSettingsActivity.class.getSimpleName())){
            fromPage=intent.getStringExtra(SystemSettingsActivity.class.getSimpleName());
        }
    }
    @Override
    protected void initView() {
        if (TextUtils.isEmpty(fromPage)){
            iv_welcome.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage("drawable://"+R.mipmap.welcome,iv_welcome);
//            iv_welcome.setImageResource(R.mipmap.login_or_regist);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_welcome.setVisibility(View.GONE);
                    iv_welcome.setImageResource(0);
                    if (TextUtils.isEmpty(SPUtil.read(activity,DataConstants.GUIDE_TAG))) {
                        SPUtil.write(activity,DataConstants.GUIDE_TAG, DataConstants.GUIDE_TAG);
                        initGuide();
                    } else if(isTaskRoot()){
                        goMainPage();
                    }
                }
            }, DataConstants.GUIDE_INTERVAL);
        }else {
            initGuide();
        }
    }

    private void initGuide() {
        isGuide=true;
        list = new ArrayList<Integer>();
        list.add(R.mipmap.guide1);
        list.add(R.mipmap.login_or_regist);
        list.add(R.mipmap.login_or_regist);
        scrollableView.setAdapter(new ViewPagerAdapter<>(activity, list));
        scrollableView.showIndicators();
    }

    private void goMainPage(){
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isGuide){
            goMainPage();
        }else {
            super.onBackPressed();
        }
    }
}

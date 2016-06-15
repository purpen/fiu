package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

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
 *         created at 2016/4/18 16:10
 */
public class UserGuideActivity extends BaseActivity {
    public interface OnVideoCompleteListener {
        void execute();
    }

    private static OnVideoCompleteListener onVideoCompleteListener;

    public static void setOnVideoCompleteListener(OnVideoCompleteListener listener) {
        UserGuideActivity.onVideoCompleteListener = listener;
    }
    @Bind(R.id.scrollableView)
    ScrollableView scrollableView;
    @Bind(R.id.iv_welcome)
    ImageView iv_welcome;
    @Bind(R.id.activity_video_view)
    VideoView activityVideoView;
    private Intent intent;
    private boolean isGuide = false;
    private List<Integer> list;
    public static String fromPage;

    public UserGuideActivity() {
        super(R.layout.activity_user_guide_layout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && TextUtils.equals(Intent.ACTION_MAIN, intent.getAction())) {
                finish();
            }
        }
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
        intent = getIntent();
        if (intent.hasExtra(SystemSettingsActivity.class.getSimpleName())) {
            fromPage = intent.getStringExtra(SystemSettingsActivity.class.getSimpleName());
        }
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(fromPage)) {
            iv_welcome.setVisibility(View.VISIBLE);
            iv_welcome.setImageResource(R.mipmap.welcome);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_welcome.setVisibility(View.GONE);
                    if (TextUtils.isEmpty(SPUtil.read(activity, DataConstants.GUIDE_TAG))) {
                        SPUtil.write(activity, DataConstants.GUIDE_TAG, DataConstants.GUIDE_TAG);
                        initGuide();
                    } else if (isTaskRoot()) {
                        goMainPage();
                    }
                }
            }, DataConstants.GUIDE_INTERVAL);
        } else {
            initGuide();
        }
    }

    public void initVideoRes() {
        scrollableView.setVisibility(View.GONE);
        activityVideoView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT,
                RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        activityVideoView.setLayoutParams(layoutParams);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.first_in_app;
        activityVideoView.setVideoURI(Uri.parse(uri));
        activityVideoView.start();
        activityVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (onVideoCompleteListener != null) onVideoCompleteListener.execute();
            }
        });
    }

    private void initGuide() {
        if (TextUtils.isEmpty(fromPage)) {
            isGuide = true;
        }
        list = new ArrayList<>();
        list.add(R.mipmap.guide0);
        list.add(R.mipmap.guide1);
        list.add(R.mipmap.guide2);
        list.add(R.mipmap.guide3);
        scrollableView.setAdapter(new ViewPagerAdapter<>(activity, list));
//        scrollableView.showIndicators();
    }

    private void goMainPage() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        if (isGuide) {
            goMainPage();
        } else {
            super.onBackPressed();
        }
    }


}

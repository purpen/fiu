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

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/18 16:10
 */
public class UserGuideActivity extends BaseActivity {
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
    private int currentPosition;

    public UserGuideActivity() {
        super(R.layout.activity_user_guide_layout);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            currentPosition = savedInstanceState.getInt("currentPosition");
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
                    if (TextUtils.isEmpty(SPUtil.read(DataConstants.GUIDE_TAG))) {
                        initVideoRes();
                    } else if (isTaskRoot()) {
                        if (SPUtil.readBool(DataConstants.INVITE_CODE_TAG)) {
                            showInvite();
                        } else {
                            goMainPage();
                        }
                    }
                }
            }, DataConstants.GUIDE_INTERVAL);
        } else {
            initGuide();
        }
    }


    private void initVideoRes() {
        scrollableView.setVisibility(View.GONE);
        activityVideoView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        activityVideoView.setLayoutParams(layoutParams);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.first_in_app;
        activityVideoView.setVideoURI(Uri.parse(uri));
        activityVideoView.start();
        SPUtil.write(DataConstants.GUIDE_TAG, DataConstants.GUIDE_TAG);
        activityVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                initGuide();
            }
        });
        activityVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                initGuide();
                return true;
            }
        });

    }

    private void showInvite() {
        scrollableView.setVisibility(View.VISIBLE);
        activityVideoView.setVisibility(View.GONE);
        list = new ArrayList<>();
        list.add(R.mipmap.guide3);
        scrollableView.setAdapter(new ViewPagerAdapter<>(activity, list));
    }

    private void initGuide() {
        String idfa = Util.getAppMetaData(getResources().getString(R.string.channel_name));
        LogUtil.e(TAG, "渠道为:" + idfa);
        ClientDiscoverAPI.activeStatus(idfa, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (!response.isSuccess()) {
                    LogUtil.e(TAG, "提交渠道失败信息:" + response.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG, "网络异常,请确保网络畅通");
            }
        });
        scrollableView.setVisibility(View.VISIBLE);
        activityVideoView.setVisibility(View.GONE);
        if (TextUtils.isEmpty(fromPage)) {
            isGuide = true;
        }
        list = new ArrayList<>();
        list.add(R.mipmap.guide0);
        list.add(R.mipmap.guide1);
        list.add(R.mipmap.guide2);
        list.add(R.mipmap.guide3);
        scrollableView.setAdapter(new ViewPagerAdapter<>(activity, list));
    }


    private void goMainPage() {
        startActivity(new Intent(activity, MainActivity.class));
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (null == activityVideoView) return;
        activityVideoView.seekTo(currentPosition);
        activityVideoView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null == activityVideoView) return;
        if (activityVideoView.isPlaying()) {
            currentPosition = activityVideoView.getCurrentPosition();
            activityVideoView.pause();
        }
    }

    //    @Override
//    public void onBackPressed() {
//        if (isGuide) {
//            goMainPage();
//        } else {
//            super.onBackPressed();
//        }
//    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("currentPosition", currentPosition);
        super.onSaveInstanceState(outState);
    }
}

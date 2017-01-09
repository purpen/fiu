package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.SPUtil;
import com.taihuoniao.fineix.view.ScrollableView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

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
    @Bind(R.id.rl_video)
    RelativeLayout rlVideo;
    @Bind(R.id.ibn_volume)
    ImageButton ibnVolume;
    private Intent intent;
    private List<Integer> list;
    private boolean flag = false;
    public static String fromPage;
    private int currentPosition;
    private MediaPlayer mediaPlayer;

    private boolean empty;
    private boolean taskRoot;
    private boolean readBool;

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
        empty = TextUtils.isEmpty(SPUtil.read(DataConstants.GUIDE_TAG));
        taskRoot = isTaskRoot();
        readBool = SPUtil.readBool(DataConstants.INVITE_CODE_TAG);
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(fromPage)) {
            iv_welcome.setImageResource(R.mipmap.welcome);
            iv_welcome.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    iv_welcome.setVisibility(View.GONE);
                    if (empty) {
                        initVideoRes();
                    } else {
                        if (taskRoot) {
                            if (readBool) {
                                showInvite();
                            } else {
                                goMainPage();
                            }
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
        rlVideo.setVisibility(View.VISIBLE);
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
        activityVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mediaPlayer = mp;
                setVolume(0);
            }
        });
    }

    private void showInvite() {
        scrollableView.setVisibility(View.VISIBLE);
        rlVideo.setVisibility(View.GONE);
        list = new ArrayList<>();
        list.add(R.mipmap.guide3);
        scrollableView.setAdapter(new ViewPagerAdapter<>(activity, list));
    }

    private void initGuide() {
        RequestParams params = ClientDiscoverAPI.getactiveStatusRequestParams();
        HttpRequest.post(params, URL.GATEWAY_RECORD_FIU_USER_ACTIVE, new GlobalDataCallBack(){
//        ClientDiscoverAPI.activeStatus(new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                LogUtil.e(TAG,json);
                HttpResponse response = JsonUtil.fromJson(json, HttpResponse.class);
                if (!response.isSuccess()) {
                    LogUtil.e(TAG, "提交渠道失败信息:" + response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                LogUtil.e(TAG, "网络异常,请确保网络畅通");
            }
        });
        rlVideo.setVisibility(View.GONE);
        activityVideoView = null;
        scrollableView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(fromPage)) {
            boolean isGuide = true;
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


    @OnClick({R.id.ibn_volume, R.id.btn_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibn_volume:
                if (mediaPlayer == null) return;
                if (flag) {
                    ibnVolume.setImageResource(R.mipmap.volume_mute);
                    flag = false;
                    setVolume(0);
                } else {
                    ibnVolume.setImageResource(R.mipmap.volume);
                    flag = true;
                    setVolume(50);
                }
                break;
            case R.id.btn_pass:
                if (activityVideoView.isPlaying()) {
                    rlVideo.setVisibility(View.GONE);
                    mediaPlayer.stop();
                    activityVideoView = null;
                    mediaPlayer = null;
                    initGuide();
                }
                break;
        }
    }

    private void setVolume(int amount) {
        final int max = 100;
        final double numerator = max - amount > 0 ? Math.log(max - amount) : 0;
        final float volume = (float) (1 - (numerator / Math.log(max)));
        this.mediaPlayer.setVolume(volume, volume);
    }
}

package com.taihuoniao.fineix.main;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;

/**
 * Created by taihuoniao on 2016/6/12.
 */
public class VideoActivity extends BaseActivity {
    public VideoActivity() {
        super(0);
    }

    @Override
    protected void initView() {
        VideoView videoView = new VideoView(VideoActivity.this);
        videoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(videoView);
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.first_in_app;
        videoView.setVideoURI(Uri.parse(uri));
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.e("<<<播放完毕", "界面跳转");
            }
        });


    }

}

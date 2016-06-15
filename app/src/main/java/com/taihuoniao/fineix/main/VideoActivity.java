package com.taihuoniao.fineix.main;

import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.widget.VideoView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/6/12.
 */
public class VideoActivity extends BaseActivity {
    @Bind(R.id.activity_video_view)
    VideoView videoView;
    public VideoActivity() {
        super(R.layout.activity_video);
    }

    @Override
    protected void initView() {
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

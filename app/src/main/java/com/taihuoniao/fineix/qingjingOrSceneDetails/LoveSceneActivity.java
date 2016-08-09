package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.widget.ListView;
import android.widget.ProgressBar;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/2.
 */
public class LoveSceneActivity extends BaseActivity {
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    private ListView listView;

    public LoveSceneActivity() {
        super(R.layout.activity_love_scene);
    }

    @Override
    protected void initView() {
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setContinueTvVisible(false);
        titleLayout.setTitle(R.string.love_person, getResources().getColor(R.color.black333333));
        listView = pullRefreshView.getRefreshableView();
        pullRefreshView.setPullToRefreshEnabled(false);
    }

    @Override
    protected void requestNet() {

    }


}

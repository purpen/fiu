package com.taihuoniao.fineix.user;

import android.widget.ProgressBar;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/30.
 * 已经订阅的情景
 */
public class HasSubsActivity extends BaseActivity {
    @Bind(R.id.activity_has_subs_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_has_subs_pulltorefreshview)
    PullToRefreshGridView pullToRefreshView;
    @Bind(R.id.activity_has_subs_progress)
    ProgressBar progressBar;

    public HasSubsActivity() {
        super(R.layout.activity_has_subs);
    }

    @Override
    protected void initView() {

    }
}

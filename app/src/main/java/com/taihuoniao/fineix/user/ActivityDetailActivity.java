package com.taihuoniao.fineix.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CollectViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.user.fragments.ActivityResultFragment;
import com.taihuoniao.fineix.user.fragments.ParticipateQJFragment;
import com.taihuoniao.fineix.user.fragments.RuleFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Field;
import java.util.Arrays;

import butterknife.Bind;

/**
 * @author lilin  活动详情
 *         created at 2016/8/23 10:28
 */
public class ActivityDetailActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_desc)
    TextView tvDesc;
    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.tv_during)
    TextView tvDuring;

    private CollectViewPagerAdapter adapter;
    private WaittingDialog dialog;
    private String id;
    private SubjectData data;

    public ActivityDetailActivity() {
        super(R.layout.activity_activity_detail);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            id = savedInstanceState.getString(TAG);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            id = intent.getStringExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "活动详情");
        dialog = new WaittingDialog(this);
    }

    private void initTabLayout() {
        if (data == null) return;
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("summary", data.summary);
        bundle.putInt("evt", data.evt);
        String[] array = getResources().getStringArray(R.array.activity_detail_tab);
        Fragment[] fragments = {RuleFragment.newInstance(bundle), ParticipateQJFragment.newInstance(id), ActivityResultFragment.newInstance(data.sights)};
        if (data.evt != 2) {//2活动未结束展示两项
            array = Arrays.copyOf(array, 2);
            fragments = Arrays.copyOf(fragments, 2);
        }
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                tabLayout.addTab(tabLayout.newTab().setText(array[0]), true);
            } else {
                tabLayout.addTab(tabLayout.newTab().setText(array[i]), false);
            }
        }

        adapter = new CollectViewPagerAdapter(getSupportFragmentManager(), fragments, array);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setIndicatorWidth() throws NoSuchFieldException, IllegalAccessException {
        int margin = activity.getResources().getDimensionPixelSize(R.dimen.dp25);
        Class<?> tablayout = tabLayout.getClass();
        Field tabStrip = tablayout.getDeclaredField("mTabStrip");
        tabStrip.setAccessible(true);
        LinearLayout ll_tab = (LinearLayout) tabStrip.get(tabLayout);
        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                params.setMarginStart(margin);
                params.setMarginEnd(margin);
            } else {
                params.setMargins(margin, 0, margin, 0);
            }
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    protected void installListener() {
        tabLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    setIndicatorWidth();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(id)) return;
        ClientDiscoverAPI.getSubjectData(id, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<SubjectData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<SubjectData>>() {
                });

                if (response.isSuccess()) {
                    data = response.getData();
                    refreshUI();
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (data == null) return;
        ImageLoader.getInstance().displayImage(data.banner_url, imageView);
        if (!TextUtils.isEmpty(data.title)) {
            tvTitle.setText(data.title);
            tvTitle.setBackgroundColor(getResources().getColor(android.R.color.black));
            tvTitle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = tvTitle.getMeasuredWidth();
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, activity.getResources().getDimensionPixelSize(R.dimen.dp2));
                    params.gravity = Gravity.CENTER_HORIZONTAL;
                    viewLine.setLayoutParams(params);
                    viewLine.setBackgroundColor(activity.getResources().getColor(R.color.color_af8323));
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                        tvTitle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        tvTitle.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }
        tvDesc.setText(data.short_title);
        tvDuring.setText(String.format("%s-%s", data.begin_time_at, data.end_time_at));
        initTabLayout();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(TAG, id);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putString(TAG, id);
        super.onSaveInstanceState(outState, outPersistentState);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(DataConstants.BroadSceneActivityDetail, intent.getAction())) {
                if (TextUtils.isEmpty(id)) return;
                requestNet();
            }
        }
    };
}

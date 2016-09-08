package com.taihuoniao.fineix.user;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ActivityResultAdapter;
import com.taihuoniao.fineix.adapters.ParticipateQJListAdapter;
import com.taihuoniao.fineix.adapters.RuleContentAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.DataParticipateQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin  活动详情
 *         created at 2016/8/23 10:28
 */
public class ActivityDetailActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_gv)
    ListView pullGv;
    @Bind(R.id.pull_lv)
    ListView pullLv;
    @Bind(R.id.btn_participate)
    Button btn_participate;
    @Bind(R.id.pull_rule)
    ListView pullRule;
    //    @Bind(R.id.imageView)
    ImageView imageView;
    //    @Bind(R.id.tv_title)
    TextView tvTitle;
    //    @Bind(R.id.tv_desc)
    TextView tvDesc;
    //    @Bind(R.id.tabLayout)
//    TabLayout tabLayout;
//    @Bind(R.id.viewPager)
//    ViewPager viewPager;
//    @Bind(R.id.view_line)
    View viewLine;
    //    @Bind(R.id.tv_during)
    TextView tvDuring;
    RelativeLayout rlRule;
    RelativeLayout rlParticipate;
    RelativeLayout rlResult;
    private ParticipateQJListAdapter adapter;
    private WaittingDialog dialog;
    private String id;
    private SubjectData data;
    private View lineRule;
    private View lineParticipate;
    private View lineResult;
    private int curPage = 1;
    private ArrayList<DataParticipateQJ.ItemParticipateQJ> mList = new ArrayList<>();

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
        View view = Util.inflateView(R.layout.activity_detail_head, null);
        imageView = ButterKnife.findById(view, R.id.imageView);
        tvTitle = ButterKnife.findById(view, R.id.tv_title);
        tvDesc = ButterKnife.findById(view, R.id.tv_desc);
        viewLine = ButterKnife.findById(view, R.id.view_line);
        tvDuring = ButterKnife.findById(view, R.id.tv_during);
        rlRule = ButterKnife.findById(view, R.id.rl_rule);
        rlParticipate = ButterKnife.findById(view, R.id.rl_participate);
        rlResult = ButterKnife.findById(view, R.id.rl_result);
        lineRule = ButterKnife.findById(view, R.id.line_rule);
        lineParticipate = ButterKnife.findById(view, R.id.line_participate);
        lineResult = ButterKnife.findById(view, R.id.line_result);

        pullRule.addHeaderView(view);
        pullRule.setVisibility(View.VISIBLE);
        pullGv.addHeaderView(view);
        pullLv.addHeaderView(view);
        pullLv.setAdapter(null);
        pullGv.setAdapter(null);
        pullRule.setAdapter(null);
        loadData();
    }
    @Override
    protected void installListener() {
        rlRule.setOnClickListener(this);
        rlParticipate.setOnClickListener(this);
        rlResult.setOnClickListener(this);
        btn_participate.setOnClickListener(this);
        pullGv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    if (mList.size() % 2 == 0) {
                        if (view.getLastVisiblePosition() == mList.size() / 2) {
                            LogUtil.e("curPage==偶数", curPage + "");
                            loadData();
                        }
                    } else {
                        if (view.getLastVisiblePosition() == mList.size() / 2 + 1) {
                            LogUtil.e("curPage==奇数", curPage + "");
                            loadData();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_rule:
                resetUI();
                pullRule.setVisibility(View.VISIBLE);
                lineRule.setVisibility(View.VISIBLE);
                btn_participate.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_participate:
                resetUI();
                pullGv.setVisibility(View.VISIBLE);
                lineParticipate.setVisibility(View.VISIBLE);
                break;
            case R.id.rl_result:
                resetUI();
                pullLv.setVisibility(View.VISIBLE);
                lineResult.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_participate:
                Intent intent = new Intent(activity, SelectPhotoOrCameraActivity.class);
                MainApplication.id = String.valueOf(data._id);
                activity.startActivity(intent);
                break;
        }
    }

    private void resetUI() {
        btn_participate.setVisibility(View.GONE);
        pullRule.setVisibility(View.GONE);
        pullGv.setVisibility(View.GONE);
        pullLv.setVisibility(View.GONE);
        lineRule.setVisibility(View.INVISIBLE);
        lineParticipate.setVisibility(View.INVISIBLE);
        lineResult.setVisibility(View.INVISIBLE);
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

    /**
     * 加载参与情境数据
     */
    protected void loadData() {
        if (TextUtils.isEmpty(id)) return;
        LogUtil.e(TAG, id);
        ClientDiscoverAPI.participateActivity(String.valueOf(curPage), id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<DataParticipateQJ> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<DataParticipateQJ>>() {
                });

                if (response.isSuccess()) {
                    ArrayList list = response.getData().rows;
                    if (list == null || list.size() == 0) return;
                    curPage++;
                    mList.addAll(list);
                    if (adapter == null) {
                        adapter = new ParticipateQJListAdapter(mList, activity);
                        pullGv.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                e.printStackTrace();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (data == null) return;
        ImageLoader.getInstance().displayImage(data.banner_url, imageView);
        tvDesc.setText(data.short_title);
        if (data.evt == 2) {
            rlRule.setVisibility(View.VISIBLE);
            rlParticipate.setVisibility(View.VISIBLE);
            rlResult.setVisibility(View.VISIBLE);
            tvDuring.setText("已结束");
        } else {
            if (data.evt == 0) {
                rlParticipate.setVisibility(View.GONE);
            } else {
                rlParticipate.setVisibility(View.VISIBLE);
            }
            rlResult.setVisibility(View.GONE);
            rlRule.setVisibility(View.VISIBLE);
            btn_participate.setVisibility(View.VISIBLE);
            tvDuring.setText(String.format("%s-%s", data.begin_time_at, data.end_time_at));
        }
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

        ActivityResultAdapter resultAdapter = new ActivityResultAdapter(data.sights, activity);
        pullRule.setAdapter(new RuleContentAdapter(data, activity));
        pullLv.setAdapter(resultAdapter);
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

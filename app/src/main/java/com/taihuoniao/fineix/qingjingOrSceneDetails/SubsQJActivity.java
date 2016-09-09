package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SubsListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.UserInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.user.SubscribeThemeActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/2.
 */
public class SubsQJActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.has_subs_count_relative)
    RelativeLayout hasSubsCountRelative;
    @Bind(R.id.has_subs_count_tv)
    TextView hasSubsCountTv;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshGridView pullRefreshView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private GridView gridView;
    private WaittingDialog dialog;

    private int page = 1;//订阅情景列表页码
    private List<SceneList.DataBean.RowsBean> sceneList;//订阅的情景列表数据
    private SubsListAdapter subsListAdapter;//订阅情景列表适配器
    private ArrayList<String> subsId;//订阅的情景主题
    private String ids;//订阅的情景主题id

    public SubsQJActivity() {
        super(R.layout.activity_subs);
    }

    @Override
    protected void initView() {
        titleLayout.setBackImg(R.mipmap.back_white);
        titleLayout.setTitle(R.string.subs, getResources().getColor(R.color.white));
        titleLayout.setContinueTvVisible(false);
        hasSubsCountRelative.setOnClickListener(this);
        pullRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                page = 1;
                requestNet();
            }
        });
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                getSubsQJ();
            }
        });
        gridView = pullRefreshView.getRefreshableView();
        gridView.setSelector(R.color.nothing);
        gridView.setNumColumns(2);
        gridView.setHorizontalSpacing(DensityUtils.dp2px(this, 15));
        gridView.setVerticalSpacing(DensityUtils.dp2px(this, 15));
        //设置适配器
        sceneList = new ArrayList<>();
        subsListAdapter = new SubsListAdapter(this, sceneList);
        gridView.setAdapter(subsListAdapter);
        WindowUtils.chenjin(this);
        dialog = new WaittingDialog(this);
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    protected void requestNet() {
        hasSubsCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.has_subs_count_relative:
                if (subsId == null) {
                    if (!dialog.isShowing()) {
                        dialog.show();
                    }
                    page = 1;
                    requestNet();
                    return;
                }
                Intent intent = new Intent(this, SubscribeThemeActivity.class);
                intent.putExtra(SubscribeThemeActivity.class.getSimpleName(), subsId);
                startActivityForResult(intent, 1);
                break;
        }
    }
    private HttpHandler<String> listHandler;
    //获取订阅的情景
    private void getSubsQJ() {
      listHandler =   ClientDiscoverAPI.getSceneList(page + "", 8 + "", null, ids, null, null, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<情景列表", responseInfo.result);
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SceneList sceneL = new SceneList();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SceneList>() {
                    }.getType();
                    sceneL = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "情景列表解析异常" + e.toString());
                }
                if (sceneL.isSuccess()) {
                    subsListAdapter.setPage(sceneL.getData().getCurrent_page());
                    pullRefreshView.setLoadingTime();
                    if (page == 1) {
                        sceneList.clear();
                        pullRefreshView.lastTotalItem = -1;
                        pullRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getData().getRows());
                    subsListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //获取订阅情境主题个数
    private void hasSubsCount() {
        ClientDiscoverAPI.getUserCenterData(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<个人信息", responseInfo.result);
                UserInfo userInfo = new UserInfo();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<UserInfo>() {
                    }.getType();
                    userInfo = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<个人信息", "解析异常=" + e.toString());
                }
                if (userInfo.isSuccess()) {
                    hasSubsCountTv.setText("已订阅" + userInfo.getData().getInterest_scene_cate().size() + "个情境主题");
                    subsId = new ArrayList<String>();
                    subsId.addAll(userInfo.getData().getInterest_scene_cate());
                    StringBuilder ids = new StringBuilder();
                    for (String id : userInfo.getData().getInterest_scene_cate()) {
                        ids.append(",").append(id);
                    }
                    if (ids.length() > 0) {
                        ids.deleteCharAt(0);
                        SubsQJActivity.this.ids = ids.toString();
                        subsListAdapter.setIds(ids.toString());
                        getSubsQJ();
                        return;
                    }
                    SubsQJActivity.this.ids = null;
                    progressBar.setVisibility(View.GONE);
                    dialog.dismiss();
                    pullRefreshView.onRefreshComplete();
                    sceneList.clear();
                    subsListAdapter.notifyDataSetChanged();
                    return;
                }
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                ToastUtils.showError(userInfo.getMessage());
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (resultCode) {
            case RESULT_OK:
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                page = 1;
                requestNet();
                break;
        }
    }
}

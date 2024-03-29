package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SubsListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.UserInfoBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.user.SubscribeThemeActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 *
 * 查看订阅
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
    private WaittingDialog dialog;

    private int page = 1;//订阅情景列表页码
    private List<SceneListBean2.RowsEntity> sceneList;//订阅的情景列表数据
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
        GridView gridView = pullRefreshView.getRefreshableView();
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

    //获取订阅的情景
    private void getSubsQJ() {
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(page + "", 8 + "", null, ids, null, null, null, null);
        Call listHandler = HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                HttpResponse<SceneListBean2> sceneL = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneListBean2>>() {});
                if (sceneL.isSuccess()) {
                    subsListAdapter.setPage(TypeConversionUtils.StringConvertInt(sceneL.getData().getCurrent_page()));
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
            public void onFailure(String error) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(listHandler);
    }

    //获取订阅情境主题个数
    private void hasSubsCount() {
        Call httpHandler = HttpRequest.post(URL.USER_CENTER, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<UserInfoBean> userInfo = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<UserInfoBean>>() { });
                if (userInfo.isSuccess()) {
                    hasSubsCountTv.setText("已订阅" + userInfo.getData().getInterest_scene_cate().size() + "个情境主题");
                    subsId = new ArrayList<>();
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
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                dialog.dismiss();
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
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

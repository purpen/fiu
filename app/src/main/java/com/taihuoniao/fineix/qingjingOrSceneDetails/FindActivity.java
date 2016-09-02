package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FindQJSceneListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class FindActivity extends BaseActivity implements PullToRefreshBase.OnLastItemVisibleListener {
    //上个界面传递过来的数据
    private int pos;//显示的位置
    private int page;//当前页码
    private List<SceneList.DataBean.RowsBean> sceneList;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private ListView listView;
    private FindQJSceneListAdapter findQJSceneListAdapter;

    public FindActivity() {
        super(R.layout.activity_find);
    }

    @Override
    protected void initView() {
        titleLayout.setTitle(R.string.find);
        titleLayout.setContinueTvVisible(false);
        pullRefreshView.setPullToRefreshEnabled(false);
        pullRefreshView.setOnLastItemVisibleListener(this);
        listView = pullRefreshView.getRefreshableView();
        listView.setDividerHeight(0);
        listView.setSelector(R.color.nothing);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void initList() {
        page = getIntent().getIntExtra("page", 1);
        if (getIntent().hasExtra(SubsQJActivity.class.getSimpleName())) {
            pos = getIntent().getIntExtra(SubsQJActivity.class.getSimpleName(), 0);
        } else if (getIntent().hasExtra(FindFragment.class.getSimpleName())) {
            pos = getIntent().getIntExtra(FindFragment.class.getSimpleName(), 0);
        }
        sceneList = MainApplication.sceneList;
        findQJSceneListAdapter = new FindQJSceneListAdapter(this, sceneList);
        listView.setAdapter(findQJSceneListAdapter);
        MainApplication.sceneList = null;
        listView.setSelection(pos);
    }

    @Override
    public void onLastItemVisible() {
        progressBar.setVisibility(View.VISIBLE);
        page++;
        lastList();
    }

    //加载下一页
    private void lastList() {
        if (getIntent().hasExtra(SubsQJActivity.class.getSimpleName())) {
            String ids = getIntent().getStringExtra("ids");
            getSceneList(page, 8 + "", null, ids, null, null, null, null, null);
        } else if (getIntent().hasExtra(FindFragment.class.getSimpleName())) {
            getSceneList(page, 10 + "", null, null, 0 + "", null, null, null, null);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    //获取情景列表
    private void getSceneList(final int page, String size, String scene_id, String category_ids, String sort, String fine, String dis, String lng, String lat) {
        ClientDiscoverAPI.getSceneList(page + "", size, scene_id, category_ids, sort, fine, dis, lng, lat, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                pullRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                Log.e("<<<情景列表", responseInfo.result);
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
                    sceneList.addAll(sceneL.getData().getRows());
                    if (findQJSceneListAdapter != null) {
                        findQJSceneListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data==null){
            return;
        }
        switch (requestCode){
            case RESULT_OK:
                if(findQJSceneListAdapter==null){
                    return;
                }
                int count = data.getIntExtra(CommentListActivity.class.getSimpleName(), -1);
                if (count == -1) {
                    return;
                }
                Log.e("<<<首页接收评论数量", "count=" + count);
                sceneList.get(findQJSceneListAdapter.getPos()).setComment_count(count);
                findQJSceneListAdapter.notifyDataSetChanged();
                break;
        }
    }
}

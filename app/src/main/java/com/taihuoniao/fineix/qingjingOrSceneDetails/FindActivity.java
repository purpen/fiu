package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FindQJSceneListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.TypeConversionUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/24.
 */
public class FindActivity extends BaseActivity implements PullToRefreshBase.OnLastItemVisibleListener {
    //上个界面传递过来的数据
    private int pos;//显示的位置
    private int page;//当前页码
    private List<SceneListBean2.RowsEntity> sceneList;
    @Bind(R.id.title_layout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
//    @Bind(R.id.progress_bar)
//    ProgressBar progressBar;
    private ListView listView;
    private FindQJSceneListAdapter findQJSceneListAdapter;
    private WaittingDialog dialog;//耗时操作对话框

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
        dialog = new WaittingDialog(this);
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
        if (!dialog.isShowing()) {
            dialog.show();
        }
//        progressBar.setVisibility(View.VISIBLE);
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
            dialog.dismiss();
//            progressBar.setVisibility(View.GONE);
        }
    }

    private Call listHandler;

    //获取情景列表
    private void getSceneList(final int page, String size, String scene_id, String category_ids, String sort, String fine, String dis, String lng, String lat) {
        HashMap<String, String> re = ClientDiscoverAPI.getSceneListRequestParams(page + "", size, scene_id, category_ids, sort, fine, lng, lat);
        listHandler = HttpRequest.post(re, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                pullRefreshView.onRefreshComplete();
                dialog.dismiss();
                HttpResponse<SceneListBean2> sceneL = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneListBean2>>() {});
                if (sceneL.isSuccess()) {
                    sceneList.addAll(sceneL.getData().getRows());
                    if (findQJSceneListAdapter != null) {
                        findQJSceneListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
                pullRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case RESULT_OK:
                if (findQJSceneListAdapter == null) {
                    return;
                }
                int count = data.getIntExtra(CommentListActivity.class.getSimpleName(), -1);
                if (count == -1) {
                    return;
                }
                sceneList.get(findQJSceneListAdapter.getPos()).setComment_count(TypeConversionUtils.IntConvertString(count));
                findQJSceneListAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (listHandler != null)
            listHandler.cancel();
        super.onDestroy();
    }
}

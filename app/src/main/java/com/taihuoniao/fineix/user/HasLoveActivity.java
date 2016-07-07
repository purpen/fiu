package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SupportQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoveSceneBean;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WriteJsonToSD;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/30.
 * 已经点赞的场景
 * 未完成，接口返回数据有问题
 */
public class HasLoveActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pullGv;
    //上个界面传递过来的数据
    private User user;//用户信息
    //    GlobalTitleLayout titleLayout;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.activity_has_love_progress)
    ProgressBar progressBar;
    private WaittingDialog dialog;

    //场景列表
    private int page = 1;
    private List<LoveSceneBean.LoveSceneItem> list;
    private SupportQJAdapter adapter;


    @Override
    protected void getIntentData() {
        user = (User) getIntent().getSerializableExtra("user");
        if (user == null || user._id == 0L) {
            ToastUtils.showError("用户信息为空");
            finish();
        }
    }

    public HasLoveActivity() {
        super(R.layout.activity_has_love);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.has_love);
        dialog = new WaittingDialog(HasLoveActivity.this);
    }

    @Override
    protected void initList() {
        list = new ArrayList<>();
        adapter = new SupportQJAdapter(list, activity);
        pullGv.setAdapter(adapter);
        pullGv.setOnItemClickListener(this);
//        pullToRefreshView.setPullToRefreshEnabled(false);
//        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                page++;
//                progressBar.setVisibility(View.VISIBLE);
//                requestLoveSceneList();
//            }
//        });

        pullGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                resetData();
                requestLoveSceneList();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });

        pullGv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                requestLoveSceneList();
            }
        });
    }

    private void resetData() {
        page = 1;
        list.clear();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        requestLoveSceneList();
    }

    private void requestLoveSceneList() {
        ClientDiscoverAPI.commonList(page + "", 8 + "", null, user._id + "", "sight", "love", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<赞过的", responseInfo.result);
                WriteJsonToSD.writeToSD("json", responseInfo.result);
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<LoveSceneBean>() {
                    }.getType();
                    LoveSceneBean loveSceneBean = gson.fromJson(responseInfo.result, type);
                    if (loveSceneBean.isSuccess()) {
                        if (page == 1) {
                            list.clear();
                        }
                        list.addAll(loveSceneBean.getData().getRows());
                        adapter.notifyDataSetChanged();

                        if (pullGv != null)
                            pullGv.onRefreshComplete();

                    } else {
                        ToastUtils.showError(loveSceneBean.getMessage());
//                        dialog.showErrorWithStatus(loveSceneBean.getMessage());
//                        Toast.makeText(HasLoveActivity.this, loveSceneBean.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("<<<", "请求失败" + error.toString());
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LoveSceneBean.LoveSceneItem loveSceneItem = (LoveSceneBean.LoveSceneItem) pullGv.getRefreshableView().getAdapter().getItem(position);
        Intent intent = new Intent(HasLoveActivity.this, SceneDetailActivity.class);
        intent.putExtra("id", loveSceneItem.get_id());
        startActivity(intent);
    }

}

package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.LoveSceneBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

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

//    GlobalTitleLayout titleLayout;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.activity_has_love_pulltorefreshview)
    PullToRefreshListView pullToRefreshView;
    @Bind(R.id.activity_has_love_progress)
    ProgressBar progressBar;
    private ListView listView;
    private SVProgressHUD dialog;
    //当前用户的user_id
    private long user_id;
    //场景列表
    private int page = 1;
    private List<LoveSceneBean.LoveSceneItem> list;
    private SceneListViewAdapter sceneListViewAdapter;


    @Override
    protected void getIntentData() {
        user_id = LoginInfo.getUserId();//用户id
//        Log.e("<<<", "logininfo.userid=" + user_id);
        if (user_id == -1 || user_id == 0) {
            new SVProgressHUD(this).showErrorWithStatus("用户信息为空");
//            Toast.makeText(HasLoveActivity.this, "用户信息为空", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public HasLoveActivity() {
        super(R.layout.activity_has_love);
    }

    @Override
    protected void initView() {
//        titleLayout = (GlobalTitleLayout) findViewById(R.id.activity_has_love_titlelayout);
        custom_head.setHeadCenterTxtShow(true,R.string.has_love);
//        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.activity_has_love_pulltorefreshview);
//        progressBar = (ProgressBar) findViewById(R.id.activity_has_love_progress);
        listView = pullToRefreshView.getRefreshableView();
        listView.setDividerHeight(DensityUtils.dp2px(HasLoveActivity.this,5));
        dialog = new SVProgressHUD(HasLoveActivity.this);
    }

    @Override
    protected void initList() {
//        titleLayout.setBackgroundResource(R.color.white);
//        titleLayout.setBackImg(R.mipmap.back_black);
//        titleLayout.setContinueTvVisible(false);
//        titleLayout.setTitle(R.string.has_love, getResources().getColor(R.color.black333333));
        list = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(HasLoveActivity.this, null, list,null,null);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                requestLoveSceneList();
            }
        });

    }

    @Override
    protected void requestNet() {
        dialog.show();
        requestLoveSceneList();
    }

    private void requestLoveSceneList() {
        ClientDiscoverAPI.commonList(page + "", 8 + "", null, user_id + "", "sight", "love", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<>", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
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
                        sceneListViewAdapter.notifyDataSetChanged();
                    } else {
                        dialog.showErrorWithStatus(loveSceneBean.getMessage());
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
    protected void onDestroy() {
        //cancelNet();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LoveSceneBean.LoveSceneItem loveSceneItem = (LoveSceneBean.LoveSceneItem) listView.getAdapter().getItem(position);
        Intent intent = new Intent(HasLoveActivity.this, SceneDetailActivity.class);
        intent.putExtra("id", loveSceneItem.get_id());
        startActivity(intent);
    }
}

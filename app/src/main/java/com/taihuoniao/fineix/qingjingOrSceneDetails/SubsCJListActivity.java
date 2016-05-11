package com.taihuoniao.fineix.qingjingOrSceneDetails;

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
import com.taihuoniao.fineix.beans.SubsCjListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/5/11.
 */
public class SubsCJListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.activity_subs_cjlist_pullrefreshview)
    PullToRefreshListView pullToRefreshView;
    @Bind(R.id.activity_subs_cjlist_progress)
    ProgressBar progressBar;
    private ListView listView;
    private WaittingDialog dialog;
    //网络请求返回数据
    //场景列表
    private int page = 1;
    private List<SubsCjListBean.SubsCJItem> list;
    private SceneListViewAdapter sceneListViewAdapter;

    public SubsCJListActivity() {
        super(R.layout.activity_subs_cjlist);
    }

    @Override
    protected void initView() {
        listView = pullToRefreshView.getRefreshableView();
        dialog = new WaittingDialog(SubsCJListActivity.this);
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                requestNet();
            }
        });
    }

    @Override
    protected void initList() {
        list = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(SubsCJListActivity.this, null, null, null, list);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
        dialog.show();
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.subsCJList(page + "", 8 + "", null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SubsCjListBean subsCjListBean = new SubsCjListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SubsCjListBean>() {
                    }.getType();
                    subsCjListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (subsCjListBean.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        pullToRefreshView.lastTotalItem = -1;
                    }
                    list.addAll(subsCjListBean.getData().getRows());
                    sceneListViewAdapter.notifyDataSetChanged();
                }
//                Log.e("<<<订阅", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<failure>>>", "请求失败" + error.toString());
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SubsCjListBean.SubsCJItem subsCJItem = (SubsCjListBean.SubsCJItem) listView.getAdapter().getItem(position);
        if (subsCJItem != null) {
            Intent intent = new Intent(SubsCJListActivity.this, SceneDetailActivity.class);
            intent.putExtra("id", subsCJItem.get_id());
            startActivity(intent);
        }
    }
}

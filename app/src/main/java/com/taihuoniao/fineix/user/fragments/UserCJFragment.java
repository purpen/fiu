package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.UserCJListAdapter;
import com.taihuoniao.fineix.beans.UserCJListData;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/5/6 13:22
 */
public class UserCJFragment extends MyBaseFragment {
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    private int curPage = 1;
    private boolean isLoadMore = false;
    public static final String PAGE_SIZE = "10";
    private WaittingDialog dialog;
    private UserCJListAdapter adapter;
    private ListView listView;
    private List<SceneListBean> mList = new ArrayList<>();
    private String userId=String.valueOf(LoginInfo.getUserId());
    public UserCJFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments!=null){
            userId=arguments.getString(UserCenterActivity.class.getSimpleName(),userId);
        }
        LogUtil.e("onCreate",userId);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_my_cj);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    protected void initParams() {
        if (dialog == null)
            dialog = new WaittingDialog(activity);
    }

    @Override
    protected void initViews() {
        pull_lv.setPullToRefreshEnabled(false);
        listView = pull_lv.getRefreshableView();
    }

    @Override
    protected void installListener() {
        pull_lv.setOnLastItemVisibleListener(new com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SceneListBean item=mList.get(i);
                Intent intent = new Intent(activity, SceneDetailActivity.class);
                intent.putExtra("id", item.get_id());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void loadData() {
        ClientDiscoverAPI.getSceneList(String.valueOf(curPage), PAGE_SIZE,userId,new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (dialog!=null){
                    if (curPage == 1) dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getSceneList", responseInfo.result);
                HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                if (response.isSuccess()) {
                    UserCJListData listBean = JsonUtil.fromJson(responseInfo.result,new TypeToken<HttpResponse<UserCJListData>>(){});
                    List list = listBean.rows;
                    refreshUIAfterNet(list);
                    return;
                }
                Util.makeToast(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet(List list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (isLoadMore) {
                Util.makeToast("没有更多数据哦！");
            } else {
                Util.makeToast("暂无数据！");
            }
            return;
        }

        curPage++;

        if (adapter == null) {
            mList.addAll(list);
            adapter = new UserCJListAdapter(list,activity);
            listView.setAdapter(adapter);
        } else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_lv != null)
            pull_lv.onRefreshComplete();
    }
}

package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderedQJAdapter;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/5/6 10:01
 */
public class UserQJFragment extends MyBaseFragment {
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pull_gv;
    private int curPage = 1;
    private boolean isLoadMore = false;
    public static final String PAGE_SIZE = "10";
    private SVProgressHUD dialog;
    private OrderedQJAdapter adapter;
    private List<QingJingListBean.QingJingItem> mList = new ArrayList<>();
    private String userId=String.valueOf(LoginInfo.getUserId());
    public UserQJFragment() {
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
        super.setFragmentLayout(R.layout.fragment_gv_qj);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    protected void initParams() {
        if (dialog == null)
            dialog = new SVProgressHUD(activity);
    }

    @Override
    protected void initViews() {
        if (pull_gv != null)
            pull_gv.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    protected void installListener() {
        pull_gv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                resetData();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            }

        });


        pull_gv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        pull_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(activity, QingjingDetailActivity.class);
                intent.putExtra("id", mList.get(i).get_id());
                startActivity(intent);
            }
        });
    }

    private void resetData() {
        curPage = 1;
        isLoadMore = false;
        mList.clear();
    }

    @Override
    protected void loadData() {
        LogUtil.e("getQJList=======>",userId);
        ClientDiscoverAPI.getQJList(String.valueOf(curPage), PAGE_SIZE,userId, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (curPage == 1) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                LogUtil.e("getQJList", responseInfo.result);
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()) {
                    List list = listBean.getData().getRows();
                    refreshUIAfterNet(list);
                    return;
                }
                Util.makeToast(listBean.getMessage());
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

            } else {
                Util.makeToast("暂无数据！");
            }
            return;
        }

        curPage++;

        if (adapter == null) {
            mList.addAll(list);
            adapter = new OrderedQJAdapter(mList, activity);
            pull_gv.setAdapter(adapter);
        } else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_gv != null)
            pull_gv.onRefreshComplete();
    }
}

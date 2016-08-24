package com.taihuoniao.fineix.user.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SupportQJAdapter;
import com.taihuoniao.fineix.beans.DataSupportQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemSubscribedQJ;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin  情境列表接口 subject_id
 *         created at 2016/8/10 17:24
 */
public class ParticipateQJFragment extends MyBaseFragment {
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pullGv;
    private WaittingDialog dialog;
    private ArrayList<DataSupportQJ.ItemSupportQJ> mList = new ArrayList<>();
    private int curPage = 1;
    private SupportQJAdapter adapter;
    private boolean isLoadMore = false;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.id = bundle.getString("id");
        }
        super.onCreate(savedInstanceState);
    }

    public static ParticipateQJFragment newInstance(String id) {
        ParticipateQJFragment fragment = new ParticipateQJFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_participate_qj);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        pullGv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mList = new ArrayList<>();
        dialog = new WaittingDialog(activity);
    }

    @Override
    protected void installListener() {
        pullGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                isLoadMore = true;
                curPage = 1;
                mList.clear();
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });

        pullGv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                loadData();
            }
        });

        pullGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null && mList.size() > 0) {
                    ItemSubscribedQJ item = (ItemSubscribedQJ) pullGv.getRefreshableView().getAdapter().getItem(position);
                    Intent intent = new Intent(activity, SceneDetailActivity.class);
                    intent.putExtra("id", item._id);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void loadData() {
        if (TextUtils.isEmpty(id)) return;
        LogUtil.e(TAG, id);
        ClientDiscoverAPI.participateActivity(String.valueOf(curPage), id, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<DataSupportQJ> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<DataSupportQJ>>() {
                });

                if (response.isSuccess()) {
                    pullGv.onRefreshComplete();
                    ArrayList list = response.getData().rows;
                    refreshUIAfterNet(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                e.printStackTrace();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUIAfterNet(ArrayList list) {
        if (list == null || list.size() == 0) return;
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new SupportQJAdapter(mList, activity);
            pullGv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

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
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ParticipateQJAdapter;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DataParticipateQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemSubscribedQJ;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QJDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

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
    private ArrayList<DataParticipateQJ.ItemParticipateQJ> mList = new ArrayList<>();
    private int curPage = 1;
    private ParticipateQJAdapter adapter;
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
        pullGv.setMode(PullToRefreshBase.Mode.DISABLED);
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
                    Intent intent = new Intent(activity, QJDetailActivity.class);
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
        HashMap<String, String> requestParams = ClientDiscoverAPI.getparticipateActivityRequestParams(String.valueOf(curPage), id);
        HttpRequest.post(requestParams, URL.SCENE_LIST, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<DataParticipateQJ> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DataParticipateQJ>>() {
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
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
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
            adapter = new ParticipateQJAdapter(mList, activity);
            pullGv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

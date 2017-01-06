package com.taihuoniao.fineix.product.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodsDetailsCommentListsAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.TryCommentsBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class CommentFragment extends SearchFragment {
    private String target_id;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    @Bind(R.id.empty_view)
    TextView emptyView;
    @Bind(R.id.progress_bar)
    ProgressBar progressBar;
    private int page = 1;
    private GoodsDetailsCommentListsAdapter commentListsAdapter;
    private List<TryCommentsBean> commentsList;

    public static CommentFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        target_id = getArguments().getString("id");

    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_buy_gooddetail_comment, null);
        ButterKnife.bind(this, view);
        ListView listView = pullRefreshView.getRefreshableView();
        pullRefreshView.setPullToRefreshEnabled(false);
        pullRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                getCommentList();
            }
        });
        commentsList = new ArrayList<>();
        commentListsAdapter = new GoodsDetailsCommentListsAdapter(getActivity(), commentsList, true);
        listView.setAdapter(commentListsAdapter);
        return view;
    }

    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(target_id)) {
            return;
        }
        getCommentList();
    }


    //获得评论列表
    private void getCommentList() {
        progressBar.setVisibility(View.VISIBLE);
        RequestParams params =ClientDiscoverAPI. getGoodsDetailsCommentsListRequestParams(target_id, page + "");
       Call httpHandler =  HttpRequest.post(params, URL.COMMENTS_LIST, new GlobalDataCallBack(){
//        HttpHandler<String> httpHandler = ClientDiscoverAPI.getGoodsDetailsCommentsList(target_id, page + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<商品评论", responseInfo.result);
//                WriteJsonToSD.writeToSD("json",responseInfo.result);
                List<TryCommentsBean> list = DataPaser.parserTryDetailsCommentsList(responseInfo.result);
                progressBar.setVisibility(View.GONE);
                if (page == 1) {
                    commentsList.clear();
                    pullRefreshView.lastSavedFirstVisibleItem = -1;
                    pullRefreshView.lastTotalItem = -1;
                }
                commentsList.addAll(list);
                commentListsAdapter.notifyDataSetChanged();
                if (commentsList.size() > 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    emptyView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError(R.string.net_fail);
            }
        });
        addNet(httpHandler);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

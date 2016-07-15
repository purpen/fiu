package com.taihuoniao.fineix.scene.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SearchBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lilin
 *         created at 2016/4/25 18:38
 */
public class ProductResultFragment extends BaseFragment {
    private String q;
    private String t;
    private boolean isContent = false;
    //控件
    private PullToRefreshListView pullToRefreshView;
    private ListView listView;
    private ProgressBar progressBar;
    private TextView emptyView;
    private WaittingDialog dialog;
    //产品列表
    private int page = 1;
    private List<SearchBean.SearchItem> list;
    private GoodListAdapter goodListAdapter;

    public static ProductResultFragment newInstance(String q, String t, boolean isContent) {

        Bundle args = new Bundle();
        args.putString("q", q);
        args.putString("t", t);
        args.putBoolean("isContent", isContent);
        ProductResultFragment fragment = new ProductResultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        q = getArguments().getString("q", null);
        t = getArguments().getString("t", null);
        isContent = getArguments().getBoolean("isContent");
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_product_result, null);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.fragment_product_result_pullrefreshview);
        listView = pullToRefreshView.getRefreshableView();
        listView.setDivider(null);
        listView.setDividerHeight(0);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_product_result_progressBar);
        emptyView = (TextView) view.findViewById(R.id.fragment_product_result_emptyview);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                page++;
                if (isContent) {
                    search(q, t, page + "", "content", null);
                } else {
                    search(q, t, page + "", "tag", null);
                }
            }
        });
        list = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(getActivity(), null, list);
        listView.setAdapter(goodListAdapter);
    }

    @Override
    protected void requestNet() {
        Log.e("<<<productresult", "q=" + q + ",t=" + t);
        if (TextUtils.isEmpty(q) || TextUtils.isEmpty(t)) {
            return;
        }
        dialog.show();
//        progressBar.setVisibility(View.VISIBLE);
        if (isContent) {
            search(q, t, page + "", "content", null);
        } else {
            search(q, t, page + "", "tag", null);
        }
    }

    public void refreshData(String q, String t) {
        this.q = q;
        this.t = t;
        page = 1;
        requestNet();
    }

    private void search(String q, String t, String p, String evt, String sort) {
        ClientDiscoverAPI.search(q, t,null, p, evt, sort, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SearchBean searchBean = new SearchBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<SearchBean>() {
                    }.getType();
                    searchBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                SearchBean netSearch = searchBean;
                if (netSearch.isSuccess()) {
                    if (page == 1) {
                        list.clear();
                    }
                    list.addAll(netSearch.getData().getRows());
                    if (list.size() <= 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                    }
                    goodListAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSearch.getMessage());
//                        dialog.showErrorWithStatus(netSearch.getMessage());
//                        Toast.makeText(getActivity(), netSearch.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.SEARCH_LIST:
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    SearchBean netSearch = (SearchBean) msg.obj;
//                    if (netSearch.isSuccess()) {
//                        if (page == 1) {
//                            list.clear();
//                        }
//                        list.addAll(netSearch.getData().getRows());
//                        if (list.size() <= 0) {
//                            emptyView.setVisibility(View.VISIBLE);
//                        } else {
//                            emptyView.setVisibility(View.GONE);
//                        }
//                        goodListAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showError(netSearch.getMessage());
////                        dialog.showErrorWithStatus(netSearch.getMessage());
////                        Toast.makeText(getActivity(), netSearch.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    ToastUtils.showError("网络错误");
////                    dialog.showErrorWithStatus("网络错误");
//                    break;
//            }
//        }
//    };


}

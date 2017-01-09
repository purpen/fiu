package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.main.App;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.personal.salesevice.adapter.SaleAfterListAdapter;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackListBean;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by taihuoniao on 2016/8/28.
 */
public class ReturnGoodsFragment extends BaseFragment implements View.OnClickListener {
    @Bind(R.id.pullToRefreshListView_return)
    PullToRefreshListView pullToRefreshView;
    @Bind(R.id.return_textView_empty)
    TextView emptyView;
    @Bind(R.id.return_progressBar)
    ProgressBar progressBar;

    private int curPage = 1;
    private static final String size = "8";
    private SaleAfterListAdapter mAdapter;
    private WaittingDialog mDialog;
    private List<ChargeBackListBean.RowsEntity> rows;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.activity_return_goods, null);
        ButterKnife.bind(this, view);
        mDialog = new WaittingDialog(getActivity());
        if (curPage == 1) {
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        }
        ListView listView_show = pullToRefreshView.getRefreshableView();
        listView_show.setCacheColorHint(0);

        //下拉监听
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                orderListParser();
            }
        });
        // 设置上拉加载下一页
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                curPage++;
                orderListParser();
            }
        });

        rows = new ArrayList<>();
        mAdapter = new SaleAfterListAdapter(rows, getActivity(), this);
        listView_show.setAdapter(mAdapter);

        listView_show.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ChargeBackResultActivity.class);
                intent.putExtra(KEY.CHARGEBACK_RESULT, rows.get(position));
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected void requestNet() {
        orderListParser();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void orderListParser() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
        RequestParams params =ClientDiscoverAPI. getRefundListRequestParams(String.valueOf(curPage), size);
        HttpRequest.post(params,URL.SHOPPING_REFUND_LIST, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getRefundList(String.valueOf(curPage), size, new RequestCallBack<String>() {
            @Override
            public void onSuccess(String json) {
                ChargeBackListBean refundListData = null;
                try {
                    HttpResponse<ChargeBackListBean> refundList = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<ChargeBackListBean>>() {
                    });
                    if (refundList.isSuccess()) {
                        refundListData = refundList.getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showError(App.getString(R.string.hint_load_error));
                }

                pullToRefreshView.onRefreshComplete();
                mDialog.dismiss();
                if (refundListData != null && refundListData.getRows() != null) {
                    if (curPage == 1) {
                        rows.clear();
                    }

                    rows.addAll(refundListData.getRows());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(App.getString(R.string.hint_load_without_data));
                }
            }

            @Override
            public void onFailure(String error) {
                progressBar.setVisibility(View.GONE);
                mDialog.dismiss();
                ToastUtils.showError(App.getString(R.string.hint_load_net_error));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relativeLayout_good_info:
                int productId = (int) v.getTag(R.id.relativeLayout_good_info);
                Intent intent = new Intent(getActivity(), BuyGoodsDetailsActivity.class);
                intent.putExtra("id", String.valueOf(productId));
                getActivity().startActivity(intent);
                break;
        }
    }
}

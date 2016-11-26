package com.taihuoniao.fineix.personal.salesevice;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.OrderEntity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.personal.salesevice.adapter.SaleAfterListAdapter;
import com.taihuoniao.fineix.personal.salesevice.bean.ChargeBackListBean;
import com.taihuoniao.fineix.product.BuyGoodsDetailsActivity;
import com.taihuoniao.fineix.user.OrderDetailsActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
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

    private SaleAfterListAdapter mAdapter;
    private List<OrderEntity> mList = new ArrayList<>();
    private String status = "8";
    private WaittingDialog mDialog;

    private int curPage = 1;
    private int size = 10;

    private ChargeBackListBean refundListData;
    private List<ChargeBackListBean.RowsBean> rows;


    @Override
    public void onResume() {
        super.onResume();
    }

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
                //页码从新开始
                curPage = 1;
                //开始刷新
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

        ClientDiscoverAPI.getRefundList(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }
                try {
                    HttpResponse<ChargeBackListBean> refundList = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<ChargeBackListBean>>() {
                    });
                    if (refundList.isSuccess()) {
                        refundListData = refundList.getData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtils.showError("加载错误");
                }

                pullToRefreshView.onRefreshComplete();
                mDialog.dismiss();
                if (refundListData != null) {
                    if (curPage == 1) {
                        rows.clear();
                    }
                    rows.addAll(refundListData.getRows());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                progressBar.setVisibility(View.GONE);
                mDialog.dismiss();
                ToastUtils.showError("网络错误");
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

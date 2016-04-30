package com.taihuoniao.fineix.scene.addProductFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AddProductGridAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.beans.ProductListBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/13.
 */
public class AddProductsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private int position;
    private CategoryBean categoryBean;
    //控件
    private PullToRefreshGridView pullToRefreshView;
    private TextView nothingTv;
    private ProgressBar progressBar;
    //当前页码
    private int currentPage = 1;
    //网络请求返回数据商品列表
    private List<ProductListBean> productList;
    private AddProductGridAdapter addProductGridAdapter;

    public static AddProductsFragment newInstance(int position, CategoryBean categoryBean) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("categoryBean", categoryBean);
        AddProductsFragment fragment = new AddProductsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments() != null ? getArguments().getInt("position") : 0;
        categoryBean = getArguments() != null ? (CategoryBean) getArguments().getSerializable("categoryBean") : null;
    }

    @Override
    protected void requestNet() {
        progressBar.setVisibility(View.VISIBLE);
        DataPaser.getProductList(categoryBean.getList().get(position).get_id(), currentPage + "", handler);
    }

    @Override
    protected void initList() {
        GridView grid = pullToRefreshView.getRefreshableView();
        grid.setVerticalScrollBarEnabled(false);
        grid.setNumColumns(2);
        int dp10 = DensityUtils.dp2px(getActivity(), 10);
        int dp12 = DensityUtils.dp2px(getActivity(), 12);
        grid.setPadding(dp10, dp12, dp10, dp12);
        grid.setHorizontalSpacing(dp10);
        grid.setVerticalSpacing(dp10);
        pullToRefreshView.setEmptyView(nothingTv);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                requestNet();
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                requestNet();
            }
        });
        productList = new ArrayList<>();
        addProductGridAdapter = new AddProductGridAdapter(getActivity(), productList);
        grid.setAdapter(addProductGridAdapter);
        grid.setOnItemClickListener(this);
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.view_addproduct_fragment, null);
        pullToRefreshView = (PullToRefreshGridView) view.findViewById(R.id.view_addproduct_fragment_refresh);
        nothingTv = (TextView) view.findViewById(R.id.view_addproduct_fragment_nothingtv);
        progressBar = (ProgressBar) view.findViewById(R.id.view_addproduct_fragment_progress);
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
                    pullToRefreshView.onRefreshComplete();
                    progressBar.setVisibility(View.GONE);
                    ProductBean netProductBean = (ProductBean) msg.obj;
                    if (netProductBean.isSuccess()) {
                        if (currentPage == 1) {
                            productList.clear();
                            pullToRefreshView.lastTotalItem = -1;
                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        }
                        productList.addAll(netProductBean.getList());
                        //刷新数据
                        addProductGridAdapter.notifyDataSetChanged();
                        pullToRefreshView.setLoadingTime();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    pullToRefreshView.onRefreshComplete();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("添加此商品？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.putExtra("product", productList.get(position));
                getActivity().setResult(DataConstants.RESULTCODE_EDIT_ADDPRODUCT, intent);
                getActivity().finish();
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

    }


}

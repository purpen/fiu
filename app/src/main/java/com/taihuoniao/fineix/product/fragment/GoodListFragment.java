package com.taihuoniao.fineix.product.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.GoodListAdapter;
import com.taihuoniao.fineix.adapters.GoodListFragmentRecyclerAdapter;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryLabelListBean;
import com.taihuoniao.fineix.beans.ProductBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class GoodListFragment extends Fragment implements EditRecyclerAdapter.ItemClick {
    private CategoryBean categoryBean;
    private int position;
    private String tag_id;//第二级商品分类的tag_id
    //界面下的控件
    private RecyclerView recyclerView;//二级分类
    private ProgressBar progressBar;
    private PullToRefreshListView pullToRefreshView;
    private ListView listView;
    //二级分类列表
    private List<CategoryLabelListBean.CategoryTagItem> recyclerList;
    private GoodListFragmentRecyclerAdapter goodListFragmentRecyclerAdapter;
    private int pos = -1;//被点击的二级分类
    //商品列表
    private int page = 1;//列表页码
    private List<ProductBean.ProductListItem> productList;
    private GoodListAdapter goodListAdapter;
    //网络请求工具类
//    private SVProgressHUD dialog;
    private WaittingDialog dialog;

    public static GoodListFragment newInstance(int position, CategoryBean categoryBean) {

        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putSerializable("categoryBean", categoryBean);
        GoodListFragment fragment = new GoodListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initView();
        initList();
        requestNet();
        return view;
    }


    protected View initView() {
        categoryBean = (CategoryBean) getArguments().getSerializable("categoryBean");
        position = getArguments().getInt("position", 0);
        Log.e("<<<产品分类",position+"");
        if (position >= 0)
            tag_id = categoryBean == null ? "0" : categoryBean.getList().get(position).getTag_id();
        View view = View.inflate(getActivity(), R.layout.fragment_good_list, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_good_list_recycler);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_good_list_progress);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.fragment_good_list_pullrefreshview);
        listView = pullToRefreshView.getRefreshableView();
        listView.setDivider(null);
        listView.setDividerHeight(0);
//        dialog = new SVProgressHUD(getActivity());
        dialog = new WaittingDialog(getActivity());
        return view;
    }


    protected void initList() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerList = new ArrayList<>();
        goodListFragmentRecyclerAdapter = new GoodListFragmentRecyclerAdapter(getActivity(), recyclerList, this);
        recyclerView.setAdapter(goodListFragmentRecyclerAdapter);
        pullToRefreshView.setPullToRefreshEnabled(false);
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                if (position == 0) {
                    DataPaser.getProductList(null, null, null, page + "", 8 + "", null, null, null, null, handler);
                    return;
                }
                if (tag_id.equals("0")) {
                    DataPaser.getProductList(categoryBean.getList().get(position).get_id(), null, null, page + "", 8 + "", null, null, null, null, handler);
                } else {
                    if (pos == -1) {
                        DataPaser.getProductList(categoryBean.getList().get(position).get_id(), null, null, page + "", 8 + "", null, null, null, null, handler);
                    } else {
                        DataPaser.getProductList(categoryBean.getList().get(position).get_id(), null, recyclerList.get(pos).get_id(), page + "", 8 + "", null, null, null, null, handler);
                    }
                }
            }
        });
        productList = new ArrayList<>();
        goodListAdapter = new GoodListAdapter(getActivity(), productList, null);
        listView.setAdapter(goodListAdapter);
    }

    protected void requestNet() {

//        progressBar.setVisibility(View.VISIBLE);
        dialog.show();
        Log.e("<<<", "tag_id = " + tag_id);
//        progressBar.setVisibility(View.VISIBLE);
        if (position == 0) {
            recyclerView.setVisibility(View.GONE);
            DataPaser.getProductList(null, null, null, page + "", 8 + "", null, null, null, null, handler);
            return;
        }
        if (tag_id == null) {
            dialog.dismiss();
            return;
        }
        DataPaser.getProductList(categoryBean.getList().get(position).get_id(), null, null, page + "", 8 + "", null, null, null, null, handler);
        if (tag_id.equals("0")) {
            recyclerView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            DataPaser.categoryLabel(tag_id, handler);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.ADD_PRODUCT_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
//                    listview适配器
                    ProductBean netProductBean = (ProductBean) msg.obj;
                    if (netProductBean.isSuccess()) {
                        productList.addAll(netProductBean.getData().getRows());
                        goodListAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.CATEGORY_LABEL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    CategoryLabelListBean netCategory = (CategoryLabelListBean) msg.obj;
                    if (netCategory.isSuccess()) {
                        recyclerList.clear();
                        recyclerList.addAll(netCategory.getData().getTags());
//                        click(0);
                        goodListFragmentRecyclerAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.showError(netCategory.getMessage());
//                        new SVProgressHUD(getActivity()).showErrorWithStatus(netCategory.getMessage());
//                        Toast.makeText(getActivity(), netCategory.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

    @Override
    public void click(int postion) {
//        progressBar.setVisibility(View.VISIBLE);
        dialog.show();
        for (int i = 0; i < recyclerList.size(); i++) {
            if (i == postion) {
                recyclerList.get(postion).setIsSelect(true);
            } else {
                recyclerList.get(i).setIsSelect(false);
            }
        }
        goodListFragmentRecyclerAdapter.notifyDataSetChanged();
        pos = postion;
        page = 1;
        productList.clear();
        goodListAdapter.notifyDataSetChanged();
//        dialog.show();

        Log.e("<<<", "id=" + recyclerList.get(postion).get_id());
        DataPaser.getProductList(categoryBean.getList().get(this.position).get_id(), null, recyclerList.get(postion).get_id(), page + "", 8 + "", null, null, null, null, handler);
    }

}

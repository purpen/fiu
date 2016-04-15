package com.taihuoniao.fineix.main.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinRecyclerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.beans.CategoryListBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

public class WellGoodsFragment extends BaseFragment implements EditRecyclerAdapter.ItemClick {
    private RecyclerView recyclerView;
    private WaittingDialog dialog;
    //品牌列表
    private List<CategoryListBean> list;
    private PinRecyclerAdapter pinRecyclerAdapter;

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_wellgoods, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_wellgoods_recycler);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void requestNet() {
        dialog.show();
//        DataPaser.hotLabelList();
        DataPaser.categoryList(1 + "", 1 + "", handler);
    }

    @Override
    protected void initList() {
        list = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        pinRecyclerAdapter = new PinRecyclerAdapter(getActivity(), list, this);
        recyclerView.setAdapter(pinRecyclerAdapter);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.CATEGORY_LIST:
                    dialog.dismiss();
                    CategoryBean netCategoryBean = (CategoryBean) msg.obj;
                    if (netCategoryBean.isSuccess()) {
                        list.addAll(netCategoryBean.getList());
                        pinRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
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
    public void click(int postion) {
        Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
    }
}

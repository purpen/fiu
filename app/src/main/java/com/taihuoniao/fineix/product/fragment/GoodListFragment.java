package com.taihuoniao.fineix.product.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.CategoryBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.view.CustomSlidingTab;
import com.taihuoniao.fineix.view.WaittingDialog;

/**
 * Created by taihuoniao on 2016/5/4.
 */
public class GoodListFragment extends BaseFragment {
    private CategoryBean categoryBean;
    private int position;
    private String tag_id;//第二级商品分类的tag_id
    //界面下的控件
    private CustomSlidingTab secondSliding;
    private ViewPager secondViewPager;
    //网络请求对话框
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryBean = (CategoryBean) getArguments().getSerializable("categoryBean");
        position = getArguments().getInt("position", 0);
        tag_id = categoryBean == null ? "0" : categoryBean.getList().get(position).getTag_id();
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_good_list, null);
        secondSliding = (CustomSlidingTab) view.findViewById(R.id.fragment_good_list_second_sliding);
        secondViewPager = (ViewPager) view.findViewById(R.id.fragment_good_list_viewpager);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
        secondSliding.setIndicatorColor(getResources().getColor(R.color.yellow_bd8913));
        secondSliding.setTextColor(getResources().getColor(R.color.black333333));
        secondSliding.setCurTabTextColor(getResources().getColor(R.color.yellow_bd8913));
        secondSliding.setTypeface(null, Typeface.NORMAL);
        secondSliding.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp14));
    }

    @Override
    protected void requestNet() {
        if (tag_id == null) {
            return;
        }
        dialog.show();
        if (tag_id.equals("0")) {
//            真麻烦。。。。。。。。。
        } else {
            DataPaser.categoryLabel(tag_id, handler);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.NET_FAIL:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }
}

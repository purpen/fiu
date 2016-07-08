package com.taihuoniao.fineix.scene.labelFragment;

import android.os.Bundle;
import android.view.View;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllLabelListViewAdapter;
import com.taihuoniao.fineix.adapters.AllLabelViewPagerAdapter1;
import com.taihuoniao.fineix.adapters.HotLabelViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.view.ListViewForScrollView;

/**
 * Created by taihuoniao on 2016/4/12.
 */
public class AllLabelFragment extends BaseFragment {
    private int position;
    private AllLabelBean allLabelBean;
    private AllLabelListViewAdapter.MoreClick moreClick;//点击更多
    private  HotLabelViewPagerAdapter.LabelClick labelClick;
    private static AllLabelViewPagerAdapter1 allLabelViewPagerAdapter;

    public static AllLabelFragment newInstance(int position, AllLabelBean allLabelBean,
                                               AllLabelListViewAdapter.MoreClick moreClick1,
                                               HotLabelViewPagerAdapter.LabelClick labelClick1,
                                               AllLabelViewPagerAdapter1 allLabelViewPagerAdapter1) {
//        labelClick = labelClick1;
        allLabelViewPagerAdapter = allLabelViewPagerAdapter1;
        Bundle args = new Bundle();
        AllLabelFragment fragment = new AllLabelFragment();
        args.putInt("position", position);
        args.putSerializable("labelClick",labelClick1);
        args.putSerializable("alllabel", allLabelBean);
        args.putSerializable("moreClick", moreClick1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        labelClick = (HotLabelViewPagerAdapter.LabelClick) getArguments().getSerializable("labelClick");
        position = getArguments() != null ? getArguments().getInt("position") : 0;
        allLabelBean = getArguments() != null ? (AllLabelBean) getArguments().getSerializable("alllabel") : null;
        moreClick = getArguments() != null ? (AllLabelListViewAdapter.MoreClick) getArguments().getSerializable("moreClick") : null;
    }

    @Override
    protected void requestNet() {

    }

    @Override
    protected void initList() {

    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.view_alllabel_listview, null);
//        ListView listView = (ListView) view.findViewById(R.id.view_alllabel_listview_listview);
        ListViewForScrollView listView = (ListViewForScrollView) view.findViewById(R.id.view_alllabel_listview_listview);
        AllLabelListViewAdapter adapter = new AllLabelListViewAdapter(getActivity(), allLabelBean.getChildren(), position, moreClick, labelClick, allLabelViewPagerAdapter);
        listView.setAdapter(adapter);
//        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtils.dp2px(getActivity(),555)));
        return view;
    }


}

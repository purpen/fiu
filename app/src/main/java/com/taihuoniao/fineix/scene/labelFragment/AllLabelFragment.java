package com.taihuoniao.fineix.scene.labelFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllLabelListViewAdapter;
import com.taihuoniao.fineix.adapters.AllLabelViewPagerAdapter1;
import com.taihuoniao.fineix.adapters.HotLabelViewPagerAdapter;
import com.taihuoniao.fineix.beans.AllLabelBean;
import com.taihuoniao.fineix.view.ListViewForScrollView;

/**
 * Created by taihuoniao on 2016/4/12.
 */
public class AllLabelFragment extends Fragment {
    private int position;
    private AllLabelBean allLabelBean;
    private AllLabelListViewAdapter adapter;
    private static AllLabelListViewAdapter.MoreClick moreClick;//点击更多
    private static HotLabelViewPagerAdapter.LabelClick labelClick;
    private static AllLabelViewPagerAdapter1 allLabelViewPagerAdapter;

    public static AllLabelFragment newInstance(int position, AllLabelBean allLabelBean,
                                               AllLabelListViewAdapter.MoreClick moreClick1,
                                               HotLabelViewPagerAdapter.LabelClick labelClick1,
                                               AllLabelViewPagerAdapter1 allLabelViewPagerAdapter1) {
        moreClick = moreClick1;
        labelClick = labelClick1;
        allLabelViewPagerAdapter = allLabelViewPagerAdapter1;
        Bundle args = new Bundle();
        AllLabelFragment fragment = new AllLabelFragment();
        args.putInt("position", position);
        args.putSerializable("alllabel", allLabelBean);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }


    protected View initView() {
        position = getArguments() != null ? getArguments().getInt("position") : 0;
        allLabelBean = getArguments() != null ? (AllLabelBean) getArguments().getSerializable("alllabel") : null;
        View view = View.inflate(getActivity(), R.layout.view_alllabel_listview, null);
//        ListView listView = (ListView) view.findViewById(R.id.view_alllabel_listview_listview);
        ListViewForScrollView listView = (ListViewForScrollView) view.findViewById(R.id.view_alllabel_listview_listview);
        adapter = new AllLabelListViewAdapter(getActivity(), allLabelBean.getChildren(), position, moreClick, labelClick, allLabelViewPagerAdapter);
        listView.setAdapter(adapter);
//        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtils.dp2px(getActivity(),555)));
        return view;
    }


}

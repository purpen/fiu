package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.ActivityAdapter;
import com.taihuoniao.fineix.beans.SubjectData;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class ActivityResultFragment extends MyBaseFragment {
    @Bind(R.id.pull_lv)
    PullToRefreshListView pullLv;
    private ArrayList<SubjectData.SightBean> mList;
    private ActivityAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mList = bundle.getParcelableArrayList("list");
        }
    }

    public static ActivityResultFragment newInstance(ArrayList<SubjectData.SightBean> list) {
        ActivityResultFragment fragment = new ActivityResultFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_article);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        mList = new ArrayList<>();
        pullLv.setMode(PullToRefreshBase.Mode.DISABLED);
    }

    @Override
    protected void installListener() {

    }

    @Override
    protected void refreshUIAfterNet() {
        if (mList == null || mList.size() == 0) return;
        adapter = new ActivityAdapter(mList, activity);
        pullLv.setAdapter(adapter);
    }
}

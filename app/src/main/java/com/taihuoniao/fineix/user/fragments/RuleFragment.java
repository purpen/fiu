package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class RuleFragment extends MyBaseFragment {
    private static final String TAG = "RuleFragment";
    @Bind(R.id.tv_rule)
    TextView tvRule;
    private String string;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            string = bundle.getString(TAG);
        }
    }

    public static RuleFragment newInstance(String string) {
        RuleFragment fragment = new RuleFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TAG, string);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.setFragmentLayout(R.layout.fragment_rule);
        super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initViews() {
        tvRule.setText(string);
    }
}

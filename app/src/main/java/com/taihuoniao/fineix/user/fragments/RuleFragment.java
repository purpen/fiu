package com.taihuoniao.fineix.user.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.main.fragment.MyBaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/8/10 17:24
 */
public class RuleFragment extends MyBaseFragment {
    @Bind(R.id.tv_rule)
    TextView tvRule;
    @Bind(R.id.btn)
    Button btn;
    private String string;
    private int evt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            string = bundle.getString("summary");
            String id = bundle.getString("id");
            evt = bundle.getInt("evt");
        }
    }

    public static RuleFragment newInstance(Bundle bundle) {
        RuleFragment fragment = new RuleFragment();
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
        if (evt == 2) {
            btn.setVisibility(View.GONE);
        }
        tvRule.setText(string);
    }

    @OnClick(R.id.btn)
    void onClick() {

    }
}

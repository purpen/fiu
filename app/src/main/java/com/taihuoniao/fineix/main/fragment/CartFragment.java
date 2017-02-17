package com.taihuoniao.fineix.main.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.main.App;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Stephen on 2017/2/14 16:29
 * Email: 895745843@qq.com
 */

public class CartFragment extends BaseFragment {

    @Bind(R.id.search_img)
    ImageView searchImg;
    @Bind(R.id.title_layout)
    RelativeLayout titleLayout;

    @Override
    protected void requestNet() {

    }

    @Override
    protected View initView() {
        return View.inflate(getActivity(), R.layout.fragment_cart, null);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            titleLayout.setPadding(0, App.getStatusBarHeight(), 0, 0);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

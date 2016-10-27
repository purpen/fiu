package com.taihuoniao.fineix.user.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.taihuoniao.fineix.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/6/22 10:51
 */
public class AddressAPIChangeFragment extends DialogFragment {
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn)
    public void onClick() {
        dismiss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        final View view = inflater.inflate(R.layout.fragment_address_api_change, container);
        ButterKnife.bind(this, view);
        return view;
    }
}

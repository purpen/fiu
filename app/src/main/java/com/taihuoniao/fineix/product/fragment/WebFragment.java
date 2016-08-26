package com.taihuoniao.fineix.product.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;

/**
 * Created by taihuoniao on 2016/8/25.
 */
public class WebFragment extends SearchFragment {
    private WebView webView;

    public static WebFragment newInstance() {

        Bundle args = new Bundle();

        WebFragment fragment = new WebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView() {
        webView = new WebView(getActivity());
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return webView;
    }

    @Override
    protected void requestNet() {

    }

    @Override
    public void refreshData(String q) {
        webView.loadUrl(q);
    }

}

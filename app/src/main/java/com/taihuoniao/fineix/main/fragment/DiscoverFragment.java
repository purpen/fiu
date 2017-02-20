package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.DiscoverContentAdapter;
import com.taihuoniao.fineix.adapters.DiscoverIndexAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DiscoverBean;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SearchActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class DiscoverFragment extends BaseFragment {
    private DiscoverIndexAdapter discoverIndexAdapter;
    private DiscoverBean discoverBean;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.relative)
    RelativeLayout relative;
    @Bind(R.id.lv_index)
    ListView lvIndex;
    @Bind(R.id.lv_content)
    ListView lvContent;
    private WaittingDialog dialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<DiscoverIndexBean> indexList;
    private OnFragmentInteractionListener mListener;

    public DiscoverFragment() {
        // Required empty public constructor
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected View initView() {
        dialog = new WaittingDialog(activity);
        View view = View.inflate(activity, R.layout.fragment_discover, null);
        return view;
    }

    @OnClick({R.id.title_left,R.id.title_right})
    void onClick(View v){
        switch (v.getId()){
            case R.id.title_left:
                startActivity(new Intent(getActivity(), CaptureActivity.class));
                break;
            case R.id.title_right:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", 9);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            relative.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        indexList = new ArrayList<>();
        String[] array = getResources().getStringArray(R.array.discover_index);
        for (int i = 0; i < array.length; i++) {
            DiscoverIndexBean indexBean = new DiscoverIndexBean();
            indexBean.indexName = array[i];
            if (i == 0) {
                indexBean.isSelected = true;
            } else {
                indexBean.isSelected = false;
            }
            indexList.add(indexBean);
        }
        discoverIndexAdapter = new DiscoverIndexAdapter(indexList, activity);
        lvIndex.setAdapter(discoverIndexAdapter);
    }


    @Override
    protected void installListener() {
        lvIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (indexList == null || indexList.size() == 0) return;
                selectedOne(i);
                lvContent.setSelection(i);
            }
        });

        lvContent.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                selectedOne(lvContent.getFirstVisiblePosition());
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }

    private void selectedOne(int index) {
        for (DiscoverIndexBean indexBean : indexList) {
            indexBean.isSelected = false;
        }
        indexList.get(index).isSelected = true;
        discoverIndexAdapter.notifyDataSetChanged();
    }

    @Override
    protected void requestNet() {
        HttpRequest.post(null, URL.DISCOVER_URL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                HttpResponse<DiscoverBean> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DiscoverBean>>() {
                });
                discoverBean = response.getData();
                refreshUI();
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
            }
        });
    }

    @Override
    protected void refreshUI() {
        DiscoverContentAdapter discoverContentAdapter = new DiscoverContentAdapter(activity, discoverBean,indexList);
        lvContent.setAdapter(discoverContentAdapter);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

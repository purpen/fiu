package com.taihuoniao.fineix.main.fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.DiscoverIndexAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DiscoverIndexBean;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class DiscoverFragment extends BaseFragment {
    private DiscoverIndexAdapter discoverIndexAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.lv_index)
    ListView lvIndex;
    @Bind(R.id.lv_content)
    ListView lvContent;
    private WaittingDialog dialog;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    @Override
    protected View initView() {
        dialog = new WaittingDialog(activity);
        View view = View.inflate(activity, R.layout.fragment_discover, null);
        return view;
    }

    @Override
    protected void initList() {
        List<DiscoverIndexBean> list=new ArrayList<>();
        String[] array = getResources().getStringArray(R.array.discover_index);
        for (int i=0;i<array.length;i++) {
            DiscoverIndexBean indexBean = new DiscoverIndexBean();
            indexBean.indexName = array[i];
            if (i==0){
                indexBean.isSelected=true;
            }else {
                indexBean.isSelected=false;
            }
        }
        discoverIndexAdapter = new DiscoverIndexAdapter(list,activity);
        lvIndex.setAdapter(discoverIndexAdapter);
    }

    @Override
    protected void installListener() {
        lvIndex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO
            }
        });
    }

    @Override
    protected void requestNet() {
        HttpRequest.post(null, URL.DISCOVER_URL, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                if (dialog!=null&&!activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                if (dialog!=null&&!activity.isFinishing()) dialog.dismiss();
                LogUtil.e(json);
            }

            @Override
            public void onFailure(String error) {
                if (dialog!=null&&!activity.isFinishing()) dialog.dismiss();
            }
        });
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

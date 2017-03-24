package com.taihuoniao.fineix.zone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.qingjingOrSceneDetails.bean.SceneListBean2;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.zone.adapter.ZoneRelateSceneAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;


/**
 * 地盘相关情境
 */
public class ZoneRelateSceneFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private static final String ZONE_ID = "ZONE_ID";
    private WaittingDialog dialog;
    private String sZoneId;
    private int curPage = 1;
    private List<SceneListBean2.RowsEntity> relateSceneList;
    private boolean isBottom;
    private boolean isLoading;
    private OnFragmentInteractionListener mListener;
    private String stick = "1";//默认是推荐
    private String sort = "1"; //默认是推荐
    private ZoneRelateSceneAdapter relateSceneAdapter;
    private LinearLayoutManager linearLayoutManager;
    public ZoneRelateSceneFragment() {
        // Required empty public constructor
    }

    public static ZoneRelateSceneFragment newInstance(String param) {
        ZoneRelateSceneFragment fragment = new ZoneRelateSceneFragment();
        Bundle args = new Bundle();
        args.putString(ZONE_ID, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sZoneId = getArguments().getString(ZONE_ID);
        }
    }


    @Override
    protected View initView() {
        dialog = new WaittingDialog(activity);
        View view = View.inflate(activity, R.layout.fragment_list, null);
        return view;
    }

    @Override
    protected void initList() {
        relateSceneList = new ArrayList();
        relateSceneAdapter = new ZoneRelateSceneAdapter(activity, relateSceneList);
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(relateSceneAdapter);
    }


    @Override
    protected void installListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading&&isBottom && lastVisibleItemPosition == relateSceneList.size()-1){
                    requestNet();
                }

            }
        });

        relateSceneAdapter.setSortListener(new ZoneRelateSceneAdapter.ISortListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tui_jian:
                        stick ="1";
                        sort = "1";
                        curPage =1;
                        relateSceneList.clear();
                        requestNet();
                        break;
                    case R.id.zui_xin:
                        sort = "3";
                        stick = "";
                        curPage = 1;
                        relateSceneList.clear();
                        requestNet();
                        break;
                    case R.id.btn_upload:
                        MainApplication.zoneId = sZoneId;
                        activity.startActivity(new Intent(activity, SelectPhotoOrCameraActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

    }


    @Override
    protected void requestNet() {
        if (TextUtils.isEmpty(sZoneId)) return;
        HashMap param = ClientDiscoverAPI.getRelateScene(curPage, sZoneId, sort, stick);
        HttpRequest.post(param, URL.SCENE_LIST, new GlobalDataCallBack() {
            @Override
            public void onStart() {
                isLoading = true;
//                if (dialog != null && !activity.isFinishing()) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                isLoading =false;
                if (dialog != null && !activity.isFinishing()) dialog.dismiss();
                HttpResponse<SceneListBean2> sceneL = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<SceneListBean2>>() {});
                if (sceneL.getData().getRows().size() > 0) {
                    curPage++;
                    relateSceneList.addAll(sceneL.getData().getRows());
                } else {
                    isBottom = true;
                }
                refreshUI();
            }

            @Override
            public void onFailure(String error) {
                isLoading = false;
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (relateSceneAdapter == null) {
            recyclerView.setAdapter(relateSceneAdapter);
        } else {
            relateSceneAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

package com.taihuoniao.fineix.user.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SimpleTextAdapter;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.AddressData;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/10/25 20:16
 */
public class AddressSelectFragment extends DialogFragment {
    @Bind(R.id.tv_province)
    TextView tvProvince;
    @Bind(R.id.tv_city)
    TextView tvCity;
    @Bind(R.id.tv_county)
    TextView tvCounty;
    @Bind(R.id.tv_town)
    TextView tvTown;
    @Bind(R.id.recycler_view_province)
    RecyclerView recyclerViewProvince;
    @Bind(R.id.recycler_view_city)
    RecyclerView recyclerViewCity;
    @Bind(R.id.recycler_view_county)
    RecyclerView recyclerViewCounty;
    @Bind(R.id.recycler_view_town)
    RecyclerView recyclerViewTown;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    private List<AddressData.RowsEntity> provinceList;
    private List<AddressData.RowsEntity> cityList;
    private List<AddressData.RowsEntity> countyList;
    private List<AddressData.RowsEntity> townList;
    private static final int PROVINCE_TYPE = 1;
    private static final int CITY_TYPE = 2;
    private static final int COUNTY_TYPE = 3;
    private static final int TOWN_TYPE = 4;
    private static final int OTHERS_TYPE = 5;
    private SimpleTextAdapter adapterProvince;
    private SimpleTextAdapter adapterCity;
    private SimpleTextAdapter adapterCounty;
    private SimpleTextAdapter adapterTown;
    private AddressData.RowsEntity curProvince;
    private AddressData.RowsEntity curCity;
    private AddressData.RowsEntity curCounty;
    private AddressData.RowsEntity curTown;
    private int oid;
    private int pid;
    private int layer;
    private Dialog dialog;
    private OnAddressChoosedListener listener;
    public interface OnAddressChoosedListener {
        void onAddressChoosed(AddressData.RowsEntity province, AddressData.RowsEntity city, AddressData.RowsEntity county, AddressData.RowsEntity town);
    }

    public void setOnAddressChoosedListener(OnAddressChoosedListener listener){
        this.listener=listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog==null){
            dialog = new Dialog(getActivity(), R.style.BottomDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialog.getWindow();
            dialog.setContentView(R.layout.view_bottom_list);
            dialog.setCanceledOnTouchOutside(true);
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.BOTTOM;
                wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
                wlp.height= getResources().getDimensionPixelSize(R.dimen.dp300);
                window.setAttributes(wlp);
            }
            ButterKnife.bind(this, dialog);
            initData();
            installListener();
        }
        return dialog;
    }

    private void initData() {
        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        countyList = new ArrayList<>();
        townList = new ArrayList<>();
        //省
        adapterProvince = new SimpleTextAdapter(getActivity(), provinceList);
        recyclerViewProvince.setAdapter(adapterProvince);
        recyclerViewProvince.setLayoutManager(new LinearLayoutManager(getActivity()));
        //市
        adapterCity = new SimpleTextAdapter(getActivity(), cityList);
        recyclerViewCity.setAdapter(adapterCity);
        recyclerViewCity.setLayoutManager(new LinearLayoutManager(getActivity()));

        //区、县
        adapterCounty = new SimpleTextAdapter(getActivity(), countyList);
        recyclerViewCounty.setAdapter(adapterCounty);
        recyclerViewCounty.setLayoutManager(new LinearLayoutManager(getActivity()));

        //乡,镇
        adapterTown = new SimpleTextAdapter(getActivity(), townList);
        recyclerViewTown.setAdapter(adapterTown);
        recyclerViewTown.setLayoutManager(new LinearLayoutManager(getActivity()));
        pid = 0;
        oid = 0;
        layer = PROVINCE_TYPE;
        requestNet();
    }

    private void installListener() {
        if (adapterProvince != null) {
            adapterProvince.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //获取省的下一级市
                    if (provinceList == null || provinceList.size() == 0) return;
                    curProvince = provinceList.get(position);

                    if (!TextUtils.equals(tvProvince.getText(), curProvince.name)) {
                        tvCity.setText(R.string.please_select);
                        curCity=null;
                        tvCounty.setVisibility(View.GONE);
                        tvCounty.setText(R.string.please_select);
                        curCounty=null;
                        tvTown.setVisibility(View.GONE);
                        tvTown.setText(R.string.please_select);
                        curTown=null;
                    }
                    tvProvince.setText(curProvince.name);
                    pid = curProvince.oid;
                    layer = CITY_TYPE;
                    requestNet();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }

        if (adapterCity != null) { //市被点击
            adapterCity.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //获取市下一级县/区
                    if (cityList == null || cityList.size() == 0) return;
                    curCity = cityList.get(position);
                    if (!TextUtils.equals(tvCity.getText(), curCity.name)) {
                        tvCounty.setText(R.string.please_select);
                        curCounty=null;
                        tvTown.setVisibility(View.GONE);
                        tvTown.setText(R.string.please_select);
                        curTown=null;
                    }
                    tvCity.setText(curCity.name);
                    pid = curCity.oid;
                    layer = COUNTY_TYPE;
                    requestNet();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }

        if (adapterCounty != null) { //获取县/区一级乡镇
            adapterCounty.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (countyList == null || countyList.size() == 0) return;
                    curCounty = countyList.get(position);
                    if (!TextUtils.equals(tvCounty.getText(), curCounty.name)) {
                        tvTown.setText(R.string.please_select);
                        curTown=null;
                    }
                    tvCounty.setText(curCounty.name);
                    pid = curCounty.oid;
                    layer = TOWN_TYPE;
                    requestNet();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }

        if (adapterTown != null) { //获取乡镇的下一级
            adapterTown.setOnItemClickListener(new SimpleTextAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (townList == null || townList.size() == 0) return;
                    curTown = townList.get(position);
                    tvTown.setText(curTown.name);
                    pid = curTown.oid;
                    layer = OTHERS_TYPE;
                    requestNet();
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
        }
    }

    private void requestNet() {
        HashMap<String, String> params =ClientDiscoverAPI. getrequestAddressRequestParams(String.valueOf(oid), String.valueOf(pid), String.valueOf(layer));
        HttpRequest.post(params,URL.SHOPPING_FETCH_CHINA_CITY, new GlobalDataCallBack(){
//        ClientDiscoverAPI.requestAddress(String.valueOf(oid), String.valueOf(pid), String.valueOf(layer), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                progressbar.setVisibility(View.VISIBLE);
                setItemClickable(false);
            }

            @Override
            public void onSuccess(String json) {
                setItemClickable(true);
                progressbar.setVisibility(View.GONE);
                HttpResponse<AddressData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<AddressData>>() {
                });
                if (response.isSuccess()) {
                    AddressData data = response.getData();
                    if (data.rows.size() == 0) {
//                        StringBuilder builder = new StringBuilder();
//                        builder.append(curProvince.name);
//                        String please_select = getResources().getString(R.string.please_select);
//                        if (!TextUtils.equals(please_select, tvCity.getText()))
//                            builder.append(curCity.name);
//                        if (!TextUtils.equals(please_select, tvCounty.getText()))
//                            builder.append(curCounty.name);
//                        if (!TextUtils.equals(please_select, tvTown.getText()))
//                            builder.append(curTown.name);

                        if (listener!=null){
                            listener.onAddressChoosed(curProvince,curCity,curCounty,curTown);
                        }
                        dismiss();
                        return;
                    }
                    switch (layer) {
                        case PROVINCE_TYPE:
                            provinceList.clear();
                            provinceList.addAll(data.rows);
                            break;
                        case CITY_TYPE:
                            cityList.clear();
                            cityList.addAll(data.rows);
                            tvCity.setVisibility(View.VISIBLE);
                            break;
                        case COUNTY_TYPE:
                            countyList.clear();
                            countyList.addAll(data.rows);
                            tvCounty.setVisibility(View.VISIBLE);
                            break;
                        case TOWN_TYPE:
                            townList.clear();
                            townList.addAll(data.rows);
                            tvTown.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                    refreshUI();
                } else {
                    ToastUtils.showError(response.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                setItemClickable(true);
                progressbar.setVisibility(View.GONE);
            }
        });
    }

    private void refreshUI() {
        resetListUI();
        switch (layer) {
            case PROVINCE_TYPE:
                recyclerViewProvince.setVisibility(View.VISIBLE);
                if (adapterProvince == null) {
                    adapterProvince = new SimpleTextAdapter(getActivity(), provinceList);
                    recyclerViewProvince.setAdapter(adapterProvince);
                } else {
                    adapterProvince.notifyDataSetChanged();
                }
                break;
            case CITY_TYPE:
                recyclerViewCity.setVisibility(View.VISIBLE);
                if (adapterCity == null) {
                    adapterCity = new SimpleTextAdapter(getActivity(), cityList);
                    recyclerViewCity.setAdapter(adapterCity);
                } else {
                    adapterCity.notifyDataSetChanged();
                }
                break;
            case COUNTY_TYPE:
                recyclerViewCounty.setVisibility(View.VISIBLE);
                if (adapterCounty == null) {
                    adapterCounty = new SimpleTextAdapter(getActivity(), countyList);
                    recyclerViewCounty.setAdapter(adapterCounty);
                } else {
                    adapterCounty.notifyDataSetChanged();
                }
                break;
            case TOWN_TYPE:
                recyclerViewTown.setVisibility(View.VISIBLE);
                if (adapterTown == null) {
                    adapterTown = new SimpleTextAdapter(getActivity(), townList);
                    recyclerViewTown.setAdapter(adapterTown);
                } else {
                    adapterTown.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.tv_province, R.id.tv_city, R.id.tv_county, R.id.tv_town})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_province:
                resetListUI();
                recyclerViewProvince.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_city:
                resetListUI();
                recyclerViewCity.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_county:
                resetListUI();
                recyclerViewCounty.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_town:
                resetListUI();
                recyclerViewTown.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetListUI() {
        recyclerViewProvince.setVisibility(View.GONE);
        recyclerViewCity.setVisibility(View.GONE);
        recyclerViewCounty.setVisibility(View.GONE);
        recyclerViewTown.setVisibility(View.GONE);
    }

    private void setItemClickable(boolean clickable) {
        adapterProvince.setClickable(clickable);
        adapterCity.setClickable(clickable);
        adapterCounty.setClickable(clickable);
        adapterTown.setClickable(clickable);
    }
}

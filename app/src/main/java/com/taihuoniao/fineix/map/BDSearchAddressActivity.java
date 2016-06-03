package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.BDAddressListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/12 18:15
 */
public class BDSearchAddressActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.et)
    EditText et;
    @Bind(R.id.ll)
    ListView ll;
    @Bind(R.id.ibtn)
    ImageButton ibtn;
    private ArrayList<PoiInfo> list;
    private BDAddressListAdapter adapter;
    private boolean isFirstLoc = true;
    private int radius = 1000;    //默认半径
    private int pageCapacity = 10; //默认分页大小
    private int pageNum=0;
    private PoiSortType sortType = PoiSortType.distance_from_near_to_far; //默认排序类型
    private LatLng latLng;
    private WaittingDialog dialog;
    //当前位置的市和区
    private String city, district;

    public BDSearchAddressActivity() {
        super(R.layout.activity_bdsearch_address_layout);
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
        //TODO 获取上个界面LatLng
//        latLng=new LatLng(39.990605, 116.505045);
        latLng = getIntent().getParcelableExtra("latLng");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (latLng == null) {
            MapUtil.getCurrentLocation(activity, new MapUtil.OnReceiveLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (bdLocation == null) {
                        return;
                    }
                    if (isFirstLoc) {
                        isFirstLoc = false;
                        latLng = new LatLng(bdLocation.getLatitude(),
                                bdLocation.getLongitude());
                        loadAndshowGeoCoderResult(latLng);
                    }
                }
            });
        }else {
            loadAndshowGeoCoderResult(latLng);
        }
    }

    private void loadAndshowGeoCoderResult(LatLng latLng) {
        if (latLng == null)
            return;
        if (!activity.isFinishing() &&dialog!=null) dialog.show();
        MapUtil.getAddressByCoordinate(latLng, new MapUtil.MyOnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (!activity.isFinishing() &&dialog!=null) dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                city = result.getAddressDetail().city;
                district = result.getAddressDetail().district;
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                list.addAll(result.getPoiList());
                if (list.size() > 0) {
                    if (adapter == null) {
                        adapter = new BDAddressListAdapter(activity, list);
                        ll.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showError("抱歉,没有检索到结果!");
//                    dialog.showErrorWithStatus("抱歉,没有检索到结果！");
                }
            }
            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {}
        });
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "位置");
        dialog=new WaittingDialog(this);
        custom_head.setHeadGoBackShow(false);
        custom_head.setIvLeft(R.mipmap.current_location);
        custom_head.setHeadRightTxtShow(true, R.string.cancel);
    }

    @Override
    protected void installListener() {
        et.addTextChangedListener(tw);
        ll.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == SCROLL_STATE_IDLE || i == SCROLL_STATE_FLING) {
                    if (absListView.getLastVisiblePosition() == list.size()) {
                        LogUtil.e("pageNum==", pageNum + "");
                        loadAndshowPoiResult(et.getText().toString().trim(),latLng);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (city == null || district == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(PoiInfo.class.getSimpleName(), list.get(i));
                intent.putExtra("city", city);
                intent.putExtra("district", district);
//                setResult(RESULT_OK, intent);
                setResult(DataConstants.RESULTCODE_CREATESCENE_BDSEARCH, intent);
                finish();
            }
        });
        ibtn.setOnClickListener(this);
        custom_head.getIvLeft().setOnClickListener(this);
        custom_head.getHeadRightTV().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibtn:
                et.getText().clear();
                ibtn.setVisibility(View.GONE);
                break;
            case R.id.tv_head_right:
                finish();
            case R.id.iv_left:
                loadAndshowGeoCoderResult(latLng);
                break;
        }
    }

    private TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before, int count) {
            String keyWord = cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                ibtn.setVisibility(View.VISIBLE);
                pageNum=0;
                loadAndshowPoiResult(keyWord,latLng);
            } else {
//                if (searchPoiList != null) {
//                    searchPoiList.clear();
//                }
//                hideSoftinput();
                ibtn.setVisibility(View.GONE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadAndshowPoiResult(String keyWord, LatLng latLng) {
        if (!activity.isFinishing() &&dialog!=null) dialog.show();
        MapUtil.getPoiNearbyByKeyWord(keyWord,latLng,radius,pageNum,pageCapacity,sortType, new MapUtil.MyOnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (!activity.isFinishing() &&dialog!=null) dialog.dismiss();
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                pageNum++;
                if (list == null) {
                    list = new ArrayList<>();
                } else {
                    list.clear();
                }
                list.addAll(result.getAllPoi());
                if (list.size() > 0) {
                    if (adapter == null) {
                        adapter = new BDAddressListAdapter(activity, list);
                        ll.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Util.makeToast(activity, "抱歉,没有检索到结果！");
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult result) {

            }
        });
    }



    @Override
    protected void refreshUI() {
        super.refreshUI();
    }

    @Override
    protected void onDestroy() {
        MapUtil.destroyGeoCoder();
        MapUtil.destroyPoiSearch();
        super.onDestroy();
    }
}

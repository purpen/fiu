package com.taihuoniao.fineix.map;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

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
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/4/12 18:15
 */
public class BDSearchAddressActivity extends BaseActivity implements View.OnClickListener{
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
    private int radius=1000;    //默认半径
    private int pageCapacity=10; //默认分页大小
    private PoiSortType sortType=PoiSortType.distance_from_near_to_far; //默认排序类型
    private LatLng latLng;
    public BDSearchAddressActivity(){
        super(R.layout.activity_bdsearch_address_layout);
    }

    @Override
    protected void getIntentData() {
        super.getIntentData();
        //TODO 获取上个界面LatLng
        latLng=new LatLng(39.990605, 116.505045);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (latLng==null){
            //TODO 获取当前位置
            latLng=new LatLng(39.990605, 116.505045);
        }
        loadAndshowGeoCoderResult(latLng);
    }

    private void loadAndshowGeoCoderResult(LatLng latLng) {
        if (latLng==null)
            return;
        MapUtil.getAddressByCoordinate(latLng,new MapUtil.MyOnGetGeoCoderResultListener() {
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                if (list==null){
                    list=new ArrayList<PoiInfo>();
                }else {
                    list.clear();
                }
                list.addAll(result.getPoiList());
                if (list.size()>0){
                    if (adapter==null){
                        adapter=new BDAddressListAdapter(activity,list);
                        ll.setAdapter(adapter);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Util.makeToast(activity,"抱歉,没有检索到结果！");
                }
            }

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {

            }
        });
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"位置");
        custom_head.setHeadGoBackShow(false);
        custom_head.setIvLeft(R.mipmap.current_location);
        custom_head.setHeadRightTxtShow(true,R.string.cancel);
    }

    @Override
    protected void installListener() {
        et.addTextChangedListener(tw);
        ll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra(PoiInfo.class.getSimpleName(),list.get(i));
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        ibtn.setOnClickListener(this);
        custom_head.getIvLeft().setOnClickListener(this);
        custom_head.getHeadRightTV().setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibtn:
                et.getText().clear();
                break;
            case R.id.tv_head_right:
                finish();
            case R.id.iv_left:
                loadAndshowGeoCoderResult(latLng);
                break;
        }
    }

    private TextWatcher tw=new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence cs, int start, int before,int count) {
            String keyWord=cs.toString().trim();
            if (!TextUtils.isEmpty(keyWord)) {
                loadAndshowPoiResult(keyWord,latLng);
            } else {
//                if (searchPoiList != null) {
//                    searchPoiList.clear();
//                }
//                hideSoftinput();
            }
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private void loadAndshowPoiResult(String keyWord,LatLng latLng) {
        MapUtil.getPoiNearbyByKeyWord(keyWord,latLng,radius,pageCapacity,sortType,new MapUtil.MyOnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    return;
                }
                if (list==null){
                    list=new ArrayList<PoiInfo>();
                }else {
                    list.clear();
                }
                list.addAll(result.getAllPoi());
                if (list.size()>0){
                    if (adapter==null){
                        adapter=new BDAddressListAdapter(activity,list);
                        ll.setAdapter(adapter);
                    }else {
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Util.makeToast(activity,"抱歉,没有检索到结果！");
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

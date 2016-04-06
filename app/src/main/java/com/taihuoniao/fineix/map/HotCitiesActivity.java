package com.taihuoniao.fineix.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.HotCitiesAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.Cities;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.service.LocationService;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/3/30 15:12
 */
public class HotCitiesActivity extends BaseActivity<Cities> {
    private LocationService locationService;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.tv_location)
    TextView tv_location;
    @Bind(R.id.gv_hotcity)
    GridView gv_hotcity;
    private HotCitiesAdapter adapter = null;
    public HotCitiesActivity() {
        super(R.layout.activity_hotcities_layout);
    }
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.select_city);
    }

    public void setCurrentCity(BDLocation location) {
        tv_location.setText(location.getCity());
    }

    @Override
    protected void onStop() {
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void installListener() {
        super.installListener();
        gv_hotcity.setOnItemClickListener(itemClickListener);
    }
    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent=new Intent(activity,BaiDuLBSActivity.class);
            if (view instanceof TextView){
                intent.putExtra(TAG,((TextView) view).getText());
            }
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLocate();
    }

    private void startLocate() {
        tv_location.setText(R.string.locating);
        locationService = ((MainApplication) getApplication()).locationService;
        int type = getIntent().getIntExtra("from", 0);

        if (type == 0) {
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        } else if (type == 1) {
            locationService.setLocationOption(locationService.getOption());
        }
        locationService.registerListener(mListener);
        locationService.start();
    }
    @Override
    protected void requestNet() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, NetworkConstance.HOT_CITIES, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                //TODO 弹出加载框
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                //TODO 关闭加载框
                if (responseInfo!=null && responseInfo.result!=null){
                    ArrayList<Cities> cities = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ArrayList<Cities>>>() {});
                    refreshUI(cities);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                //TODO 关闭加载框
                LogUtil.e(TAG,s);
            }
        });

    }


    @Override
    protected void refreshUI(ArrayList<Cities> list) {
        if (adapter==null){
            adapter=new HotCitiesAdapter(list, activity);
            gv_hotcity.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                setCurrentCity(location);
            }else {
                tv_location.setText(R.string.location_error);
            }
        }

    };

}

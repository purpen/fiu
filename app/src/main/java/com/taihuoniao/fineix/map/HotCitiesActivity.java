package com.taihuoniao.fineix.map;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.HotCitiesAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.City;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.network.NetworkConstance;
import com.taihuoniao.fineix.service.LocationService;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/3/30 15:12
 */
public class HotCitiesActivity extends BaseActivity<City> {
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    private LocationService locationService;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.tv_location)
    TextView tv_location;
    private ArrayList<City> cities;
    private HotCitiesAdapter adapter = null;
    private SVProgressHUD dialog;
    public HotCitiesActivity() {
        super(R.layout.activity_hotcities_layout);
    }
    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.select_city);
        custom_head.setHeadRightTxtShow(true, "全部城市");
        custom_head.getHeadRightTV().setCompoundDrawablesWithIntrinsicBounds(R.mipmap.all_city, 0, 0, 0);
        custom_head.getHeadRightTV().setTextColor(Color.parseColor("#af8323"));
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        recycler_view.setHasFixedSize(true);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        dialog=new SVProgressHUD(this);
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
    }

//    private AdapterView.OnItemClickListener itemClickListener=new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            Intent intent=new Intent(activity,BaiDuLBSActivity.class);
//            intent.putExtra(TAG,cities.get(i));
//            startActivity(intent);
//        }
//    };
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
                if (!activity.isFinishing()&&dialog!=null) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog!=null) dialog.dismiss();
                if (responseInfo!=null && responseInfo.result!=null){
                   cities = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<ArrayList<City>>>() {});
                    refreshUI();
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog!=null) dialog.dismiss();
                Util.makeToast("网络异常,请确保网络畅通");
            }
        });

    }


    @Override
    protected void refreshUI() {
        if (adapter==null){
            adapter=new HotCitiesAdapter(activity,cities);
            adapter.setmOnItemClickLitener(new HotCitiesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    LogUtil.e("onItemClick",position+"");
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            recycler_view.setAdapter(adapter);
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

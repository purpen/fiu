package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class AllQingjingActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageView searchQJImg;
    private ImageView createQingjingImg;
    private PullToRefreshGridView pullToRefreshView;
    private GridView qingjingGrid;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private RelativeLayout firstRelative;//第一次进入app会用到
    //适配器
    private AllQingjingGridAdapter allQingjingGridAdapter;
    private ProgressBar progressBar;
    //情景列表
    private double[] location = null;
    private int page = 1;//页码
    private double distance = 5000;//列表范围
    //网络请求对话框
    private WaittingDialog dialog;

    public AllQingjingActivity() {
        super(0);
    }

    @Override
    protected void initView() {
//        View activity_view = View.inflate(AllQingjingActivity.this, R.layout.activity_all_qingjing, null);
        setContentView(R.layout.activity_all_qingjing);
        searchQJImg = (ImageView) findViewById(R.id.activity_all_qingjing_search);
        createQingjingImg = (ImageView) findViewById(R.id.activity_all_qingjing_createqinjing);
        pullToRefreshView = (PullToRefreshGridView) findViewById(R.id.activity_all_qingjing_pullrefreshview);
        qingjingGrid = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) findViewById(R.id.activity_all_qingjing_progress);
        firstRelative = (RelativeLayout) findViewById(R.id.activity_all_qingjing_first);
        dialog = new WaittingDialog(AllQingjingActivity.this);
    }

    @Override
    protected void initList() {
//        isSelect = getIntent().getIntExtra("isSelect", 0);
        searchQJImg.setOnClickListener(this);
        createQingjingImg.setOnClickListener(this);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
//                progressBar.setVisibility(View.VISIBLE);
                dialog.show();
                page = 1;
                getQJList(page + "", 1 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                page++;
                getQJList(page + "", 1 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
            }
        });
        qingjingGrid.setNumColumns(2);
        int space = DensityUtils.dp2px(AllQingjingActivity.this, 5);
        qingjingGrid.setHorizontalSpacing(space);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, AllQingjingActivity.this, space);
        qingjingGrid.setAdapter(allQingjingGridAdapter);
        qingjingGrid.setOnItemClickListener(this);
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        MapUtil.getCurrentLocation(AllQingjingActivity.this, new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    dialog.show();
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    getQJList(page + "", 1 + "", 0 + "", distance + "", location[0] + "", location[1] + "");
                }
            }
        });
    }

    private void getQJList(String pa, String sort, String fine, String dis, String lng, String lat) {
        ClientDiscoverAPI.qingjingList(pa, sort, fine, dis, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
//                Log.e("<<<情景列表", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.QINGJING_LIST;
                QingJingListBean netQingjing = new QingJingListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingJingListBean>() {
                    }.getType();
                    netQingjing = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
                }
//                handler.sendMessage(msg);
                progressBar.setVisibility(View.GONE);
                pullToRefreshView.onRefreshComplete();
                if (netQingjing.isSuccess()) {
                    if (page == 1) {
                        qingjingList.clear();
                        pullToRefreshView.lastTotalItem = -1;
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                    }
                    qingjingList.addAll(netQingjing.getData().getRows());
                    pullToRefreshView.setLoadingTime();
                    allQingjingGridAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullToRefreshView.onRefreshComplete();
                ToastUtils.showError("网络错误");
            }
        });
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.QINGJING_LIST:
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    pullToRefreshView.onRefreshComplete();
//                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
//                    if (netQingjingListBean.isSuccess()) {
//                        if (page == 1) {
//                            qingjingList.clear();
//                            pullToRefreshView.lastTotalItem = -1;
//                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
//                        }
//                        qingjingList.addAll(netQingjingListBean.getData().getRows());
//                        pullToRefreshView.setLoadingTime();
//                        allQingjingGridAdapter.notifyDataSetChanged();
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    progressBar.setVisibility(View.GONE);
//                    pullToRefreshView.onRefreshComplete();
//                    break;
//            }
//        }
//    };

//    @Override
//    protected void onDestroy() {
//        //cancelNet();
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//            handler = null;
//        }
//        super.onDestroy();
//    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (isSelect == 1) {
//            Intent intent = new Intent();
//            intent.putExtra("qingjing", qingjingList.get(position));
//            Log.e("<<<>>>", qingjingList.get(position).getTitle());
//            setResult(DataConstants.RESULTCODE_SELECTQJ_ALLQJ, intent);
//            finish();
//        } else {
        Intent intent = new Intent(AllQingjingActivity.this, QingjingDetailActivity.class);
        intent.putExtra("id", qingjingList.get(position).get_id());
        startActivity(intent);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_all_qingjing_search:
                onBackPressed();
                break;
            case R.id.activity_all_qingjing_createqinjing:
                if (!LoginInfo.isUserLogin()) {
//                    Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                MainApplication.tag = 2;
                startActivity(new Intent(AllQingjingActivity.this, SelectPhotoOrCameraActivity.class));
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            SharedPreferences firstInSp = getSharedPreferences(DataConstants.SHAREDPREFRENCES_FIRST_IN, Context.MODE_PRIVATE);
            //判断是不是第一次进入Fiu界面
            boolean isFirstIn = firstInSp.getBoolean(DataConstants.FIRST_IN_ALL, true);
            if (isFirstIn) {
                firstRelative.setVisibility(View.VISIBLE);
                firstRelative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        firstRelative.setVisibility(View.GONE);
                    }
                });
                SharedPreferences.Editor editor = firstInSp.edit();
                editor.putBoolean(DataConstants.FIRST_IN_ALL, false);
                editor.apply();
            }
        }
    }
}

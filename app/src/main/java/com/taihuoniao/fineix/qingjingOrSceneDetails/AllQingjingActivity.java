package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.baidu.location.BDLocation;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.scene.SelectPhotoOrCameraActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taihuoniao on 2016/4/25.
 */
public class AllQingjingActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    //界面下的控件
    private ImageView createQingjingImg;
    private PullToRefreshGridView pullToRefreshView;
    private GridView qingjingGrid;
    private List<QingJingListBean.QingJingItem> qingjingList;
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
        setContentView(R.layout.activity_all_qingjing);
        createQingjingImg = (ImageView) findViewById(R.id.activity_all_qingjing_createqinjing);
        pullToRefreshView = (PullToRefreshGridView) findViewById(R.id.activity_all_qingjing_pullrefreshview);
        qingjingGrid = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) findViewById(R.id.activity_all_qingjing_progress);
        dialog = new WaittingDialog(AllQingjingActivity.this);
    }

    @Override
    protected void initList() {
        createQingjingImg.setOnClickListener(this);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                page = 1;
                DataPaser.qingjingList(page + "", 0 + "", distance + "", location[0] + "", location[1] + "", handler);
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
                DataPaser.qingjingList(page + "", 0 + "", distance + "", location[0] + "", location[1] + "", handler);
            }
        });
        qingjingGrid.setNumColumns(2);
        int space = DensityUtils.dp2px(AllQingjingActivity.this, 5);
        qingjingGrid.setHorizontalSpacing(space);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, AllQingjingActivity.this, space);
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
                    DataPaser.qingjingList(page + "", 0 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.QINGJING_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshView.onRefreshComplete();
                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
                    if (netQingjingListBean.isSuccess()) {
                        if (page == 1) {
                            qingjingList.clear();
                            pullToRefreshView.lastTotalItem = -1;
                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        }
                        qingjingList.addAll(netQingjingListBean.getData().getRows());
                        pullToRefreshView.setLoadingTime();
                        allQingjingGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshView.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AllQingjingActivity.this, QingjingDetailActivity.class);
        intent.putExtra("id", qingjingList.get(position).get_id());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_all_qingjing_createqinjing:
                MainApplication.tag = 2;
                startActivity(new Intent(AllQingjingActivity.this, SelectPhotoOrCameraActivity.class));
                break;
        }
    }
}

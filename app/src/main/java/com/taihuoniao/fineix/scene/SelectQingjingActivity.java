package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllQingjingGridAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.AllQingjingActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class SelectQingjingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    //上个界面传递过来的经纬度
    private LatLng latLng;
    @Bind(R.id.activity_select_qingjing_titlelayout)
    GlobalTitleLayout titleLayout;
    @Bind(R.id.activity_select_qingjing_search)
    EditText searchEdt;
    @Bind(R.id.activity_select_qingjing_all)
    TextView allQingjingTv;
    @Bind(R.id.activity_select_qingjing_grid)
    PullToRefreshGridView pullToRefreshView;
    @Bind(R.id.activity_select_qingjing_progress)
    ProgressBar progressBar;
    private GridView qingjingGrid;
    private WaittingDialog dialog;
    //情景列表
    private int page = 1;
    private double distance = 5000;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private AllQingjingGridAdapter allQingjingGridAdapter;


    @Override
    protected void getIntentData() {
        latLng = getIntent().getParcelableExtra("latLng");
    }

    public SelectQingjingActivity() {
        super(R.layout.activity_select_qingjing);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(SelectQingjingActivity.this);
        titleLayout.setBackgroundResource(R.color.white);
        titleLayout.setBackImg(R.mipmap.back_black);
        titleLayout.setTitle(R.string.select_qingjing, getResources().getColor(R.color.black333333));
        titleLayout.setRightTv(R.string.confirm, getResources().getColor(R.color.black333333), this);
        searchEdt.setOnClickListener(this);
        allQingjingTv.setOnClickListener(this);
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                progressBar.setVisibility(View.VISIBLE);
                DataPaser.qingjingList(page + "", 1 + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
            }
        });
        pullToRefreshView.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                page++;
                progressBar.setVisibility(View.VISIBLE);
                DataPaser.qingjingList(page + "", 1 + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
            }
        });
        qingjingGrid = pullToRefreshView.getRefreshableView();
        qingjingGrid.setNumColumns(2);
        int space = DensityUtils.dp2px(SelectQingjingActivity.this, 5);
        qingjingGrid.setHorizontalSpacing(space);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, SelectQingjingActivity.this, space);
        qingjingGrid.setAdapter(allQingjingGridAdapter);
        qingjingGrid.setOnItemClickListener(this);
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.qingjingList(page + "", 1 + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_qingjing_all:
                Intent intent = new Intent(SelectQingjingActivity.this, AllQingjingActivity.class);
                intent.putExtra("isSelect", 1);
                startActivityForResult(intent, DataConstants.REQUESTCODE_SELECTQJ_ALLQJ);
                break;
            case R.id.activity_select_qingjing_search:
                Toast.makeText(SelectQingjingActivity.this, "跳转到地图界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.title_continue:
                Toast.makeText(SelectQingjingActivity.this, "确定", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (resultCode) {
                case DataConstants.RESULTCODE_SELECTQJ_ALLQJ:
                    QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) data.getSerializableExtra("qingjing");
                    if (qingJingItem != null) {
                        Intent intent = new Intent();
                        Log.e("<<<>>>", qingJingItem.getTitle());
                        intent.putExtra("qingjing", qingJingItem);
                        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
                        finish();
                    }
                    break;
            }
        }
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
                    dialog.show();
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
        Intent intent = new Intent();
        intent.putExtra("qingjing", qingjingList.get(position));
        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
        finish();
    }
}

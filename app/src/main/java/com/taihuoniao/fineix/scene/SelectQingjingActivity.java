package com.taihuoniao.fineix.scene;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.HeaderGridView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/28.
 */
public class SelectQingjingActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener {
    //上个界面传递过来的经纬度
    private LatLng latLng;
    @Bind(R.id.activity_select_qingjing_titlelayout)
    GlobalTitleLayout titleLayout;
    private LinearLayout searchLinear;
    private TextView qingjingTv;
    private TextView allQingjingTv;
    @Bind(R.id.activity_select_qingjing_grid)
    HeaderGridView qingjingGrid;
    @Bind(R.id.activity_select_qingjing_progress)
    ProgressBar progressBar;
    //    private GridView qingjingGrid;
    private WaittingDialog dialog;
    //情景列表
    private int page = 1;
    private double distance = 5000;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private AllQingjingGridAdapter allQingjingGridAdapter;
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;
    //判断是否是全部情景的标识
    private int stick = 1;


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
        View header = View.inflate(SelectQingjingActivity.this, R.layout.header_select_qingjing, null);
        searchLinear = (LinearLayout) header.findViewById(R.id.activity_select_qingjing_searchlinear);
        qingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_qingjingtv);
        allQingjingTv = (TextView) header.findViewById(R.id.activity_select_qingjing_all);
        qingjingGrid.addHeaderView(header);
        qingjingGrid.setOnScrollListener(this);
        searchLinear.setOnClickListener(this);
        allQingjingTv.setOnClickListener(this);
        int space = DensityUtils.dp2px(SelectQingjingActivity.this, 5);
        qingjingGrid.setHorizontalSpacing(space);
        qingjingGrid.setVerticalSpacing(space);
        qingjingList = new ArrayList<>();
        allQingjingGridAdapter = new AllQingjingGridAdapter(qingjingList, null, SelectQingjingActivity.this, space);
        qingjingGrid.setAdapter(allQingjingGridAdapter);
        qingjingGrid.setOnItemClickListener(this);
        titleLayout.setFocusable(true);
        titleLayout.setFocusableInTouchMode(true);
        titleLayout.requestFocus();
    }

    @Override
    protected void requestNet() {
        dialog.show();
        DataPaser.qingjingList(page + "", stick + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_select_qingjing_searchlinear:
                Toast.makeText(SelectQingjingActivity.this, "跳转到地图界面", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_select_qingjing_all:
//                Intent intent = new Intent(SelectQingjingActivity.this, AllQingjingActivity.class);
//                intent.putExtra("isSelect", 1);
//                startActivityForResult(intent, DataConstants.REQUESTCODE_SELECTQJ_ALLQJ);
                page = 1;
                if (stick == 1) {
                    stick = 0;
                } else {
                    stick = 1;
                }
                dialog.show();
                DataPaser.qingjingList(page + "", stick + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
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
                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
                    if (netQingjingListBean.isSuccess()) {
                        if (stick == 0) {
                            allQingjingTv.setText("查看推荐");
                            qingjingTv.setText("全部情景");
                        } else {
                            allQingjingTv.setText("查看全部");
                            qingjingTv.setText("推荐情景");
                        }
                        if (page == 1) {
                            qingjingList.clear();
                            lastTotalItem = -1;
                            lastSavedFirstVisibleItem = -1;
                        }
                        qingjingList.addAll(netQingjingListBean.getData().getRows());
                        allQingjingGridAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.show();
                    progressBar.setVisibility(View.GONE);
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
        QingJingListBean.QingJingItem qingJingItem = (QingJingListBean.QingJingItem) qingjingGrid.getAdapter().getItem(position);
        Log.e("<<<", qingJingItem.getTitle());
        Intent intent = new Intent();
        intent.putExtra("qingjing", qingJingItem);
        setResult(DataConstants.RESULTCODE_CREATESCENE_SELECTQJ, intent);
        finish();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount > 1 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                && latLng != null) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            page++;
            progressBar.setVisibility(View.VISIBLE);
            DataPaser.qingjingList(page + "", stick + "", distance + "", latLng.longitude + "", latLng.latitude + "", handler);
        }
    }
}

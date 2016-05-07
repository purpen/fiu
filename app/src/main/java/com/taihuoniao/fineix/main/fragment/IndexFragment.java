package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ImageView searchImg;
    private ImageView subsImg;
    private PullToRefreshListView pullToRefreshLayout;
    private ListView listView;
    private List<SceneListBean> sceneList;
    private SceneListViewAdapter sceneListViewAdapter;
    //场景列表当前页码
    private int currentPage = 1;
    private double distance = 5000;//请求距离
    //当前位置经纬度
    private double[] location = null;
    //网络请求对话框
    private WaittingDialog dialog;


    @Override
    protected void requestNet() {
    }

    @Override
    protected void initList() {
        searchImg.setOnClickListener(this);
        subsImg.setOnClickListener(this);
        pullToRefreshLayout.setPullToRefreshEnabled(false);
//        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                currentPage = 1;
//                if (location == null) {
//                    getCurrentLocation();
//                    return;
//                }
//                DataPaser.getSceneList(currentPage + "", null, null, 0 + "", distance + "", location[0] + "", location[1] + "", handler);
//            }
//        });
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                currentPage++;
                DataPaser.getSceneList(currentPage + "", null, null, 2 + "", distance + "", location[0] + "", location[1] + "", handler);
            }
        });
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null,null);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        MapUtil.getCurrentLocation(getActivity(), new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    dialog.show();
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    DataPaser.getSceneList(currentPage + "", null, null, 2 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
    }

    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_index, null);
        searchImg = (ImageView) view.findViewById(R.id.fragment_index_search);
        subsImg = (ImageView) view.findViewById(R.id.fragment_index_subs);
        pullToRefreshLayout = (PullToRefreshListView) view.findViewById(R.id.fragment_index_pullrefreshview);
        listView = pullToRefreshLayout.getRefreshableView();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    //    @OnClick({R.id.location_btn, R.id.poi_btn,R.id.share_btn,R.id.sliding_tab_btn,R.id.geo,R.id.select_search_qj,R.id.focus})
//    protected void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.location_btn:
//                activity.startActivity(new Intent(activity, HotCitiesActivity.class));
//                break;
//            case R.id.poi_btn:
//                activity.startActivity(new Intent(activity, POIListActivity.class));
//                break;
//            case R.id.share_btn:
//                //TODO 调用所有分享
////                Intent intent = new Intent();
////                intent.setAction(Intent.ACTION_SEND);
////                intent.putExtra(intent.EXTRA_TEXT,"我是分享内容。。。。http://www.baidu.com/");
////                intent.setType("*/*");
////                startActivity(Intent.createChooser(intent, "分享到"));
//
//                //TODO 通过短信分享
//                String content="我是分享内容....";
//                Uri sms = Uri.parse("smsto:");
//                Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, sms);
//                //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
//                sendIntent.putExtra( "sms_body",content);
//                sendIntent.setType("vnd.android-dir/mms-sms" );
//                startActivity(sendIntent);
//                break;
//            case R.id.sliding_tab_btn:
////                activity.startActivity(new Intent(activity, OrderListActivity.class));
//                activity.startActivity(new Intent(activity, SearchResultActivity.class));
//                break;
//            case R.id.geo:
//                activity.startActivity(new Intent(activity, BDSearchAddressActivity.class));
//                break;
//            case R.id.select_search_qj:
//                activity.startActivity(new Intent(activity, SelectOrSearchQJActivity.class));
//                break;
//            case R.id.focus:
//                activity.startActivity(new Intent(activity, CaptureActivity.class));
//                break;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.SCENE_LIST:
                    pullToRefreshLayout.onRefreshComplete();
                    dialog.dismiss();
                    SceneList netSceneList = (SceneList) msg.obj;
                    if (netSceneList.isSuccess()) {
                        pullToRefreshLayout.setLoadingTime();
                        if (currentPage == 1) {
                            sceneList.clear();
                            pullToRefreshLayout.lastTotalItem = -1;
                            pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
                        }
                        sceneList.addAll(netSceneList.getSceneListBeanList());
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    pullToRefreshLayout.onRefreshComplete();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        MapUtil.destroyLocationClient();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        intent.putExtra("id", sceneList.get(position).get_id());
        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_index_subs:
                Toast.makeText(getActivity(), "订阅", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fragment_index_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", 9 + "");
                startActivity(intent);
                break;
        }
    }
}

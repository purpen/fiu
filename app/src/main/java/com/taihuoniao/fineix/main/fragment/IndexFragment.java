package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.baidu.location.BDLocation;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.VideoActivity;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, AbsListView.OnScrollListener, View.OnTouchListener {
    public static IndexFragment instance;
    private View fragment_view;
    private RelativeLayout titlelayout;
    private ImageView searchImg;
    private ImageView subsImg;
    private PullToRefreshListView pullToRefreshLayout;
    private ListView listView;
    private ProgressBar progressBar;
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
        instance = IndexFragment.this;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
    //外界调用刷新场景列表
    public void refreshSceneList(){
        currentPage = 1;
        if (location == null) {
            getCurrentLocation();
            return;
        }
        dialog.show();
        DataPaser.getSceneList(currentPage + "", 8 + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
    }

    @Override
    protected void initList() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.e("<<<状态栏", "statusbarheight=" + getStatusBarHeight());
//            titlelayout.setPadding(0, getStatusBarHeight(), 0, 0);
//        }
        searchImg.setOnClickListener(this);
        subsImg.setOnClickListener(this);
//        pullToRefreshLayout.setPullToRefreshEnabled(false);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (location == null) {
                    getCurrentLocation();
                    return;
                }
                dialog.show();
                DataPaser.getSceneList(currentPage + "", 8 + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
            }
        });
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                DataPaser.getSceneList(currentPage + "", 8 + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
            }
        });
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null, null, null);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
//        listView.setOnScrollListener(this);
        listView.setOnTouchListener(this);
        getCurrentLocation();
    }

    private void getCurrentLocation() {
        dialog.show();
        MapUtil.getCurrentLocation(getActivity(), new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    DataPaser.getSceneList(currentPage + "", 8 + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
    }

    @Override
    protected View initView() {
        fragment_view = View.inflate(getActivity(), R.layout.fragment_index, null);
        titlelayout = (RelativeLayout) fragment_view.findViewById(R.id.fragment_index_title);
        searchImg = (ImageView) fragment_view.findViewById(R.id.fragment_index_search);
        subsImg = (ImageView) fragment_view.findViewById(R.id.fragment_index_subs);
        pullToRefreshLayout = (PullToRefreshListView) fragment_view.findViewById(R.id.fragment_index_pullrefreshview);
        pullToRefreshLayout.animLayout();
        listView = pullToRefreshLayout.getRefreshableView();
        progressBar = (ProgressBar) fragment_view.findViewById(R.id.fragment_index_progress);
        listView.setDividerHeight(0);
        dialog = new WaittingDialog(getActivity());
        return fragment_view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -2:

                    break;
                case DataConstants.SCENE_LIST:
                    pullToRefreshLayout.onRefreshComplete();
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    SceneList netSceneList = (SceneList) msg.obj;
                    if (netSceneList.isSuccess()) {
                        pullToRefreshLayout.setLoadingTime();
                        if (currentPage == 1) {
                            sceneList.clear();
                            pullToRefreshLayout.lastTotalItem = -1;
                            pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
                        }
                        sceneList.addAll(netSceneList.getSceneListBeanList());
                        if (currentPage == 1 && sceneList.size() > 0) {
                            sceneList.get(0).setStartAnim(true);
                        }
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshLayout.onRefreshComplete();
                    ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
//                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(getActivity(), VideoActivity.class));
//                if (!LoginInfo.isUserLogin()) {
////                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
//                    MainApplication.which_activity = DataConstants.ElseActivity;
//                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
//                    return;
//                }
//                startActivity(new Intent(getActivity(), SubsCJListActivity.class));
                break;
            case R.id.fragment_index_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("t", 9 + "");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        Log.e("<<<滑动状态", "state=" + scrollState);
//        if (scrollState == SCROLL_STATE_FLING) {
//            //划过多少个整个的item
//
//        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.e("<<<首页滑动",
//                "scrollY=" + getScrollY());
    }


    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

//    private boolean isMove = false;//判断手指在屏幕上是否移动过

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (listView == null || listView.getChildAt(0) == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startP = new PointF(event.getX(), event.getY());
//                isMove = false;
//                Log.e("<<<按下坐标", "x=" + startP.x + ",y=" + startP.y);
                break;
            case MotionEvent.ACTION_MOVE:
                nowP = new PointF(event.getX(), event.getY());
//                Log.e("<<<移动的坐标", "x=" + nowP.x + ",y=" + nowP.y);
//                isMove = true;
                break;
            case MotionEvent.ACTION_UP:
                if (nowP == null || startP == null) {
                    return false;
                }
//                int i = getScrollY() / (listView.getChildAt(0).getHeight());
                double move = Math.sqrt((nowP.x - startP.x) * (nowP.x - startP.x) + (nowP.y - startP.y) * (nowP.y - startP.y));
                //firstvisibleitem的偏移量
                int s = getScrollY() % (listView.getChildAt(0).getHeight());
                if (nowP.y < startP.y && s > 0.2 * listView.getChildAt(0).getHeight()) {
                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
                    cancelChenjin();
                    anim(listView.getFirstVisiblePosition() + 1);
                } else if (nowP.y < startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
                } else if (nowP.y > startP.y && s < 0.8 * listView.getChildAt(0).getHeight() && s > 0) {
                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
                    chenjin();
                    anim(listView.getFirstVisiblePosition());
                } else if (nowP.y > startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
                }
                nowP = null;
                startP = null;
                break;
        }
        return false;
    }

    private void anim(int position) {
        for (int i = 0; i < sceneList.size(); i++) {
            if (i == position) {
                sceneList.get(i).setStartAnim(true);
            } else {
                sceneList.get(i).setStartAnim(false);
            }
        }
//        sceneList.get(listView.getFirstVisiblePosition()+1).setStartAnim(true);
        sceneListViewAdapter.notifyDataSetChanged();
    }

    private void cancelChenjin() {
        searchImg.setVisibility(View.GONE);
        subsImg.setVisibility(View.GONE);
        Intent intent = new Intent();
        intent.putExtra("index", 1);
        intent.setAction(DataConstants.BroadShopCart);
        getActivity().sendBroadcast(intent);
    }

    private void chenjin() {
        searchImg.setVisibility(View.VISIBLE);
        subsImg.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.putExtra("index", 2);
        intent.setAction(DataConstants.BroadShopCart);
        getActivity().sendBroadcast(intent);
    }

    private PointF startP, nowP;
}

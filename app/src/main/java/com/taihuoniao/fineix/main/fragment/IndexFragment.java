package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SubsCJListActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
import com.taihuoniao.fineix.view.svprogress.SVProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, AbsListView.OnScrollListener {
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
    private SVProgressHUD dialog;


    @Override
    protected void requestNet() {

    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void initList() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.e("<<<状态栏", "statusbarheight=" + getStatusBarHeight());
            titlelayout.setPadding(0, getStatusBarHeight(), 0, 0);
        }
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
        listView.setOnScrollListener(this);
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
        listView = pullToRefreshLayout.getRefreshableView();
        progressBar = (ProgressBar) fragment_view.findViewById(R.id.fragment_index_progress);
        listView.setDividerHeight(DensityUtils.dp2px(getActivity(), 5));
        dialog = new SVProgressHUD(getActivity());
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
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    pullToRefreshLayout.onRefreshComplete();
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
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
                if (!LoginInfo.isUserLogin()) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    MainApplication.which_activity = DataConstants.ElseActivity;
                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
                    return;
                }
                startActivity(new Intent(getActivity(), SubsCJListActivity.class));
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
        Log.e("<<<滑动状态", "state=" + scrollState);
        ViewPager viewPager = new ViewPager(getActivity());
        viewPager.setAdapter(new FragmentStatePagerAdapter() {
            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public Fragment getItem(int position) {
                return null;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        });
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.e("<<<首页滑动",
                "first=" + firstVisibleItem + ",visible=" + visibleItemCount + ",total=" + totalItemCount);
    }
}

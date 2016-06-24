package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.dodola.bubblecloud.BubbleCloudView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.FiuUsersAdapter;
import com.taihuoniao.fineix.adapters.JingQingjingRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.CJHotLabelBean;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.HotCitiesActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.AllQingjingActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FindFragment extends BaseFragment<Banner> implements AdapterView.OnItemClickListener, View.OnClickListener, EditRecyclerAdapter.ItemClick, AbsListView.OnScrollListener {
    //    public static FindFragment instance;
    private final String PAGE_NAME = "app_fiu_sight_index_slide"; //TODO 换成场景banner
    //标签列表
    private List<String> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //图片加载
    private DisplayImageOptions options;
    private ViewPagerAdapter viewPagerAdapter;
    //场景列表
    private int currentPage = 1;//页码
    private double distance = 5000;//距离
    private double[] location = null;
    private ImageView searchImg;
    private ImageView locationImg;
    private PullToRefreshListView pullToRefreshView;
    private ListView sceneListView;
    private List<SceneListBean> sceneList;
    private SceneListViewAdapter sceneListViewAdapter;
    private ProgressBar progressBar;
    //HeaderView中的控件
    private ScrollableView scrollableView;
    private TextView allQingjingTv;
    private RecyclerView qingjingRecycler;
    private List<QingJingListBean.QingJingItem> qingjingList;
    private JingQingjingRecyclerAdapter jingQingjingRecyclerAdapter;
    private RecyclerView labelRecycler;
    private BubbleCloudView absoluteLayout;
    //网络请求对话框
    private WaittingDialog dialog;
    //listview分页加载
//    private int lastSavedFirstVisibleItem = -1;
//    private int lastTotalItem = -1;
//    private GestureDetector gestureDetector;


    @Override
    protected View initView() {
//        instance = FindFragment.this;
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        RelativeLayout titlelayout = (RelativeLayout) view.findViewById(R.id.fragment_find_titlelayout);
        searchImg = (ImageView) view.findViewById(R.id.fragment_find_search);
        locationImg = (ImageView) view.findViewById(R.id.fragment_find_location);
        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.fragment_find_scenelistview);
        sceneListView = pullToRefreshView.getRefreshableView();
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_find_progress);
        View headerView = View.inflate(getActivity(), R.layout.header_fragment_find, null);
        scrollableView = (ScrollableView) headerView.findViewById(R.id.scrollableView);
        allQingjingTv = (TextView) headerView.findViewById(R.id.fragment_find_allqingjing);
        qingjingRecycler = (RecyclerView) headerView.findViewById(R.id.fragment_find_qingjing_recycler);
        labelRecycler = (RecyclerView) headerView.findViewById(R.id.fragment_find_labelrecycler);
        absoluteLayout = (BubbleCloudView) headerView.findViewById(R.id.fragment_find_absolute);
        sceneListView.addHeaderView(headerView);
        absoluteLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                dis(absoluteLayout.getParent());
                absoluteLayout.onTouchEvent(event);
                return true;
            }
        });
        sceneListView.setDividerHeight(DensityUtils.dp2px(getActivity(), 5));
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void dis(ViewParent viewParent) {
        viewParent.requestDisallowInterceptTouchEvent(true);
        if (viewParent.getParent() != null) {
            dis(viewParent.getParent());
        }
    }

    @Override
    protected void initList() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Log.e("<<<状态栏", "statusbarheight=" + getStatusBarHeight());
//            titlelayout.setPadding(0, getStatusBarHeight(), 0, 0);
//        }
        pullToRefreshView.animLayout();
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                location = null;
                requestNet();
//                absoluteLayout.removeAllViews();
//                DataPaser.fiuUserList(1 + "", 40 + "", null, null, 1 + "", handler);
            }
        });
        searchImg.setOnClickListener(this);
        locationImg.setOnClickListener(this);
        ViewGroup.LayoutParams lp = scrollableView.getLayoutParams();
        lp.width = MainApplication.getContext().getScreenWidth();
        lp.height = lp.width * 422 / 750;
        scrollableView.setLayoutParams(lp);
        scrollableView.setFocusable(true);
        scrollableView.setFocusableInTouchMode(true);
        scrollableView.requestFocus();
        allQingjingTv.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        qingjingRecycler.setLayoutManager(linearLayoutManager);
        qingjingList = new ArrayList<>();
        jingQingjingRecyclerAdapter = new JingQingjingRecyclerAdapter(getActivity(), qingjingList, this, qingjingRecycler.getMeasuredHeight());
        qingjingRecycler.setAdapter(jingQingjingRecyclerAdapter);
        hotLabelList = new ArrayList<>();
        labelRecycler.setHasFixedSize(true);
        labelRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));
        pinLabelRecyclerAdapter = new PinLabelRecyclerAdapter(getActivity(), hotLabelList, new EditRecyclerAdapter.ItemClick() {
            @Override
            public void click(int postion) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("q", hotLabelList.get(postion));
                intent.putExtra("t", "8");
                startActivity(intent);
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null, null, null);
        sceneListView.setAdapter(sceneListViewAdapter);
        sceneListView.setOnScrollListener(this);
        sceneListView.setOnItemClickListener(this);
        MapUtil.getCurrentLocation(getActivity(), new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
//                    dialog.show();
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
//                    MapUtil.destroyLocationClient();
//                    DataPaser.qingjingList(1 + "", 2 + "", 1 + "", distance + "", location[0] + "", location[1] + "", handler);
                    getQJList();
                    getCJList();
//                    DataPaser.getSceneList(currentPage + "", null, null, 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.default_background_750_1334)
                .showImageForEmptyUri(R.mipmap.default_background_750_1334)
                .showImageOnFail(R.mipmap.default_background_750_1334)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .build();
        absoluteLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), UserCenterActivity.class);
                intent.putExtra(FocusActivity.USER_ID_EXTRA, Long.parseLong(netUsers.getData().getUsers().get(position).get_id()));
                startActivity(intent);
            }
        });
    }


    private void getCJList() {
        ClientDiscoverAPI.getSceneList(currentPage + "", null, null, 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Log.e("<<<场景列表", responseInfo.result);
//                WriteJsonToSD.writeToSD("json", responseInfo.result);
//                Message msg = handler.obtainMessage();
//                msg.what = DataConstants.SCENE_LIST;
                SceneList sceneList1 = new SceneList();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    sceneList1.setSuccess(jsonObject.optBoolean("success"));
                    sceneList1.setMessage(jsonObject.optString("message"));
//                    sceneList.setStatus(jsonObject.optString("status"));
                    if (sceneList1.isSuccess()) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray rows = data.getJSONArray("rows");
                        List<SceneListBean> list = new ArrayList<>();
                        for (int i = 0; i < rows.length(); i++) {
                            JSONObject job = rows.getJSONObject(i);
                            SceneListBean sceneListBean = new SceneListBean();
                            sceneListBean.set_id(job.optString("_id"));
                            sceneListBean.setAddress(job.optString("address"));
                            sceneListBean.setScene_title(job.optString("scene_title"));
                            sceneListBean.setView_count(job.optString("view_count"));
                            sceneListBean.setCreated_at(job.optString("created_at"));
                            sceneListBean.setLove_count(job.optString("love_count"));
                            sceneListBean.setCover_url(job.optString("cover_url"));
                            sceneListBean.setTitle(job.optString("title"));
                            sceneListBean.setDes(job.optString("des"));
                            JSONObject us = job.getJSONObject("user_info");
                            SceneListBean.User user = new SceneListBean.User();
                            user.setAccount(us.optString("account"));
//                            user.setLabel(us.optString("label"));
                            user.is_expert = us.optInt("is_expert");
                            user.expert_info = us.optString("expert_info");
                            user.expert_label = us.optString("expert_label");
                            user.setUser_id(us.optString("user_id"));
                            user.setSummary(us.optString("summary"));
                            user.setNickname(us.optString("nickname"));
                            user.setLove_count(us.optString("love_count"));
                            user.setFollow_count(us.optString("follow_count"));
                            user.setFans_count(us.optString("fans_count"));
//                            user.setCounter(us.optString("counter"));
                            user.setAvatar_url(us.optString("avatar_url"));
                            sceneListBean.setUser_info(user);
                            JSONArray product = job.getJSONArray("product");
                            List<SceneListBean.Products> productsList = new ArrayList<>();
                            for (int j = 0; j < product.length(); j++) {
                                JSONObject ob = product.getJSONObject(j);
                                SceneListBean.Products products = new SceneListBean.Products();
                                products.setId(ob.optString("id"));
                                products.setTitle(ob.optString("title"));
                                products.setPrice(ob.optString("price"));
                                products.setX(ob.optDouble("x"));
                                products.setY(ob.optDouble("y"));
                                productsList.add(products);
                            }
                            sceneListBean.setProductsList(productsList);
                            list.add(sceneListBean);
                        }
                        sceneList1.setSceneListBeanList(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                SceneList netSceneList = sceneList1;
                if (netSceneList.isSuccess()) {
                    pullToRefreshView.setLoadingTime();
                    if (currentPage == 1) {
                        sceneList.clear();
                        pullToRefreshView.lastSavedFirstVisibleItem = -1;
                        pullToRefreshView.lastTotalItem = -1;
                    }
                    sceneList.addAll(netSceneList.getSceneListBeanList());
//                        Toast.makeText(getActivity(), "测试，场景数据个数=" + sceneList.size(), Toast.LENGTH_SHORT).show();
                    sceneListViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netSceneList.getMessage());
//                        dialog.showErrorWithStatus(netSceneList.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    private void getQJList() {
        ClientDiscoverAPI.qingjingList(1 + "", 2 + "", 1 + "", distance + "", location[0] + "", location[1] + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                QingJingListBean qingJingListBean = new QingJingListBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<QingJingListBean>() {
                    }.getType();
                    qingJingListBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据异常：" + e.toString());
                }
                QingJingListBean netQingjingListBean = qingJingListBean;
                if (netQingjingListBean.isSuccess()) {
                    qingjingList.clear();
                    qingjingList.addAll(netQingjingListBean.getData().getRows());
//                        Toast.makeText(getActivity(), "测试，情景数据个数=" + qingjingList.size(), Toast.LENGTH_SHORT).show();
                    jingQingjingRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netQingjingListBean.getMessage());
//                        dialog.showErrorWithStatus(netQingjingListBean.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
            }
        });
    }


    @Override
    protected void requestNet() {
        dialog.show();
        ClientDiscoverAPI.fiuUserList(1 + "", 100 + "", null, null, 1 + "", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                FiuUserListBean fiuUserListBean = new FiuUserListBean();
                try {
                    Gson gson = new Gson();
                    Type type1 = new TypeToken<FiuUserListBean>() {
                    }.getType();
                    fiuUserListBean = gson.fromJson(responseInfo.result, type1);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<", "数据解析异常" + e.toString());
                }
                FiuUserListBean netUser = fiuUserListBean;
                netUsers = netUser;
                if (netUser.isSuccess()) {
                    FiuUsersAdapter fiuUsersAdapter = new FiuUsersAdapter(getActivity(), netUser);
//                    absoluteLayout.init(getActivity(), netUser, null);
                    absoluteLayout.setAdapter(fiuUsersAdapter);
                } else {
                    ToastUtils.showError(netUser.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
            }
        });
        ClientDiscoverAPI.getBanners(PAGE_NAME, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try {
                    BannerData bannerData = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<BannerData>>() {
                    });
                    if (bannerData == null) {
                        return;
                    }

                    if (bannerData.rows == null) {
                        return;
                    }

                    if (bannerData.rows.size() == 0) {
                        return;
                    }
                    refreshUI(bannerData.rows);
                } catch (JsonSyntaxException e) {
                    Util.makeToast(activity, "对不起,数据异常");
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                Util.makeToast(s);
            }
        });
        //场景页热门标签
//        DataPaser.cjHotLabel(true, handler);
        ClientDiscoverAPI.cjHotLabel(true, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CJHotLabelBean cjHotLabelBean = new CJHotLabelBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<CJHotLabelBean>() {
                    }.getType();
                    cjHotLabelBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<场景热门标签", "数据解析异常");
                }
                CJHotLabelBean netHot = cjHotLabelBean;
                if (netHot.isSuccess()) {
                    hotLabelList.clear();
                    hotLabelList.addAll(netHot.getData().getTags());
                    pinLabelRecyclerAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(netHot.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                pullToRefreshView.onRefreshComplete();
                progressBar.setVisibility(View.GONE);
                ToastUtils.showError("网络错误");
            }
        });
    }

    private FiuUserListBean netUsers;
//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DataConstants.FIU_USER:
////                    dialog.dismiss();
////                    pullToRefreshView.onRefreshComplete();
//                    FiuUserListBean netUser = (FiuUserListBean) msg.obj;
//                    netUsers = netUser;
//                    if (netUser.isSuccess()) {
//                        absoluteLayout.init(getActivity(), netUser, null);
//                    } else {
//                        ToastUtils.showError(netUser.getMessage());
////                        dialog.showErrorWithStatus(netUser.getMessage());
//                    }
//                    break;
//                case DataConstants.QINGJING_LIST:
//                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
//                    if (netQingjingListBean.isSuccess()) {
//                        qingjingList.clear();
//                        qingjingList.addAll(netQingjingListBean.getData().getRows());
////                        Toast.makeText(getActivity(), "测试，情景数据个数=" + qingjingList.size(), Toast.LENGTH_SHORT).show();
//                        jingQingjingRecyclerAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showError(netQingjingListBean.getMessage());
////                        dialog.showErrorWithStatus(netQingjingListBean.getMessage());
//                    }
//                    break;
//                case DataConstants.CJ_HOTLABEL:
////                    pullToRefreshView.onRefreshComplete();
//                    CJHotLabelBean netHot = (CJHotLabelBean) msg.obj;
//                    if (netHot.isSuccess()) {
//                        hotLabelList.clear();
//                        hotLabelList.addAll(netHot.getData().getTags());
//                        pinLabelRecyclerAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showError(netHot.getMessage());
//                    }
//                    break;
//                case DataConstants.SCENE_LIST:
//                    dialog.dismiss();
//                    pullToRefreshView.onRefreshComplete();
//                    progressBar.setVisibility(View.GONE);
//                    SceneList netSceneList = (SceneList) msg.obj;
//                    if (netSceneList.isSuccess()) {
//                        pullToRefreshView.setLoadingTime();
//                        if (currentPage == 1) {
//                            sceneList.clear();
//                            pullToRefreshView.lastSavedFirstVisibleItem = -1;
//                            pullToRefreshView.lastTotalItem = -1;
//                        }
//                        sceneList.addAll(netSceneList.getSceneListBeanList());
////                        Toast.makeText(getActivity(), "测试，场景数据个数=" + sceneList.size(), Toast.LENGTH_SHORT).show();
//                        sceneListViewAdapter.notifyDataSetChanged();
//                    } else {
//                        ToastUtils.showError(netSceneList.getMessage());
////                        dialog.showErrorWithStatus(netSceneList.getMessage());
//                    }
//                    break;
//                case DataConstants.NET_FAIL:
//                    dialog.dismiss();
//                    pullToRefreshView.onRefreshComplete();
//                    progressBar.setVisibility(View.GONE);
//                    ToastUtils.showError("网络错误");
////                    dialog.showErrorWithStatus("网络错误");
//                    break;
//            }
//        }
//    };
//    private Thread thread;

    @Override
    public void onPause() {
        super.onPause();
        if (scrollableView != null) {
            scrollableView.stop();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        //用户大小不一的头像
//        if (absoluteLayout.getChildCount() <= 0) {
//            DataPaser.fiuUserList(1 + "", 40 + "", null, null, 1 + "", handler);
//        }
        if (scrollableView != null) {
            scrollableView.start();
        }
    }


    @Override
    protected void refreshUI(ArrayList<Banner> list) {
//        ArrayList<String> urlList = new ArrayList<>();
//        for (Banner banner : list) {
//            urlList.add(banner.cover_url);
//        }

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, list);
            scrollableView.setAdapter(viewPagerAdapter.setInfiniteLoop(true));
            scrollableView.setAutoScrollDurationFactor(8);
            scrollableView.setInterval(4000);
            scrollableView.showIndicators();
            scrollableView.start();
        } else {
            viewPagerAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onDestroy() {
        //        cancelNet();
        MapUtil.destroyLocationClient();
//        if (thread != null && thread.isAlive()) {
//            thread.stop();
//        }
//        if (handler != null) {
//            handler.removeCallbacksAndMessages(null);
//            handler = null;
//        }
        super.onDestroy();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        SceneListBean sceneListBean = (SceneListBean) parent.getAdapter().getItem(position);
        intent.putExtra("id", sceneListBean.get_id());
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_find_search:
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                intent1.putExtra("t", "8");
                startActivity(intent1);
                break;
            case R.id.fragment_find_location:
                Intent intent = new Intent(getActivity(), HotCitiesActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_find_allqingjing:
                startActivity(new Intent(getActivity(), AllQingjingActivity.class));
                break;
        }
    }

    @Override
    public void click(int postion) {
        Intent intent = new Intent(getActivity(), QingjingDetailActivity.class);
        intent.putExtra("id", qingjingList.get(postion).get_id());
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //由于添加了headerview的原因，所以visibleitemcount要大于1，正常只需要大于0就可以
        if (visibleItemCount > sceneListView.getHeaderViewsCount() && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != pullToRefreshView.lastSavedFirstVisibleItem && pullToRefreshView.lastTotalItem != totalItemCount
                && location != null) {
            pullToRefreshView.lastSavedFirstVisibleItem = firstVisibleItem;
            pullToRefreshView.lastTotalItem = totalItemCount;
            currentPage++;
            progressBar.setVisibility(View.VISIBLE);
            getCJList();
//            DataPaser.getSceneList(currentPage + "", null, null, 0 + "", 0 + "", distance + "", location[0] + "", location[1] + "", handler);
        }
    }
}

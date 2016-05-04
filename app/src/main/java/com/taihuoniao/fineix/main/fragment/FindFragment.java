package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.JingQingjingRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.adapters.ViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.Banner;
import com.taihuoniao.fineix.beans.BannerData;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.beans.RandomImg;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.UserListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.map.HotCitiesActivity;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.qingjingOrSceneDetails.AllQingjingActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.ScrollableView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FindFragment extends BaseFragment<Banner> implements AdapterView.OnItemClickListener, View.OnClickListener, EditRecyclerAdapter.ItemClick, AbsListView.OnScrollListener {
    private static final String PAGE_NAME = "app_fiu_sight_index_slide"; //TODO 换成场景banner
    //标签列表
    private List<HotLabel.HotLabelBean> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //图片加载
    private DisplayImageOptions options;
    private ViewPagerAdapter viewPagerAdapter;
    //场景列表
    private int currentPage = 1;//页码
    private double distance = 5000;//距离
    private double[] location = null;
    //界面下的控件
    private ImageView searchImg;
    private ImageView locationImg;
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
    private AbsoluteLayout absoluteLayout;
    //网络请求对话框
    private WaittingDialog dialog;
    //listview分页加载
    private int lastSavedFirstVisibleItem = -1;
    private int lastTotalItem = -1;


    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        searchImg = (ImageView) view.findViewById(R.id.fragment_find_search);
        locationImg = (ImageView) view.findViewById(R.id.fragment_find_location);
        sceneListView = (ListView) view.findViewById(R.id.fragment_find_scenelistview);
        progressBar = (ProgressBar) view.findViewById(R.id.fragment_find_progress);
        View headerView = View.inflate(getActivity(), R.layout.header_fragment_find, null);
        scrollableView = (ScrollableView) headerView.findViewById(R.id.scrollableView);
        allQingjingTv = (TextView) headerView.findViewById(R.id.fragment_find_allqingjing);
        qingjingRecycler = (RecyclerView) headerView.findViewById(R.id.fragment_find_qingjing_recycler);
        labelRecycler = (RecyclerView) headerView.findViewById(R.id.fragment_find_labelrecycler);
        absoluteLayout = (AbsoluteLayout) headerView.findViewById(R.id.fragment_find_absolute);
        sceneListView.addHeaderView(headerView);
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
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
                Toast.makeText(getActivity(), "点击条目 = " + postion, Toast.LENGTH_SHORT).show();
            }
        });
        labelRecycler.addItemDecoration(new PinLabelRecyclerAdapter.LabelItemDecoration(getActivity()));
        labelRecycler.setAdapter(pinLabelRecyclerAdapter);
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null);
        sceneListView.setAdapter(sceneListViewAdapter);
        sceneListView.setOnScrollListener(this);
        sceneListView.setOnItemClickListener(this);
        MapUtil.getCurrentLocation(getActivity(), new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    dialog.show();
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    DataPaser.qingjingList(1 + "", 1 + "", distance + "", location[0] + "", location[1] + "", handler);
                    DataPaser.getSceneList(currentPage + "", null, null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
        options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.mipmap.default_backround)
//                .showImageForEmptyUri(R.mipmap.default_backround)
//                .showImageOnFail(R.mipmap.default_backround)
                .cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(360)).build();
    }

    @Override
    protected void requestNet() {
        dialog.show();
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
        //用户大小不一的头像
        DataPaser.userList(1 + "", 50 + "", null, null, handler);
        //热门标签
        ClientDiscoverAPI.labelList(null, 1, null, 2, 1, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Message msg = handler.obtainMessage();
                msg.what = DataConstants.HOT_LABEL_LIST;
                msg.obj = new HotLabel();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<HotLabel>() {
                    }.getType();
                    msg.obj = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Toast.makeText(getActivity(), "数据异常", Toast.LENGTH_SHORT).show();
                }
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.e("<<<", "请求失败" + error.toString() + ",msg=" + msg);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DataConstants.USER_LIST:
                    dialog.dismiss();
                    UserListBean netUserListBean = (UserListBean) msg.obj;
                    if (netUserListBean.isSuccess()) {
                        addImgToAbsolute(netUserListBean.getData().getRows());
                    }
                    break;
                case DataConstants.QINGJING_LIST:
                    dialog.dismiss();
                    QingJingListBean netQingjingListBean = (QingJingListBean) msg.obj;
                    if (netQingjingListBean.isSuccess()) {
                        qingjingList.addAll(netQingjingListBean.getData().getRows());
                        jingQingjingRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.HOT_LABEL_LIST:
                    dialog.dismiss();
                    HotLabel netHotLabel = (HotLabel) msg.obj;
                    if (netHotLabel.isSuccess()) {
                        hotLabelList.addAll(netHotLabel.getData().getRows());
                        pinLabelRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.SCENE_LIST:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    SceneList netSceneList = (SceneList) msg.obj;
                    if (netSceneList.isSuccess()) {
                        if (currentPage == 1) {
                            sceneList.clear();
                            lastSavedFirstVisibleItem = -1;
                            lastTotalItem = -1;
                        }
                        sceneList.addAll(netSceneList.getSceneListBeanList());
                        sceneListViewAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

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
        if (scrollableView != null) {
            scrollableView.start();
        }
    }

    @Override
    protected void refreshUI(ArrayList<Banner> list) {
        ArrayList<String> urlList = new ArrayList<String>();
        for (Banner banner : list) {
            urlList.add(banner.cover_url);
        }
        if (urlList.size() == 0) {
            return;
        }

        if (viewPagerAdapter == null) {
            viewPagerAdapter = new ViewPagerAdapter(activity, urlList);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }


    private void addImgToAbsolute(List<UserListBean.UserListItem> list) {
        Random random = new Random();
        List<RandomImg> randomImgs = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RandomImg randomImg = new RandomImg();
            randomImg.id = list.get(i).get_id();
            randomImg.url = list.get(i).getMedium_avatar_url();
            randomImg.type = list.get(i).getAvatar_size_type();
            if (randomImg.type.equals("1")) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 21) / 2;
            } else if (randomImg.type.equals("2")) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 33) / 2;
            } else {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 51) / 2;
            }
            randomImgs.add(randomImg);
        }
        int top = absoluteLayout.getTop();
        int bottom = absoluteLayout.getBottom();
        if (bottom - top <= 0) {
            return;
        }
        for (int i = 0; i < randomImgs.size(); i++) {
            RandomImg randomImg = randomImgs.get(i);
            whi:
            for (int k = 0; k < 200; k++) {
                int x = random.nextInt(MainApplication.getContext().getScreenWidth());
                int y = random.nextInt(bottom - top) + top;
                if (MainApplication.getContext().getScreenWidth() - x < randomImg.radius || x < randomImg.radius ||
                        bottom - y < randomImg.radius || y - top < randomImg.radius) {
                    continue;
                }
                for (int j = 0; j < absoluteLayout.getChildCount(); j++) {
                    ImageView img1 = (ImageView) absoluteLayout.getChildAt(j);
                    RandomImg randomImg1 = (RandomImg) img1.getTag();
                    if (randomImg1 == null) {
                        continue;
                    }
                    if (Math.sqrt((randomImg1.x - x) * (randomImg1.x - x) + (randomImg1.y - y) * (randomImg1.y - y)) < randomImg1.radius + randomImg.radius) {
                        continue whi;
                    }
                }
                randomImg.x = x;
                randomImg.y = y;
                break;
            }
            if (randomImg.x == 0 && randomImg.y == 0) {
                continue;
            }
            ImageView img = new ImageView(getActivity());
            ImageLoader.getInstance().displayImage(randomImgs.get(i).url, img, options);
            img.setLayoutParams(new AbsoluteLayout.LayoutParams(randomImg.radius * 2, randomImg.radius * 2,
                    randomImg.x - randomImg.radius, randomImg.y - top - randomImg.radius));
            img.setTag(randomImg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RandomImg randomImg1 = (RandomImg) v.getTag();
                    Toast.makeText(getActivity(), "又点击了" + randomImg1.id, Toast.LENGTH_SHORT).show();
                }
            });
            absoluteLayout.addView(img);
        }
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
                Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
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
        if (visibleItemCount > 1 && (firstVisibleItem + visibleItemCount >= totalItemCount)
                && firstVisibleItem != lastSavedFirstVisibleItem && lastTotalItem != totalItemCount
                && location != null) {
            lastSavedFirstVisibleItem = firstVisibleItem;
            lastTotalItem = totalItemCount;
            currentPage++;
            progressBar.setVisibility(View.VISIBLE);
            DataPaser.getSceneList(currentPage + "", null, null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
        }
    }
}

package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.EditRecyclerAdapter;
import com.taihuoniao.fineix.adapters.PinLabelRecyclerAdapter;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.HotLabel;
import com.taihuoniao.fineix.beans.HotLabelBean;
import com.taihuoniao.fineix.beans.RandomImg;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.network.DataPaser;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.MapUtil;
import com.taihuoniao.fineix.view.ListViewForScrollView;
import com.taihuoniao.fineix.view.MyScrollView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FindFragment extends BaseFragment implements View.OnTouchListener, AdapterView.OnItemClickListener, MyScrollView.OnScrollListener {
    //    private ScrollView scrollView;
    private MyScrollView scrollView;
    private int lastScrollY = -1;
    private int lastScrollViewMesureHeight = -1;
    private RelativeLayout bannerRelative;
    private AbsoluteLayout absoluteLayout;
    private RecyclerView labelRecycler;
    //    private PullToRefreshListViewForScrollView pullToRefreshLayout;
    private ListViewForScrollView sceneListView;
    //    private PullToRefreshListView pullToRefreshView;
//    private int lastFirstVisibleItem = -1;
//    private int lastTotalItemCount = -1;
    //    private ListView listView;
    private WaittingDialog dialog;
    //标签列表
    private List<HotLabelBean> hotLabelList;
    private PinLabelRecyclerAdapter pinLabelRecyclerAdapter;
    private int labelPage = 1;
    //图片加载
    private DisplayImageOptions options;
    //场景列表
    private int currentPage = 1;//页码
    private double distance = 5000;//距离
    private double[] location = null;
    private List<SceneListBean> sceneList;
    private SceneListViewAdapter sceneListViewAdapter;


    @Override
    protected View initView() {
        View view = View.inflate(getActivity(), R.layout.fragment_find, null);
        scrollView = (MyScrollView) view.findViewById(R.id.fragment_find_scrollview);
        bannerRelative = (RelativeLayout) view.findViewById(R.id.fragment_find_banner_relative);
        labelRecycler = (RecyclerView) view.findViewById(R.id.fragment_find_labelrecycler);
        absoluteLayout = (AbsoluteLayout) view.findViewById(R.id.fragment_find_absolute);
//        pullToRefreshLayout = (PullToRefreshListViewForScrollView) view.findViewById(R.id.fragment_find_pullrefresh);
//        listView = pullToRefreshLayout.getRefreshableView();
        sceneListView = (ListViewForScrollView) view.findViewById(R.id.fragment_find_scenelistview);
//        pullToRefreshView = (PullToRefreshListView) view.findViewById(R.id.fragment_find_pullrefreshview);
//        listView = pullToRefreshView.getRefreshableView();
        dialog = new WaittingDialog(getActivity());
        return view;
    }

    @Override
    protected void initList() {
//        scrollView.setOnTouchListener(this);
        scrollView.setOnScrollListener(this);
        bannerRelative.setFocusable(true);
        bannerRelative.setFocusableInTouchMode(true);
        bannerRelative.requestFocus();
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
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList);
//        listView.setAdapter(sceneListViewAdapter);
        sceneListView.setAdapter(sceneListViewAdapter);
        sceneListView.setOnItemClickListener(this);
        MapUtil.getCurrentLocation(getActivity(), new MapUtil.OnReceiveLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (location == null && bdLocation != null) {
                    dialog.show();
                    location = new double[]{bdLocation.getLongitude(), bdLocation.getLatitude()};
                    MapUtil.destroyLocationClient();
                    DataPaser.getSceneList(currentPage + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
                }
            }
        });
//        pullToRefreshLayout.setOnScrollListener(this);
//        sceneListView.setOnScrollListener(this);
//        pullToRefreshView.setOnScrollListener(this);
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
        DataPaser.hotLabelList(labelPage + "", handler);
        //虚拟数据
        handler.sendEmptyMessage(-2);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -2:
                    //添加大小不一的头像
                    addImgToAbsolute();
                    break;
                case DataConstants.HOT_LABEL_LIST:
                    dialog.dismiss();
                    HotLabel netHotLabel = (HotLabel) msg.obj;
                    if (netHotLabel.isSuccess()) {
                        hotLabelList.addAll(netHotLabel.getHotLabelBeanList());
                        pinLabelRecyclerAdapter.notifyDataSetChanged();
                    }
                    break;
                case DataConstants.SCENE_LIST:
                    dialog.dismiss();
                    SceneList netSceneList = (SceneList) msg.obj;
                    if (netSceneList.isSuccess()) {
//                        if (netSceneList.getSceneListBeanList().size() == 0) {
//                            Toast.makeText(getActivity(), "没有更多场景了", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        if (currentPage == 1) {
                            sceneList.clear();
                        }
                        sceneList.addAll(netSceneList.getSceneListBeanList());
                        sceneListViewAdapter.notifyDataSetChanged();
//                        ViewGroup.LayoutParams lp = pullToRefreshView.getLayoutParams();
//                        lp.height = MainApplication.getContext().getScreenWidth() * 16 / 9 * sceneList.size();
//                        pullToRefreshView.setLayoutParams(lp);
                    }
                    break;
                case DataConstants.NET_FAIL:
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        //        cancelNet();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.onDestroy();
    }


    private void addImgToAbsolute() {
        Random random = new Random();
        List<RandomImg> randomImgs = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            RandomImg randomImg = new RandomImg();
            randomImg.url = "http://frbird.qiniudn.com/scene_product/160413/570de6b63ffca2e3108bc4b3-1-p500x500.jpg";
            randomImg.type = random.nextInt(3) + 1;
            if (randomImg.type == 1) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 21) / 2;
            } else if (randomImg.type == 2) {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 33) / 2;
            } else {
                randomImg.radius = DensityUtils.dp2px(getActivity(), 51) / 2;
            }
            randomImgs.add(randomImg);
        }
        int top = absoluteLayout.getTop();
        int bottom = absoluteLayout.getBottom();
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
                    Toast.makeText(getActivity(), "又点击了", Toast.LENGTH_SHORT).show();
                }
            });
            absoluteLayout.addView(img);
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        if (scrollView.getHeight() > 0 && sceneListView.getMeasuredHeight() > 0
//                && (scrollView.getScrollY() + scrollView.getHeight() + MainApplication.getContext().getScreenWidth() * 16 / 9 >= scrollView.getChildAt(0).getMeasuredHeight())) {
//            if (scrollView.getScrollY() != lastScrollY && lastScrollViewMesureHeight != scrollView.getChildAt(0).getMeasuredHeight()) {
//                lastScrollY = scrollView.getScrollY();
//                lastScrollViewMesureHeight = scrollView.getChildAt(0).getMeasuredHeight();
//                //网络请求
//                currentPage++;
//                DataPaser.getSceneList(currentPage + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
//            }
//        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        intent.putExtra("id", sceneList.get(position).get_id());
        startActivity(intent);
    }

    @Override
    public void scroll(ScrollView scrollView, int l, int t, int oldl, int oldt) {
        if (scrollView.getHeight() > 0 && sceneListView.getMeasuredHeight() > 0
                && (scrollView.getScrollY() + scrollView.getHeight() + MainApplication.getContext().getScreenWidth() * 16 / 9 >= scrollView.getChildAt(0).getMeasuredHeight())) {
            if (scrollView.getScrollY() != lastScrollY && lastScrollViewMesureHeight != scrollView.getChildAt(0).getMeasuredHeight()) {
                lastScrollY = scrollView.getScrollY();
                lastScrollViewMesureHeight = scrollView.getChildAt(0).getMeasuredHeight();
                //网络请求
                currentPage++;
                DataPaser.getSceneList(currentPage + "", null, 1 + "", distance + "", location[0] + "", location[1] + "", handler);
            }
        }
    }
}

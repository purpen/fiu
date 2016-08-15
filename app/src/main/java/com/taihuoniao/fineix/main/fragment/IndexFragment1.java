//package com.taihuoniao.fineix.main.fragment;
//
//import android.content.Intent;
//import android.graphics.PointF;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
//import com.taihuoniao.fineix.base.BaseFragment;
//import com.taihuoniao.fineix.beans.LoginInfo;
//import com.taihuoniao.fineix.beans.SceneList;
//import com.taihuoniao.fineix.main.MainActivity;
//import com.taihuoniao.fineix.main.MainApplication;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.network.DataConstants;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
//import com.taihuoniao.fineix.qingjingOrSceneDetails.SubsCJListActivity;
//import com.taihuoniao.fineix.scene.SearchActivity;
//import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.WaittingDialog;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
//import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnTouchListener {
//    //    public static IndexFragment instance;
//    private View fragment_view;
//    private RelativeLayout titlelayout;
//    private ImageView searchImg;
//    private ImageView subsImg;
//        private PullToRefreshListView pullToRefreshLayout;
//    private ListView listView;
//    private ProgressBar progressBar;
//    private List<SceneList.DataBean.RowsBean> sceneList;
//    private SceneListViewAdapter sceneListViewAdapter;
//    //场景列表当前页码
//    private int currentPage = 1;
//    //网络请求对话框
//    private WaittingDialog dialog;
//    private int lastFirstItem = -1;
//    private int lastTotalItem = -1;
//
//    @Override
//    protected void requestNet() {
//        ClientDiscoverAPI.getSceneList(currentPage + "", 8 + "", null, 2 + "", 1 + "", null, null, null, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                SceneList sceneL = new SceneList();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<SceneList>(){}.getType();
//                    sceneL = gson.fromJson(responseInfo.result,type);
//                }catch (JsonSyntaxException e){
//                    Log.e("<<<","情景列表解析异常");
//                }
//                pullToRefreshLayout.onRefreshComplete();
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                if (sceneL.isSuccess()) {
//                    pullToRefreshLayout.setLoadingTime();
//                    if (currentPage == 1) {
//                        sceneList.clear();
//                        lastFirstItem = -1;
//                        lastTotalItem = -1;
//                        pullToRefreshLayout.lastTotalItem = -1;
//                        pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
//                    }
//                    sceneList.addAll(sceneL.getData().getRows());
////                    if (currentPage == 1 && sceneList.size() > 0) {
////                        sceneList.get(0).setStartAnim(true);
////                    }
//                    sceneListViewAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                progressBar.setVisibility(View.GONE);
//                pullToRefreshLayout.onRefreshComplete();
////                swipeRefreshLayout.setRefreshing(false);
//                ToastUtils.showError("网络错误");
////                    dialog.showErrorWithStatus("网络错误");
////                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//
//    @Override
//    protected void initList() {
//        searchImg.setOnClickListener(this);
//        subsImg.setOnClickListener(this);
//        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                currentPage = 1;
//                if (!dialog.isShowing()) {
//                    dialog.show();
//                }
//                requestNet();
//            }
//        });
//
//        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
//            @Override
//            public void onLastItemVisible() {
//                progressBar.setVisibility(View.VISIBLE);
//                currentPage++;
//                requestNet();
//            }
//        });
//        sceneList = new ArrayList<>();
//        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null, null, null);
//        listView.setAdapter(sceneListViewAdapter);
//        listView.setOnItemClickListener(this);
////        listView.setOnTouchListener(this);
//        dialog.show();
//    }
//
//    private int firstItem = -1;
//    private int visibleItem = -1;
//    private int totalItem = -1;
//
//    @Override
//    protected View initView() {
//        fragment_view = View.inflate(getActivity(), R.layout.fragment_index, null);
//        titlelayout = (RelativeLayout) fragment_view.findViewById(R.id.title_layout);
//        searchImg = (ImageView) fragment_view.findViewById(R.id.search_img);
//        subsImg = (ImageView) fragment_view.findViewById(R.id.subs_img);
//        pullToRefreshLayout = (PullToRefreshListView) fragment_view.findViewById(R.id.pull_refresh_view);
//        pullToRefreshLayout.animLayout();
//        listView = pullToRefreshLayout.getRefreshableView();
//        progressBar = (ProgressBar) fragment_view.findViewById(R.id.progress_bar);
//        listView.setDividerHeight(0);
//        dialog = new WaittingDialog(getActivity());
////        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
////            @Override
////            public void onScrollStateChanged(AbsListView view, int scrollState) {
////                if (scrollState == SCROLL_STATE_IDLE && visibleItem > 0 && (firstItem + visibleItem >= totalItem)
////                        && lastFirstItem != firstItem && lastTotalItem != totalItem) {
////                    lastFirstItem = firstItem;
////                    lastTotalItem = totalItem;
//////                    swipeRefreshLayout.setRefreshing(true);
////                    progressBar.setVisibility(View.VISIBLE);
////                    currentPage++;
////                    requestNet();
////                }
////            }
////
////            @Override
////            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
////                firstItem = firstVisibleItem;
////                visibleItem = visibleItemCount;
////                totalItem = totalItemCount;
////            }
////        });
//        return fragment_view;
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
//        intent.putExtra("id", sceneList.get(position).get_id());
//        startActivity(intent);
////        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
////        intent.putExtra("position", position);
//////        private SerSceneListBean serSceneListBean;//封装场景数据的列表
//////        private int currentPage = 1;//场景列表的页码
//////        //来源..从哪个界面跳转过来的
////        SerSceneListBean serSceneListBean = new SerSceneListBean();
////        serSceneListBean.setSceneList(sceneList);
////        intent.putExtra("list", serSceneListBean);
////        intent.putExtra(IndexFragment.class.getSimpleName(), false);
////        intent.putExtra("page", currentPage);
////        startActivity(intent);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.subs_img:
////                startActivity(new Intent(getActivity(), ViewPagerActivity.class));
//                if (!LoginInfo.isUserLogin()) {
//                    MainApplication.which_activity = DataConstants.ElseActivity;
//                    startActivity(new Intent(getActivity(), OptRegisterLoginActivity.class));
//                    return;
//                }
//                startActivity(new Intent(getActivity(), SubsCJListActivity.class));
//                break;
//            case R.id.search_img:
//                Intent intent = new Intent(getActivity(), SearchActivity.class);
//                intent.putExtra("t", 9 + "");
//                startActivity(intent);
//                break;
//        }
//    }
//
//
//    public int getScrollY() {
//        View c = listView.getChildAt(0);
//        if (c == null) {
//            return 0;
//        }
//        int firstVisiblePosition = listView.getFirstVisiblePosition();
//        int top = c.getTop();
//        return -top + firstVisiblePosition * c.getHeight();
//    }
//
////    private boolean isMove = false;//判断手指在屏幕上是否移动过
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
////        if (listView == null || listView.getChildAt(0) == null || sceneList.size() <= 1) {
////            return false;
////        }
////        switch (event.getAction()) {
////            case MotionEvent.ACTION_DOWN:
////                startP = new PointF(event.getX(), event.getY());
//////                isMove = false;
//////                Log.e("<<<按下坐标", "x=" + startP.x + ",y=" + startP.y);
////                break;
////            case MotionEvent.ACTION_MOVE:
////                nowP = new PointF(event.getX(), event.getY());
//////                Log.e("<<<移动的坐标", "x=" + nowP.x + ",y=" + nowP.y);
//////                isMove = true;
////                break;
////            case MotionEvent.ACTION_UP:
////                if (nowP == null || startP == null) {
////                    return false;
////                }
//////                int i = getScrollY() / (listView.getChildAt(0).getHeight());
////                double move = Math.sqrt((nowP.x - startP.x) * (nowP.x - startP.x) + (nowP.y - startP.y) * (nowP.y - startP.y));
////                if (move < DensityUtils.dp2px(getActivity(), 12)) {
////                    return false;
////                }
////                //firstvisibleitem的偏移量
////                int s = getScrollY() % (listView.getChildAt(0).getHeight());
////                if (nowP.y < startP.y && s > 0.2 * listView.getChildAt(0).getHeight()) {
//////                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
////                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1,
////                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
////                    cancelChenjin();
////                    anim(listView.getFirstVisiblePosition() + 1);
////                } else if (nowP.y < startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
//////                    listView.smoothScrollByOffset(listView.getFirstVisiblePosition());
//////                    if (is16To9()) {
//////                        listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
//////                    } else {
////                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition(),
////                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
//////                    }
////                } else if (nowP.y > startP.y && s < 0.8 * listView.getChildAt(0).getHeight() && s > 0) {
//////                    if (is16To9()) {
//////                        listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
//////                    } else {
////                    anim(listView.getFirstVisiblePosition());
////                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition(),
////                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
//////                    }
////                    chenjin();
////
////                } else if (nowP.y > startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
//////                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
////                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1,
////                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
////                }
////                nowP = null;
////                startP = null;
////                break;
////        }
//        return false;
//    }
//
////    private void anim(int position) {
////        for (int i = 0; i < sceneList.size(); i++) {
////            if (i == position) {
////                sceneList.get(i).setStartAnim(true);
////            } else {
////                sceneList.get(i).setStartAnim(false);
////            }
////        }
//////        sceneList.get(listView.getFirstVisiblePosition()+1).setStartAnim(true);
////        sceneListViewAdapter.notifyDataSetChanged();
////    }
//
//    private void cancelChenjin() {
//        searchImg.setVisibility(View.GONE);
//        subsImg.setVisibility(View.GONE);
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("index", 1);
//        intent.setAction(DataConstants.BroadShopCart);
//        getActivity().sendBroadcast(intent);
//    }
//
//    private void chenjin() {
//        searchImg.setVisibility(View.VISIBLE);
//        subsImg.setVisibility(View.VISIBLE);
//        Intent intent = new Intent(getActivity(), MainActivity.class);
//        intent.putExtra("index", 2);
//        intent.setAction(DataConstants.BroadShopCart);
//        getActivity().sendBroadcast(intent);
//    }
//
//
//    private PointF startP, nowP;
//}

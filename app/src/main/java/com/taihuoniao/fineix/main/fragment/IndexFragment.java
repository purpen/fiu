package com.taihuoniao.fineix.main.fragment;

import android.content.Intent;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SceneListViewAdapter;
import com.taihuoniao.fineix.base.BaseFragment;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.main.MainActivity;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SceneDetailActivity;
import com.taihuoniao.fineix.qingjingOrSceneDetails.SubsCJListActivity;
import com.taihuoniao.fineix.scene.SearchActivity;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.DensityUtils;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshBase;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends BaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener, View.OnTouchListener {
    //    public static IndexFragment instance;
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
//        instance = IndexFragment.this;
//        DataPaser.getSceneList(currentPage + "", 8 + "", null, 2 + "", 1 + "", distance + "", null, null, handler);
        ClientDiscoverAPI.getSceneList(currentPage + "", 8 + "", null, 2 + "", 1 + "", distance + "", null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SceneList sceneL = new SceneList();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    sceneL.setSuccess(jsonObject.optBoolean("success"));
                    sceneL.setMessage(jsonObject.optString("message"));
//                    sceneList.setStatus(jsonObject.optString("status"));
                    if (sceneL.isSuccess()) {
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
                        sceneL.setSceneListBeanList(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pullToRefreshLayout.onRefreshComplete();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                if (sceneL.isSuccess()) {
                    pullToRefreshLayout.setLoadingTime();
                    if (currentPage == 1) {
                        sceneList.clear();
                        pullToRefreshLayout.lastTotalItem = -1;
                        pullToRefreshLayout.lastSavedFirstVisibleItem = -1;
                    }
                    sceneList.addAll(sceneL.getSceneListBeanList());
                    if (currentPage == 1 && sceneList.size() > 0) {
                        sceneList.get(0).setStartAnim(true);
                    }
                    sceneListViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                pullToRefreshLayout.onRefreshComplete();
                ToastUtils.showError("网络错误");
//                    dialog.showErrorWithStatus("网络错误");
//                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void initList() {
        searchImg.setOnClickListener(this);
        subsImg.setOnClickListener(this);
//        pullToRefreshLayout.setPullToRefreshEnabled(false);
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                requestNet();
            }
        });
        pullToRefreshLayout.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                progressBar.setVisibility(View.VISIBLE);
                currentPage++;
                requestNet();
            }
        });
        sceneList = new ArrayList<>();
        sceneListViewAdapter = new SceneListViewAdapter(getActivity(), sceneList, null, null, null);
        listView.setAdapter(sceneListViewAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnTouchListener(this);
        dialog.show();
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), SceneDetailActivity.class);
        intent.putExtra("id", sceneList.get(position).get_id());
        startActivity(intent);
//        Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
//        intent.putExtra("position", position);
////        private SerSceneListBean serSceneListBean;//封装场景数据的列表
////        private int currentPage = 1;//场景列表的页码
////        //来源..从哪个界面跳转过来的
//        SerSceneListBean serSceneListBean = new SerSceneListBean();
//        serSceneListBean.setSceneList(sceneList);
//        intent.putExtra("list", serSceneListBean);
//        intent.putExtra(IndexFragment.class.getSimpleName(), false);
//        intent.putExtra("page", currentPage);
//        startActivity(intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_index_subs:
//                startActivity(new Intent(getActivity(), ViewPagerActivity.class));
                if (!LoginInfo.isUserLogin()) {
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
        if (listView == null || listView.getChildAt(0) == null || sceneList.size() <= 1) {
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
                if (move < DensityUtils.dp2px(getActivity(), 12)) {
                    return false;
                }
                //firstvisibleitem的偏移量
                int s = getScrollY() % (listView.getChildAt(0).getHeight());
                if (nowP.y < startP.y && s > 0.2 * listView.getChildAt(0).getHeight()) {
//                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1,
                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
                    cancelChenjin();
                    anim(listView.getFirstVisiblePosition() + 1);
                } else if (nowP.y < startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
//                    listView.smoothScrollByOffset(listView.getFirstVisiblePosition());
//                    if (is16To9()) {
//                        listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
//                    } else {
                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition(),
                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
//                    }
                } else if (nowP.y > startP.y && s < 0.8 * listView.getChildAt(0).getHeight() && s > 0) {
//                    if (is16To9()) {
//                        listView.smoothScrollToPosition(listView.getFirstVisiblePosition());
//                    } else {
                    anim(listView.getFirstVisiblePosition());
                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition(),
                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
//                    }
                    chenjin();

                } else if (nowP.y > startP.y && move > DensityUtils.dp2px(getActivity(), 12)) {
//                    listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
                    listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1,
                            -MainApplication.getContext().getScreenWidth() * 16 / 9 + MainApplication.getContext().getScreenHeight(), 300);
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
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("index", 1);
        intent.setAction(DataConstants.BroadShopCart);
        getActivity().sendBroadcast(intent);
    }

    private void chenjin() {
        searchImg.setVisibility(View.VISIBLE);
        subsImg.setVisibility(View.VISIBLE);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra("index", 2);
        intent.setAction(DataConstants.BroadShopCart);
        getActivity().sendBroadcast(intent);
    }


    private PointF startP, nowP;
}

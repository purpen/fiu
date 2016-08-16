//package com.taihuoniao.fineix.qingjingOrSceneDetails;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//
//import com.google.gson.Gson;
//import com.google.gson.JsonSyntaxException;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.taihuoniao.fineix.R;
//import com.taihuoniao.fineix.adapters.VPAdapter;
//import com.taihuoniao.fineix.base.BaseActivity;
//import com.taihuoniao.fineix.beans.SceneList;
//import com.taihuoniao.fineix.beans.SerSceneListBean;
//import com.taihuoniao.fineix.main.fragment.IndexFragment;
//import com.taihuoniao.fineix.network.ClientDiscoverAPI;
//import com.taihuoniao.fineix.network.DataConstants;
//import com.taihuoniao.fineix.utils.ToastUtils;
//import com.taihuoniao.fineix.view.WaittingDialog;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//
///**
// * Created by taihuoniao on 2016/7/19.
// */
//public class ViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
//    //上个界面接收到数据
//    private int position;//第几条数据
//    private SerSceneListBean serSceneListBean;//封装场景数据的列表
//    private int currentPage = 1;//场景列表的页码
//    //来源..从哪个界面跳转过来的
//    @Bind(R.id.viewpager)
//    ViewPager viewpager;
//    private List<SceneList.DataBean.RowsBean> list;
//    private VPAdapter vpAdapter;
//    private WaittingDialog dialog;
//
//    public ViewPagerActivity() {
//        super(R.layout.activity_view_pager);
//    }
//
//    @Override
//    protected void initView() {
//        dialog = new WaittingDialog(this);
//        position = getIntent().getIntExtra("position", 0);
//        currentPage = getIntent().getIntExtra("page", 1);
//        serSceneListBean = (SerSceneListBean) getIntent().getSerializableExtra("list");
//        if (serSceneListBean == null) {
//            list = new ArrayList<>();
////            Log.e("<<<", "场景列表为空");
//        } else {
//            list = serSceneListBean.getSceneList();
////            Log.e("<<<", "场景列表不为空");
//        }
//        vpAdapter = new VPAdapter(getSupportFragmentManager(), this, list);
//        viewpager.setAdapter(vpAdapter);
//        viewpager.setCurrentItem(position, false);
//        viewpager.addOnPageChangeListener(this);
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DataConstants.BroadViewPager);
//        registerReceiver(sceneDetailReceiver, filter);
//    }
//
//    @Override
//    protected void onDestroy() {
//        unregisterReceiver(sceneDetailReceiver);
//        super.onDestroy();
//    }
//
//    //广播接收器
//    private BroadcastReceiver sceneDetailReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            vpAdapter.notifyDataSetChanged();
//        }
//    };
//
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        vpAdapter.getView(position).setTranslationX(positionOffsetPixels);
//    }
//
//    @Override
//    public void onPageSelected(int position) {
//        if (position == list.size() - 1) {
//            if (!dialog.isShowing()) {
//                dialog.show();
//            }
//            currentPage++;
//            getSceneList();
//        }
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }
//
//    private void getSceneList() {
//        if (getIntent().hasExtra(IndexFragment.class.getSimpleName())) {
//            sc(currentPage + "", 8 + "", null, 2 + "", 1 + "");
//        }
//    }
//
//    private void sc(String page, String size, String scene_id, String sort, String fine) {
//        ClientDiscoverAPI.getSceneList(page, size, scene_id, sort, fine, null, null, null, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                SceneList sceneL = new SceneList();
//                try {
//                    Gson gson = new Gson();
//                    Type type = new TypeToken<SceneList>() {
//                    }.getType();
//                    sceneL = gson.fromJson(responseInfo.result, type);
//                } catch (JsonSyntaxException e) {
//                    Log.e("<<<", "情景列表解析异常");
//                }
//                dialog.dismiss();
//                if (sceneL.isSuccess()) {
//                    if (currentPage == 1) {
//                        list.clear();
//                    }
//                    list.addAll(sceneL.getData().getRows());
//                    vpAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                dialog.dismiss();
//                ToastUtils.showError("网络错误");
//            }
//        });
//    }
//
//}

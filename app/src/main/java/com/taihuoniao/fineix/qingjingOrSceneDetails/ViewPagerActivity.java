package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.view.ViewPager;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.VPAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.SerSceneListBean;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/19.
 */
public class ViewPagerActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    //上个界面接收到数据
    private int position;//第几条数据
    private SerSceneListBean serSceneListBean;//封装场景数据的列表
    private int currentPage = 1;//场景列表的页码
    //来源..从哪个界面跳转过来的
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private List<SceneListBean> list;
    private VPAdapter vpAdapter;
    private WaittingDialog dialog;

    public ViewPagerActivity() {
        super(R.layout.activity_view_pager);
    }

    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        position = getIntent().getIntExtra("position", 0);
        currentPage = getIntent().getIntExtra("page", 1);
        serSceneListBean = (SerSceneListBean) getIntent().getSerializableExtra("list");
        if (serSceneListBean == null) {
            list = new ArrayList<>();
//            Log.e("<<<", "场景列表为空");
        } else {
            list = serSceneListBean.getSceneList();
//            Log.e("<<<", "场景列表不为空");
        }
        vpAdapter = new VPAdapter(getSupportFragmentManager(), this, list);
        viewpager.setAdapter(vpAdapter);
        viewpager.setCurrentItem(position, false);
        viewpager.addOnPageChangeListener(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(DataConstants.BroadViewPager);
        registerReceiver(sceneDetailReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(sceneDetailReceiver);
        super.onDestroy();
    }

    //广播接收器
    private BroadcastReceiver sceneDetailReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            vpAdapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        vpAdapter.getView(position).setTranslationX(positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        if (position == list.size() - 1) {
            if (!dialog.isShowing()) {
                dialog.show();
            }
            currentPage++;
            getSceneList();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getSceneList() {
        if (getIntent().hasExtra(IndexFragment.class.getSimpleName())) {
            sc(currentPage + "", 8 + "", null, 2 + "", 1 + "");
        }
    }

    private void sc(String page, String size, String scene_id, String sort, String fine) {
        ClientDiscoverAPI.getSceneList(page, size, scene_id, sort, fine, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                SceneList sceneL = new SceneList();
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.result);
                    sceneL.setSuccess(jsonObject.optBoolean("success"));
                    sceneL.setMessage(jsonObject.optString("message"));
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
                            user.is_expert = us.optInt("is_expert");
                            user.expert_info = us.optString("expert_info");
                            user.expert_label = us.optString("expert_label");
                            user.setUser_id(us.optString("user_id"));
                            user.setSummary(us.optString("summary"));
                            user.setNickname(us.optString("nickname"));
                            user.setLove_count(us.optString("love_count"));
                            user.setFollow_count(us.optString("follow_count"));
                            user.setFans_count(us.optString("fans_count"));
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
                dialog.dismiss();
                if (sceneL.isSuccess()) {
                    if (currentPage == 1) {
                        list.clear();
                    }
                    list.addAll(sceneL.getSceneListBeanList());
                    vpAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

}

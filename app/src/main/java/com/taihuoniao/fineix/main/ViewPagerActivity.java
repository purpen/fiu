package com.taihuoniao.fineix.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.SceneList;
import com.taihuoniao.fineix.beans.SceneListBean;
import com.taihuoniao.fineix.beans.SerSceneListBean;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/7/8.
 */
public class ViewPagerActivity extends BaseActivity {
    //上个界面传递过来的数据
    private SerSceneListBean serSceneListBean;
    private String position;
    private int page;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private WaittingDialog dialog;
    private List<SceneListBean> list;
    private VAdapter vAdapter;

    public ViewPagerActivity() {
        super(R.layout.aaaaaa);
    }


    @Override
    protected void initView() {
        dialog = new WaittingDialog(this);
        serSceneListBean = (SerSceneListBean) getIntent().getSerializableExtra("bean");
        position = getIntent().getStringExtra("position");
        page = getIntent().getIntExtra("page", 1);
        list = new ArrayList<>();
        if (serSceneListBean != null) {
            list.addAll(serSceneListBean.getSceneList());
        }

        vAdapter = new VAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(vAdapter);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("<<<pagescrolled", "position=" + position + ",offset=" + positionOffset + ",pixels=" + positionOffsetPixels);
//                vAdapter.getView(position).setTranslationX(positionOffsetPixels);
                ViewPagerFragment viewPagerFragment = (ViewPagerFragment) vAdapter.getItem(position);
                if (viewPagerFragment != null && viewPagerFragment.getMoveView() != null) {
                    viewPagerFragment.getMoveView().setTranslationX(positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
//                Log.e("<<<selected", "size=" + list.size() + ",position=" + position);
                if (list.size() > 0 && position == list.size() - 1) {
                    getNextPageData();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (position != null) {
            Log.e("<<<", "position=" + position);
            viewpager.setCurrentItem(Integer.parseInt(position));
        }
    }

    private void getNextPageData() {
        page++;
        dialog.show();
        if (getIntent().hasExtra(FindFragment.class.getSimpleName())) {
            getCJList(page + "", null, null, 0 + "", 0 + "");
        }
    }

    private void getCJList(String page, String size, String scene_id, String sort, String fine) {
        ClientDiscoverAPI.getSceneList(page, size, scene_id, sort, fine, null, null, null, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
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
                if (sceneList1.isSuccess()) {
//                    lastSceneSize = sceneList.size();
                    list.addAll(sceneList1.getSceneListBeanList());
//                        Toast.makeText(getActivity(), "测试，场景数据个数=" + sceneList.size(), Toast.LENGTH_SHORT).show();
                    vAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showError(sceneList1.getMessage());
//                        dialog.showErrorWithStatus(netSceneList.getMessage());
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    static class VAdapter extends FragmentStatePagerAdapter {
        private List<SceneListBean> list;

        public VAdapter(FragmentManager fm, List<SceneListBean> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }


//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == object;
//        }

        @Override
        public Fragment getItem(int position) {
            return ViewPagerFragment.newInstance(list.get(position).get_id());
        }

//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            if (list.get(position).getParent() == null) {
//                container.addView(list.get(position));
//            }
//            return list.get(position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(list.get(position));
////            super.destroyItem(container, position, object);
//        }
    }

}

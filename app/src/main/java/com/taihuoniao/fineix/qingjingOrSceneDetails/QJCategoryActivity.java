package com.taihuoniao.fineix.qingjingOrSceneDetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SearchViewPagerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.NetBean;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.UserInfo;
import com.taihuoniao.fineix.main.MainApplication;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.DataConstants;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.QJFragment;
import com.taihuoniao.fineix.qingjingOrSceneDetails.fragment.SearchFragment;
import com.taihuoniao.fineix.user.OptRegisterLoginActivity;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/8/26.
 */
public class QJCategoryActivity extends BaseActivity implements View.OnClickListener {
    //上个界面传递过来的情景分类id和name
    private String id;
    private String name;
    @Bind(R.id.title_back)
    ImageView titleBack;
    @Bind(R.id.title_name)
    TextView titleName;
    @Bind(R.id.subs_btn)
    Button subsBtn;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    private int isSubs;//判断是否已经订阅 0,请求失败 1，没有订阅 2，已订阅
    private WaittingDialog dialog;
    private List<SearchFragment> fragmentList;
    private List<String> titleList;
    private SearchViewPagerAdapter searchViewPagerAdapter;

    @Override
    protected void getIntentData() {
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        if (id == null) {
            ToastUtils.showError("情境分类不存在或已删除");
            finish();
        }
    }

    public QJCategoryActivity() {
        super(R.layout.activity_qj_category);
    }

    @Override
    protected void initView() {
        titleBack.setOnClickListener(this);
        titleName.setText(name);
        subsBtn.setOnClickListener(this);
        dialog = new WaittingDialog(this);
        IntentFilter intentFilter = new IntentFilter(DataConstants.BroadQJCategory);
        registerReceiver(qjcategoryReceiver, intentFilter);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void initList() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
        fragmentList.add(QJFragment.newInstance(0, id));
        titleList.add("精选");
        fragmentList.add(QJFragment.newInstance(1, id));
        titleList.add("最新");
        //设置适配器
        searchViewPagerAdapter = new SearchViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        viewPager.setAdapter(searchViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(fragmentList.size());
    }

    @Override
    protected void requestNet() {
        hasSubsCount();
    }

    //获取订阅情境主题个数
    private void hasSubsCount() {
        ClientDiscoverAPI.getUserCenterData(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                Log.e("<<<个人信息", responseInfo.result);
                UserInfo userInfo = new UserInfo();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<UserInfo>() {
                    }.getType();
                    userInfo = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<个人信息", "解析异常=" + e.toString());
                }
                if (userInfo.isSuccess()) {
                    if (userInfo.getData().getInterest_scene_cate().contains(id)) {
                        isSubs = 2;
                        subsBtn.setBackgroundResource(R.mipmap.has_subs_bbtn);
                        return;
                    }
                    isSubs = 1;
                    subsBtn.setBackgroundResource(R.mipmap.subs_btn);
                    return;
                }
                isSubs = 0;
                ToastUtils.showError(userInfo.getMessage());
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                isSubs = 0;
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.subs_btn:
                if (!LoginInfo.isUserLogin()) {
                    MainApplication.which_activity = DataConstants.QJCategoryActivity;
                    startActivity(new Intent(this, OptRegisterLoginActivity.class));
                    return;
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
                switch (isSubs) {
                    case 1:
                        subs();
                        break;
                    case 2:
                        cancelSubs();
                        break;
                    default:
                        requestNet();
                        break;
                }
                break;
        }
    }

    //取消订阅分类
    private void cancelSubs() {
        ClientDiscoverAPI.cancelSubscribe(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<取消订阅",responseInfo.result);
                dialog.dismiss();
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<订阅情景分类", "解析异常=" + e.toString());
                }
                if (netBean.isSuccess()) {
                    isSubs = 1;
                    subsBtn.setBackgroundResource(R.mipmap.subs_btn);
                    return;
                }
                ToastUtils.showError(netBean.getMessage());
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    //订阅分类
    private void subs() {
        ClientDiscoverAPI.subscribe(id, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Log.e("<<<订阅",responseInfo.result);
                dialog.dismiss();
                NetBean netBean = new NetBean();
                try {
                    Gson gson = new Gson();
                    Type type = new TypeToken<NetBean>() {
                    }.getType();
                    netBean = gson.fromJson(responseInfo.result, type);
                } catch (JsonSyntaxException e) {
                    Log.e("<<<订阅情景分类", "解析异常=" + e.toString());
                }
                if (netBean.isSuccess()) {
                    isSubs = 2;
                    subsBtn.setBackgroundResource(R.mipmap.has_subs_bbtn);
                    return;
                }
                ToastUtils.showError(netBean.getMessage());
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                dialog.dismiss();
                ToastUtils.showError(R.string.net_fail);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(qjcategoryReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver qjcategoryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            requestNet();
            for (SearchFragment searchFragment : fragmentList) {
                searchFragment.refreshData("1");
            }
        }
    };
}
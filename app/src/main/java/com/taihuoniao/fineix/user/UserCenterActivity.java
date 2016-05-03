package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.main.fragment.FindFragment;
import com.taihuoniao.fineix.main.fragment.IndexFragment;
import com.taihuoniao.fineix.main.fragment.MineFragment;
import com.taihuoniao.fineix.main.fragment.WellGoodsFragment;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.user.fragments.UserCJFragment;
import com.taihuoniao.fineix.user.fragments.UserFansFragment;
import com.taihuoniao.fineix.user.fragments.UserFocusFragment;
import com.taihuoniao.fineix.user.fragments.UserQJFragment;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity{
//    @Bind(R.id.fl_box)
//    FrameLayout fl_box;
    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.riv)
    RoundedImageView riv;
    @Bind(R.id.tv_real)
    TextView tv_real;
    @Bind(R.id.tv_nick)
    TextView tv_nick;
    @Bind(R.id.tv_rank)
    TextView tv_rank;
    private User user;
//    @Bind(R.id.ll_qj)
//    LinearLayout ll_qj;
//
//    @Bind(R.id.ll_cj)
//    LinearLayout ll_cj;
//
//    @Bind(R.id.ll_focus)
//    LinearLayout ll_focus;
//
//    @Bind(R.id.ll_fans)
//    LinearLayout ll_fans;
    private FragmentManager fm;
    private ArrayList<Fragment> fragments;
    private Fragment showFragment;
    public UserCenterActivity(){
        super(R.layout.activity_user_center);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (fragments==null){
            fragments = new ArrayList<>();
        }
        fm = getSupportFragmentManager();
        if (savedInstanceState != null) {
            recoverAllState(savedInstanceState);
        } else {
            switchFragmentandImg(UserQJFragment.class);
        }

    }



    @Override
    protected void initView() {

    }

    private void recoverAllState(Bundle savedInstanceState) {
        Fragment userQJFragment = fm.getFragment(savedInstanceState,UserQJFragment.class.getSimpleName());
        addFragment2List(userQJFragment);
        Fragment userCJFragment = fm.getFragment(savedInstanceState, UserCJFragment.class.getSimpleName());
        addFragment2List(userCJFragment);
        Fragment userfocusFragment = fm.getFragment(savedInstanceState, UserFocusFragment.class.getSimpleName());
        addFragment2List(userfocusFragment);
        Fragment userFansFragment = fm.getFragment(savedInstanceState, UserFansFragment.class.getSimpleName());
        addFragment2List(userFansFragment);
        Class clazz = (Class) savedInstanceState.getSerializable(getClass().getSimpleName());
        if (clazz == null) return;
        LogUtil.e(TAG, clazz.getSimpleName() + "///////////");
        switchFragmentandImg(clazz);
    }


    private void addFragment2List(Fragment fragment) {
        if (fragment == null) {
            LogUtil.e(TAG,"addedFragment==null");
            return;
        }

        if (fragments.contains(fragment)) {
            LogUtil.e(TAG,"addedFragment  contains");
            return;
        }

        fragments.add(fragment);
    }

    private void switchFragmentandImg(Class clazz) {
        resetUI();
        try {
            switchFragment(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换fragment
     *
     * @param clazz
     * @throws Exception
     */
    private void switchFragment(Class clazz) throws Exception {
        LogUtil.e(TAG, "<<<<<<<<"+clazz.getSimpleName());
        Fragment fragment = fm.findFragmentByTag(clazz.getSimpleName());
        if (fragment == null) {
            fragment = (Fragment) clazz.newInstance();
            fm.beginTransaction().add(R.id.fl_box, fragment, clazz.getSimpleName()).commit();
        } else {
            fm.beginTransaction().show(fragment).commit();
        }
        addFragment2List(fragment);
        showFragment = fragment;
        LogUtil.e(TAG, fragments.size()+"<<<<<<");
    }

    private void resetUI() {
        if (fragments == null) {
            return;
        }
        if (fragments.size() == 0) {
            return;
        }

        for (Fragment fragment : fragments) {
            fm.beginTransaction().hide(fragment).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LogUtil.e(TAG,"onSaveInstanceState");
        int size=fragments.size();
        if (fragments!=null && size>0){
            for (int i=0;i<size;i++){
                fm.putFragment(outState,fragments.get(i).getTag(),fragments.get(i));
            }
        }
        if (showFragment != null) {
            outState.putSerializable(getClass().getSimpleName(), showFragment.getClass());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getMineInfo(new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LogUtil.e("result", responseInfo.result);
                if (responseInfo == null) {
                    return;
                }
                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                try{
                    user = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<User>>() {
                    });
                }catch (JsonSyntaxException e){
                    LogUtil.e(TAG,e.getLocalizedMessage());
                    Util.makeToast("对不起,数据异常");
                }
                refreshUI();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                LogUtil.e(TAG,s);
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (user==null){
            return;
        }
        tv_title.setText(user.nickname);
        ImageLoader.getInstance().displayImage(user.medium_avatar_url,riv);
        if (TextUtils.isEmpty(user.realname)){
            tv_real.setVisibility(View.GONE);
        }else {
            tv_real.setText(user.realname);
        }

        if (TextUtils.isEmpty(user.nickname)){
            tv_nick.setVisibility(View.GONE);
        }else {
            tv_nick.setText(user.nickname);
        }

        if (TextUtils.isEmpty(user.rank_title)){
            tv_rank.setVisibility(View.GONE);
        }else {
            tv_rank.setText(user.rank_title);
        }
    }
    @OnClick({R.id.ll_qj,R.id.ll_cj,R.id.ll_focus,R.id.ll_fans,R.id.iv_right,R.id.iv_detail,R.id.bt_focus,R.id.bt_msg})
    void onClick(View v){
        switch (v.getId()){
            case R.id.iv_detail:
                finish();
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity, EditUserInfoActivity.class));
                break;
            case R.id.bt_focus://TODO 换成对应用户的Id
                ClientDiscoverAPI.focusOperate(LoginInfo.getUserId() + "", new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        if (responseInfo == null) return;
                        if (TextUtils.isEmpty(responseInfo.result)) return;

                        HttpResponse response = JsonUtil.fromJson(responseInfo.result, HttpResponse.class);
                        if (response.isSuccess()) {
                            Util.makeToast(response.getMessage());
                            return;
                        }

                        Util.makeToast(response.getMessage());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Util.makeToast(s);
                    }
                });
                break;
            case R.id.bt_msg:
                Util.makeToast("私信操作");
                break;
            case R.id.ll_qj:
                switchFragmentandImg(UserQJFragment.class);
                break;
            case R.id.ll_cj:
                switchFragmentandImg(UserCJFragment.class);
                break;
            case R.id.ll_focus:
                switchFragmentandImg(UserFocusFragment.class);
                break;
            case R.id.ll_fans:
                switchFragmentandImg(UserFansFragment.class);
                break;
        }
    }


}

package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.User;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.roundImageView.RoundedImageView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 * created at 2016/4/26 17:43
 */
public class UserCenterActivity extends BaseActivity{
    @Bind(R.id.fl_box)
    FrameLayout fl_box;
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
    public UserCenterActivity(){
        super(R.layout.activity_user_center);
    }

    @Override
    protected void initView() {
        
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
                Util.makeToast("对不起,网络请求失败");
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

    @OnClick({R.id.iv_right,R.id.iv_detail})
    void onClick(View v){
        switch (v.getId()){
            case R.id.iv_detail:
                finish();
                break;
            case R.id.iv_right:
                startActivity(new Intent(activity,EditUserInfoActivity.class));
                break;
        }
    }
}

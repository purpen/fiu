package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FocusFansAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FocusFansData;
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.view.CustomHeadView;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/22 17:05
 */
public class FocusFansActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    private int curPage = 1;
    private static final String PAGE_SIZE = "100";  //分页大小
    public static final String FOCUS_TYPE = "1";  //关注列表
    public static final String FANS_TYPE = "2";  //粉丝列表
    private ArrayList<FocusFansItem> list;
    private FocusFansAdapter adapter;
    private String pageType=FOCUS_TYPE;
    private static final String USER_ID_EXTRA="USER_ID_EXTRA";
    private String userId= LoginInfo.getUserId()+"";
    public FocusFansActivity() {
        super(R.layout.activity_focus_fans);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(getClass().getSimpleName())){
            pageType=intent.getStringExtra(getClass().getSimpleName());
        }

        if (intent.hasExtra(USER_ID_EXTRA)){
            userId=intent.getStringExtra(USER_ID_EXTRA);
        }

    }

    @Override
    protected void initView() {
        if (TextUtils.equals(FOCUS_TYPE,pageType)){
            custom_head.setHeadCenterTxtShow(true, "关注");
        }else {
            custom_head.setHeadCenterTxtShow(true, "粉丝");
        }
    }

    @Override
    protected void requestNet() {
//        userId="10";
        LogUtil.e(TAG,userId);
        ClientDiscoverAPI.getFocusFansList(userId,String.valueOf(curPage), PAGE_SIZE,pageType, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                FocusFansData data = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<FocusFansData>>() {
                });

                if (data == null) {
                    return;
                }

                list=data.rows;
                refreshUI();
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

//        ClientDiscoverAPI.focusOperate("924810",new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                LogUtil.e("onSuccess",responseInfo.result);
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//
//            }
//        });
    }

    @Override
    protected void refreshUI() {
        if (list == null) {
            return;
        }
        if (list.size()==0){
            return;
        }

        if (adapter==null){
            adapter=new FocusFansAdapter(list,activity,pageType);
            lv.setAdapter(adapter);
        }else {
            adapter.notifyDataSetChanged();
        }
    }
}

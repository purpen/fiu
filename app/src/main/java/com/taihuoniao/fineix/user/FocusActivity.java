package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FocusAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.FocusFansData;
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/22 17:05
 */
public class FocusActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    ListView lv;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.ll_tips)
    LinearLayout ll_tips;
    private int curPage = 1;
    private static final String PAGE_SIZE = "9999";  //分页大小
    public static final String FOCUS_TYPE = "1";  //关注列表
    private ArrayList<FocusFansItem> list;
    private FocusAdapter adapter;
    public static final String USER_ID_EXTRA = "USER_ID_EXTRA";
    private long userId = LoginInfo.getUserId();
    private WaittingDialog dialog;

    public FocusActivity() {
        super(R.layout.activity_focus_fans);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();

        if (intent.hasExtra(USER_ID_EXTRA)) {
            userId = intent.getLongExtra(USER_ID_EXTRA, -1);
        }

    }

    @Override
    protected void onResume() {
        if (LoginInfo.getUserId() == userId) {
            requestNet();
        }
        super.onResume();
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "关注");
        dialog = new WaittingDialog(this);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FocusFansItem focusFansItem = list.get(i);
                Intent intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(USER_ID_EXTRA, focusFansItem.follows.user_id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void requestNet() {
        LogUtil.e(TAG, "requestNet==" + userId);
        dialog.show();
        ClientDiscoverAPI.getFocusFansList(userId + "", String.valueOf(curPage), PAGE_SIZE, FOCUS_TYPE, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) {
                    return;
                }

                if (TextUtils.isEmpty(responseInfo.result)) {
                    return;
                }

                LogUtil.e(TAG, responseInfo.result);
                FocusFansData data = JsonUtil.fromJson(responseInfo.result, new TypeToken<HttpResponse<FocusFansData>>() {
                });

                if (data == null) {
                    return;
                }

                list = data.rows;
                refreshUI();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI() {

        if (list == null) {
            return;
        }
        if (list.size() == 0) {
            ll_tips.setVisibility(View.VISIBLE);
            tv_tips.setText(R.string.focus_tips);
            return;
        }

        if (adapter == null) {
            adapter = new FocusAdapter(list, activity,userId);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

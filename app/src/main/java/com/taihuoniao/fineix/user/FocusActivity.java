package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.FocusAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.FocusFansData;
import com.taihuoniao.fineix.beans.FocusFansItem;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/4/22 17:05
 */
public class FocusActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    PullToRefreshListView lv;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.ll_tips)
    LinearLayout ll_tips;
    private int curPage =1;
    private static final String PAGE_SIZE = "15";  //分页大小
    public static final String FOCUS_TYPE = "1";  //关注列表
    private ArrayList<FocusFansItem> list;
    private FocusAdapter adapter;
    public static final String USER_ID_EXTRA = "USER_ID_EXTRA";
    private long userId = LoginInfo.getUserId();
    private WaittingDialog dialog;
    private boolean isFirstLoad = true;
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
    protected void initView() {
        list = new ArrayList<>();
        custom_head.setHeadCenterTxtShow(true, "关注");
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        adapter = new FocusAdapter(list,activity,userId);
        lv.setAdapter(adapter);
    }

    @Override
    protected void installListener() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i<1) return;
                FocusFansItem focusFansItem = list.get(i-1);
                if (focusFansItem.follows == null) return;
                Intent intent = new Intent(activity, UserCenterActivity.class);
                intent.putExtra(USER_ID_EXTRA, focusFansItem.follows.user_id);
                startActivity(intent);
            }
        });

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                list.clear();
                curPage=1;
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                requestNet();
            }
        });
    }

    @Override
    protected void requestNet() {
        HashMap<String, String> params = ClientDiscoverAPI.getFocusFansListRequestParams(userId + "", String.valueOf(curPage),PAGE_SIZE, FOCUS_TYPE, null);
        HttpRequest.post(params, URL.FOCUS_FAVORITE_URL, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (isFirstLoad&&!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
            public void onSuccess(String json) {
                lv.onRefreshComplete();
                isFirstLoad = false;
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                HttpResponse<FocusFansData> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<FocusFansData>>() {
                });
                if (response.isSuccess()){
                   list.addAll(response.getData().rows);
                    curPage++;
                }
                refreshUI();
            }

            @Override
            public void onFailure(String error) {
                if (!activity.isFinishing() && dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI() {
        if (null==list)  return;
        if (list.size() == 0) {
            ll_tips.setVisibility(View.VISIBLE);
            if (LoginInfo.getUserId() == userId) {
                tv_tips.setText(R.string.focus_tips);
            } else {
                tv_tips.setText(R.string.focus_tips1);
            }
            return;
        }

        if (adapter == null) {
            adapter = new FocusAdapter(list, activity, userId);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

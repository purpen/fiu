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
import com.taihuoniao.fineix.adapters.FansAdapter;
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
public class FansActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.lv)
    PullToRefreshListView lv;
    @Bind(R.id.tv_tips)
    TextView tv_tips;
    @Bind(R.id.ll_tips)
    LinearLayout ll_tips;
    private static final String PAGE_SIZE = "15";  //分页大小
    public static final String FANS_TYPE = "2";  //粉丝列表
    private ArrayList<FocusFansItem> list;
    private FansAdapter adapter;
    public static final String USER_ID_EXTRA = "USER_ID_EXTRA";
    private long userId = LoginInfo.getUserId();
    private WaittingDialog dialog;
    private boolean flag;//判断是不是从消息页面跳转过来的
    private int fansCount;//新添加的粉丝数量
    private int curPage = 1;
    private boolean isFirstLoad = true;
    public FansActivity() {
        super(R.layout.activity_focus_fans);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(USER_ID_EXTRA)) {
            userId = intent.getLongExtra(USER_ID_EXTRA, -1);
        }
        if (intent.hasExtra(MessageActivity.class.getSimpleName())) {
            flag = true;
            fansCount = intent.getIntExtra(MessageActivity.class.getSimpleName(), 0);
        } else {
            flag = false;
        }
    }

//    @Override
//    protected void onResume() {
//        if (LoginInfo.getUserId() == userId) {
//            requestNet();
//        }
//        super.onResume();
//    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "粉丝");
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
        list= new ArrayList<>();
        lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
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
        HashMap<String, String> params = ClientDiscoverAPI.getFocusFansListRequestParams(userId + "", String.valueOf(curPage), PAGE_SIZE, FANS_TYPE,
                flag ? "1" : null);
        HttpRequest.post(params, URL.FOCUS_FAVORITE_URL, new GlobalDataCallBack(){
            @Override
            public void onStart() {
                if (isFirstLoad&&!activity.isFinishing() && dialog != null) dialog.show();
            }

            @Override
                    public void onSuccess(String json) {
                        isFirstLoad =false;
                        lv.onRefreshComplete();
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
        if (list.size() == 0) {
            ll_tips.setVisibility(View.VISIBLE);
            if (LoginInfo.getUserId() == userId) {
                tv_tips.setText(R.string.fans_tips);
            } else {
                tv_tips.setText(R.string.fans_tips1);
            }
            return;
        }

        if (adapter == null) {
            adapter = new FansAdapter(list, activity, userId, flag, fansCount);
            lv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
}

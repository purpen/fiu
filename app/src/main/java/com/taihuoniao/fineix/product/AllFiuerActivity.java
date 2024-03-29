package com.taihuoniao.fineix.product;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.AllFiuerAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.common.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.FiuUserListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.user.FocusActivity;
import com.taihuoniao.fineix.user.UserCenterActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.GlobalTitleLayout;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;
import com.taihuoniao.fineix.view.pulltorefresh.PullToRefreshListView;

import java.util.HashMap;

import butterknife.Bind;
import okhttp3.Call;

/**
 * Created by taihuoniao on 2016/7/7.
 */
public class AllFiuerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.titlelayout)
    GlobalTitleLayout titlelayout;
    @Bind(R.id.searchlinear)
    LinearLayout searchLinear;
    @Bind(R.id.pull_refresh_view)
    PullToRefreshListView pullRefreshView;
    private ListView listView;
    private WaittingDialog dialog;
    private Call userHandler;

    public AllFiuerActivity() {
        super(R.layout.activity_all_fiuer);
    }

    @Override
    protected void initView() {
        listView = pullRefreshView.getRefreshableView();
        dialog = new WaittingDialog(this);
        titlelayout.setTitle("排行榜");
        titlelayout.setContinueTvVisible(false);
        searchLinear.setVisibility(View.GONE);
        searchLinear.setOnClickListener(this);
        pullRefreshView.setPullToRefreshEnabled(false);
        listView.setOnItemClickListener(this);
        listView.setDividerHeight(0);
        WindowUtils.chenjin(this);
    }

    @Override
    protected void requestNet() {
        if (!dialog.isShowing()) {
            dialog.show();
        }
        HashMap<String, String> params = ClientDiscoverAPI.getfiuUserListRequestParams(1 + "", 100 + "", 1 + "");
        userHandler = HttpRequest.post(params,  URL.FIU_USER_LIST, new GlobalDataCallBack(){
            @Override
            public void onSuccess(String json) {
                HttpResponse<FiuUserListBean> netUsers = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<FiuUserListBean>>() { });
                dialog.dismiss();
                if (netUsers.isSuccess()) {
                    AllFiuerAdapter allFiuerAdapter = new AllFiuerAdapter(AllFiuerActivity.this, netUsers.getData());
                    listView.setAdapter(allFiuerAdapter);
                } else {
                    ToastUtils.showError(netUsers.getMessage());
                }
            }

            @Override
            public void onFailure(String error) {
                dialog.dismiss();
                ToastUtils.showError("网络错误");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchlinear:

                break;
        }
    }

    private FiuUserListBean netUsers;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UserCenterActivity.class);
        long _id = netUsers.getData().getRows().get(position).get_id();
        intent.putExtra(FocusActivity.USER_ID_EXTRA, _id);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        if (userHandler != null) {
            userHandler.cancel();
        }
        super.onDestroy();
    }
}

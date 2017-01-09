package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.http.RequestParams;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderedQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.base.GlobalDataCallBack;
import com.taihuoniao.fineix.base.HttpRequest;
import com.taihuoniao.fineix.beans.DataSubscribedQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.beans.ItemSubscribedQJ;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.URL;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.LogUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.WindowUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.dialog.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * @author lilin
 *         created at 2016/5/5 18:12
 */
public class OrderQJActivity extends BaseActivity {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pull_gv;
    @Bind(R.id.tv_subscribe)
    TextView tvSubscribe;
    private int curPage = 1;
    private boolean isLoadMore = false;
    private List<ItemSubscribedQJ> mList = new ArrayList<>();
    private WaittingDialog dialog;
    private OrderedQJAdapter adapter;
    private ArrayList<String> subscribedIds;
    private static final int REQUEST_THEME_NUM = 100;

    public OrderQJActivity() {
        super(R.layout.activity_order_qj);
    }

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        if (intent.hasExtra(TAG)) {
            subscribedIds = intent.getStringArrayListExtra(TAG);
        }
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.subscribe);
        dialog = new WaittingDialog(this);
        WindowUtils.chenjin(this);
        pull_gv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    protected void installListener() {
        pull_gv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                curPage = 1;
                isLoadMore = true;
                mList.clear();
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            }

        });
        pull_gv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                requestNet();
            }
        });

//        pull_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(activity, QJDetailActivity.class);
//                intent.putExtra("id", mList.get(i)._id);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        curPage=1;
        isLoadMore=false;
        mList.clear();
        requestNet();
    }

    @Override
    protected void requestNet() {
        if (subscribedIds == null) return;
        tvSubscribe.setText(String.format("已订阅%s个情境主题", subscribedIds.size()));
        if (subscribedIds.size() == 0) return;
        StringBuilder builder = new StringBuilder();
        for (String id : subscribedIds) {
            builder.append(id).append(",");
        }
        if (TextUtils.isEmpty(builder)) return;
        RequestParams params = ClientDiscoverAPI.getQJListRequestParams(String.valueOf(curPage), builder.deleteCharAt(builder.length() - 1).toString());
        HttpRequest.post(params, URL.SCENE_LIST, new GlobalDataCallBack(){
//        ClientDiscoverAPI.getQJList(String.valueOf(curPage), builder.deleteCharAt(builder.length() - 1).toString(), new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(String json) {
                if (dialog != null) dialog.dismiss();
                pull_gv.onRefreshComplete();
                if (TextUtils.isEmpty(json)) return;
                HttpResponse<DataSubscribedQJ> response = JsonUtil.json2Bean(json, new TypeToken<HttpResponse<DataSubscribedQJ>>() {
                });
                if (response.isSuccess()) {
                    List list = response.getData().rows;
                    refreshUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(String error) {
                if (dialog != null) dialog.dismiss();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }

    @Override
    protected void refreshUI(List list) {
        if (list == null) return;
        if (list.size() == 0) {
            return;
        }
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new OrderedQJAdapter(mList, activity);
            pull_gv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }


    @OnClick(R.id.tv_subscribe)
    public void onClick() {
        Intent intent = new Intent(activity, SubscribeThemeActivity.class);
        intent.putStringArrayListExtra(SubscribeThemeActivity.class.getSimpleName(), subscribedIds);
        startActivityForResult(intent, REQUEST_THEME_NUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        switch (requestCode) {
            case REQUEST_THEME_NUM:
                if (data == null) return;
                if (subscribedIds == null) return;
                if (data.hasExtra(TAG)) {
                    subscribedIds.clear();
                    subscribedIds.addAll(data.getStringArrayListExtra(TAG));
                }
                LogUtil.e(TAG, subscribedIds.size() + "");
                break;
        }
    }
}

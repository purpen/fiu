package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.SupportQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.DataSupportQJ;
import com.taihuoniao.fineix.beans.HttpResponse;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by taihuoniao on 2016/4/30.
 * 赞过的情境
 */
public class HasLoveActivity extends BaseActivity {
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pullGv;
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    private WaittingDialog dialog;
    private boolean isLoadMore = false;
    private ArrayList<DataSupportQJ.ItemSupportQJ> mList = new ArrayList<>();
    private int curPage = 1;
    private SupportQJAdapter adapter;

    public HasLoveActivity() {
        super(R.layout.activity_has_love);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, R.string.has_love);
        dialog = new WaittingDialog(HasLoveActivity.this);
        mList = new ArrayList<>();
        pullGv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }


    @Override
    protected void installListener() {
        pullGv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                isLoadMore = true;
                resetData();
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {

            }
        });

        pullGv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                requestNet();
            }
        });

//        pullGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ItemSubscribedQJ item = (ItemSubscribedQJ) pullGv.getRefreshableView().getAdapter().getItem(position);
//                Intent intent = new Intent(HasLoveActivity.this, QJDetailActivity.class);
//                intent.putExtra("id", item._id);
//                startActivity(intent);
//            }
//        });
    }


    private void resetData() {
        curPage = 1;
        mList.clear();
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getSupportQJ(String.valueOf(curPage), "12", "2", new RequestCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                if (!isLoadMore && dialog != null) {
                    dialog.show();
                }
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                if (dialog != null) dialog.dismiss();
                pullGv.onRefreshComplete();
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<DataSupportQJ> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<DataSupportQJ>>() {
                });
                if (response.isSuccess()) {
                    List list = response.getData().rows;
                    refreshUI(list);
                    return;
                }
                ToastUtils.showError(response.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                if (dialog != null) dialog.dismiss();
                e.printStackTrace();
                ToastUtils.showError(R.string.network_err);
            }
        });
    }



    @Override
    protected void refreshUI(List list) {
        if (list == null) return;
        if (list.size() == 0) {
            if (mList.size() > 0) {
                ToastUtils.showInfo("没有更多数据哦");
            } else {
                ToastUtils.showInfo("您还没有订阅的情境");
            }
            return;
        }
        curPage++;
        mList.addAll(list);
        if (adapter == null) {
            adapter = new SupportQJAdapter(mList, activity);
            pullGv.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }
}

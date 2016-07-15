package com.taihuoniao.fineix.user;

import android.text.TextUtils;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.CollectListAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.CollectionData;
import com.taihuoniao.fineix.beans.CollectionItem;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.network.HttpResponse;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.ToastUtils;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 *         created at 2016/6/21 15:43
 */
public class CollectionsActivity extends BaseActivity<CollectionItem> {
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_lv)
    PullToRefreshListView pull_lv;
    private int curPage = 1;
    public static final String PAGE_SIZE = "10";
    public static final String PAGE_TYPE = "10";
    public static final String PAGE_EVENT = "1";
    private List<CollectionItem> mList = new ArrayList<>();
    private WaittingDialog dialog;
    private CollectListAdapter adapter;
    private boolean isLoadMore;
    public CollectionsActivity() {
        super(R.layout.activity_collect);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true, "收藏");
        dialog = new WaittingDialog(this);
        pull_lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    protected void installListener() {
        pull_lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                resetData();
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }

        });
        pull_lv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore = true;
                requestNet();
            }
        });

//        pull_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(activity, GoodsDetailActivity.class);
//                intent.putExtra("id", mList.get(i)._id);
//                startActivity(intent);
//            }
//        });
    }

    private void resetData() {
        curPage = 1;
        isLoadMore = false;
        mList.clear();
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.getCollectOrdered(String.valueOf(curPage), PAGE_SIZE, PAGE_TYPE, PAGE_EVENT, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                if (curPage == 1) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo == null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                HttpResponse<CollectionData> response = JsonUtil.json2Bean(responseInfo.result, new TypeToken<HttpResponse<CollectionData>>() {
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
                dialog.dismiss();
                if (TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI(List<CollectionItem> list) {
        if (list == null) return;

        Iterator<CollectionItem> iterator = list.iterator();
        while (iterator.hasNext()) {
            CollectionItem item = iterator.next();
            if (null == item.scene_product) iterator.remove();
        }

        if (list.size() == 0) {
//            if (isLoadMore){
//                Util.makeToast("没有更多数据哦！");
//            }else {
//                Util.makeToast("暂无数据！");
//            }
            return;
        }


        curPage++;

        if (adapter == null) {
            mList.addAll(list);
            adapter = new CollectListAdapter(mList, activity);
            pull_lv.setAdapter(adapter);
        } else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_lv != null)
            pull_lv.onRefreshComplete();
    }

}

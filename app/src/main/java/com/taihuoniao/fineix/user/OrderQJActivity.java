package com.taihuoniao.fineix.user;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.taihuoniao.fineix.R;
import com.taihuoniao.fineix.adapters.OrderedQJAdapter;
import com.taihuoniao.fineix.base.BaseActivity;
import com.taihuoniao.fineix.beans.LoginInfo;
import com.taihuoniao.fineix.beans.QingJingListBean;
import com.taihuoniao.fineix.network.ClientDiscoverAPI;
import com.taihuoniao.fineix.qingjingOrSceneDetails.QingjingDetailActivity;
import com.taihuoniao.fineix.utils.JsonUtil;
import com.taihuoniao.fineix.utils.Util;
import com.taihuoniao.fineix.view.CustomHeadView;
import com.taihuoniao.fineix.view.WaittingDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * @author lilin
 * created at 2016/5/5 18:12
 */
public class OrderQJActivity  extends BaseActivity{
    @Bind(R.id.custom_head)
    CustomHeadView custom_head;
    @Bind(R.id.pull_gv)
    PullToRefreshGridView pull_gv;
    private int curPage=1;
    private boolean isLoadMore=false;
    public static final String PAGE_SIZE="10";
    public static final String PAGE_TYPE="scene";
    public static final String PAGE_EVENT="subscription";
    private List<QingJingListBean.QingJingItem> mList=new ArrayList<>();
    private WaittingDialog dialog;
    private OrderedQJAdapter adapter;
    public OrderQJActivity(){
        super(R.layout.activity_order_qj);
    }

    @Override
    protected void initView() {
        custom_head.setHeadCenterTxtShow(true,"订阅的情景");
        dialog=new WaittingDialog(this);
        pull_gv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
    }

    @Override
    protected void installListener() {
        pull_gv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                resetData();
                requestNet();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            }

        });
        pull_gv.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                isLoadMore=true;
                requestNet();
            }
        });

        pull_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(activity, QingjingDetailActivity.class);
                intent.putExtra("id",mList.get(i).get_id());
                startActivity(intent);
            }
        });
    }

    private void resetData(){
        curPage=1;
        isLoadMore=false;
        mList.clear();
    }

    @Override
    protected void requestNet() {
        ClientDiscoverAPI.commonList(String.valueOf(curPage),PAGE_SIZE,null, String.valueOf(LoginInfo.getUserId()),PAGE_TYPE,PAGE_EVENT,new RequestCallBack<String>(){
            @Override
            public void onStart() {
                if (curPage==1) dialog.show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                dialog.dismiss();
                if (responseInfo==null) return;
                if (TextUtils.isEmpty(responseInfo.result)) return;
                QingJingListBean listBean = JsonUtil.fromJson(responseInfo.result, QingJingListBean.class);
                if (listBean.isSuccess()){
                    List list = listBean.getData().getRows();
                    refreshUI(list);
                    return;
                }
                Util.makeToast(listBean.getMessage());
            }

            @Override
            public void onFailure(HttpException e, String s) {
                dialog.dismiss();
                if(TextUtils.isEmpty(s)) return;
                Util.makeToast(s);
            }
        });
    }

    @Override
    protected void refreshUI(List list) {
        if (list==null) return;
        if (list.size()==0){
            if (isLoadMore){
//                Util.makeToast("没有更多数据哦！");
            }else {
                Util.makeToast("暂无数据！");
            }
            return;
        }

        curPage++;

        if (adapter==null){
            mList.addAll(list);
            adapter=new OrderedQJAdapter(mList,activity);
            pull_gv.setAdapter(adapter);
        }else {
            mList.addAll(list);
            adapter.notifyDataSetChanged();
        }

        if (pull_gv !=null)
            pull_gv.onRefreshComplete();
    }

}
